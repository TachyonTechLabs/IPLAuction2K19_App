package com.tachyon.techlabs.iplauction;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AdminOngoingPlayer extends AppCompatActivity {

    List<String> list;
    AllPlayerInfo allPlayerInfo = new AllPlayerInfo();
    ArrayAdapter<String> adapter;
    String [] players;
    ListView playerlist;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userEmail,id,boss_name;
    HashMap<String,Boolean> timer;
    Bundle extras;
    private Menu menu;
    String storyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ongoing_player);


        //list = new ArrayList<>(Arrays.asList(allPlayerInfo.fullname));
        //list.remove(0);

        playerlist = findViewById(R.id.adminlist);
        getId();

        extras = getIntent().getExtras();
        
    }

    public void getId()
    {

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = Objects.requireNonNull(currentUser).getEmail();
        /*
        id = extras.getString("roomid");
        userEmail = extras.getString("userEmail");
        boss_name = extras.getString("boss_name");
        setCurrentPlayer();
        */

        DocumentReference documentReference = db.collection("Players").document(userEmail);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id = documentSnapshot.getString("roomid");
                getStoryLine();
                setCurrentPlayer();
            }
        });

    }

    public void getStoryLine()
    {
        DocumentReference story_ref = db.collection(id).document("Story");
        story_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                storyline = Objects.requireNonNull(documentSnapshot.get("story")).toString();
                setStoryLine();
            }
        });
    }

    public void setStoryLine()
    {
        db.collection(storyline).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    list = new ArrayList<>();
                    for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()))
                    {
                        list.add(documentSnapshot.getId());
                    }

                    setDisplayPlayer();
                }
            }
        });
    }

    public void setDisplayPlayer()
    {
        players = new String[list.size()];
        players = list.toArray(players);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,players);
        playerlist.setAdapter(adapter);
    }


    public void setCurrentPlayer()
    {
        final DocumentReference documentReference = db.collection(id).document("CurrentPlayer");
        playerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                documentReference.update("curr",list.get(position));
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Game?");
        builder.setMessage("Do you really wish to leave the room and exit?");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.collection(id).document(userEmail).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        db.collection(id).document("START GAME").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection(id).document("CurrentPlayer").delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        DocumentReference updateRef = db.collection("Players").document(userEmail);
                                        updateRef.update("inRoom",0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                Handler handler = new Handler();
                                                Runnable runnable = new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        startActivity(new Intent(AdminOngoingPlayer.this,AfterRegistrationMainActivity.class));
                                                        finish();
                                                    }
                                                };
                                                handler.postDelayed(runnable,500);
                                                //finish();
                                                //Toast.makeText(WaitingForPlayersActivity.this, "Left the room", Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(WaitingForPlayersActivity.this, "User details updated", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                        //DocumentReference start_game = db.collection(id).document(Objects.requireNonNull("START GAME"));

                    }
                });

            }
        });
        builder.setNegativeButton(R.string.no,null);
        builder.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
      MenuItem searchMenuItem = menu.findItem(R.id.action_search);
      SearchView  searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }




}
