package com.tachyon.techlabs.iplauction;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AdminOngoingPlayer extends AppCompatActivity {

    List<String> list,fix;
    List<String> amount_multiply=new ArrayList<>();
    Spinner amount_multipler;
    AllPlayerInfo allPlayerInfo = new AllPlayerInfo();
    ArrayAdapter<String> adapter;
    String [] players;
    ListView playerlist,fixlist;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    Button phase,fixed;
    FirebaseUser currentUser;
    String userEmail,id,boss_name;
    HashMap<String,Boolean> timer;
    HashMap<String,Object> sell = new HashMap<>();
    HashMap<String,Integer> statemap = new HashMap<>();
    Bundle extras;
    private Menu menu;
    String storyline,current_player;
    TextView ipl_player;
    int num_bought;
    TextInputEditText bid_value;
    List<String> users;
    String [] usernames;
    String [] userTeam;
    Spinner spinner;
    String selectedUser;
    double bought_value;
    double currentAmount;
    int s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ongoing_player);
        phase=findViewById(R.id.phase);
        fixed=findViewById(R.id.fixed_players);


        //list = new ArrayList<>(Arrays.asList(allPlayerInfo.fullname));
        //list.remove(0);

        playerlist = findViewById(R.id.adminlist);
        fixlist = findViewById(R.id.adminlist);
        ipl_player = findViewById(R.id.ipl_player_admin_text);
        bid_value = findViewById(R.id.text_input_value_admin);
        spinner = findViewById(R.id.player_spinner);
        amount_multipler=findViewById(R.id.spinner_amount_multiplier);
        amount_multiply_add();
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
                if(documentSnapshot.exists())
                {
                    try
                    {
                        id = documentSnapshot.getString("roomid");
                        currentAmount = Objects.requireNonNull(documentSnapshot.getLong("Current_Amount")).intValue();
                        addUserList();
                        getStoryLine();
                        setCurrentPlayer();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(AdminOngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

    }

    public void getStoryLine()
    {
        DocumentReference story_ref = db.collection(id).document("Story");
        story_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        storyline = Objects.requireNonNull(documentSnapshot.get("story")).toString();
                        setStoryLine();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(AdminOngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

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

    public void setFixedPlayer()
    {
        db.collection("Fixed Players").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    fix = new ArrayList<>();
                    for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()))
                    {
                        fix.add(documentSnapshot.getId());
                    }

                    setFixed();
                }
            }
        });
    }

    public void setFixed()
    {
        Collections.sort(fix);
        players = new String[fix.size()];
        players = fix.toArray(players);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,players);
        playerlist.setAdapter(adapter);
    }

    public void setDisplayPlayer()
    {
        Collections.sort(list);
        players = new String[list.size()];
        players = list.toArray(players);
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,players);
        fixlist.setAdapter(adapter);
    }


    public void setCurrentPlayer()
    {
        final DocumentReference documentReference = db.collection(id).document("CurrentPlayer");
        playerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(s==0)
                {
                    documentReference.update("curr",list.get(position));
                    ipl_player.setText(list.get(position));
                    current_player = list.get(position);
                }
                else if(s==1)
                {
                    documentReference.update("curr",fix.get(position));
                    ipl_player.setText(fix.get(position));
                    current_player = fix.get(position);
                }

            }
        });

        /*
        fixlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(s==0)
                {
                    documentReference.update("curr",list.get(position));
                    ipl_player.setText(list.get(position));
                    current_player = list.get(position);
                }
                else if(s==1)
                {
                    documentReference.update("curr",fix.get(position));
                    ipl_player.setText(fix.get(position));
                    current_player = fix.get(position);
                }

            }
        });
        */

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


    public void sellPlayer(View view) {
        if(TextUtils.isEmpty(bid_value.getText().toString().trim()))
        {
            bid_value.setError("Enter Amount");
        }
        else if(TextUtils.isEmpty(spinner.getSelectedItem().toString().trim()))
        {
            Toast.makeText(this, "Select the team who bought player", Toast.LENGTH_SHORT).show();
        }
        else
        {
            selectedUser = spinner.getSelectedItem().toString();
            getNumber();
        }

    }

    public void getNumber()
    {
        DocumentReference getNum_doc = db.collection("Players").document(selectedUser);
        getNum_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        num_bought = Objects.requireNonNull(documentSnapshot.getLong("players_bought")).intValue();
                        num_bought+=1;
                        sellCurrentPlayer();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(AdminOngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void addUserList()
    {
        getStatePlayer();
        db.collection(id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                users = new ArrayList<>();
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    if(!documentSnapshot.getId().equals("CurrentPlayer") && !documentSnapshot.getId().equals("START GAME") && !documentSnapshot.getId().equals("Story"))
                    users.add(documentSnapshot.getId());
                }
                users.remove(userEmail);
                usernames = new String[users.size()];

                for(int i=0;i<users.size();i++)
                {
                    usernames[i] = users.get(i);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminOngoingPlayer.this,android.R.layout.simple_spinner_dropdown_item,usernames);
                spinner.setAdapter(adapter);
                //addTeamList();
            }
        });
    }

    public void getStatePlayer()
    {
        DocumentReference doc = db.collection(id).document("State");
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                s = Objects.requireNonNull(documentSnapshot.getLong("state")).intValue();
            }
        });

    }

    public  void addTeamList()
    {
        userTeam = new String[users.size()];
        DocumentReference add_team_doc = db.collection("Players").document(selectedUser);
        add_team_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        for(int i =0;i<users.size();i++)
                        {
                            userTeam[i] = documentSnapshot.getString("myteam");
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AdminOngoingPlayer.this,android.R.layout.simple_spinner_dropdown_item,userTeam);
                        spinner.setAdapter(adapter);
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(AdminOngoingPlayer.this,e.toString() , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void sellCurrentPlayer()
    {
        bought_value = Double.parseDouble(bid_value.getText().toString().trim());
        String multiply=amount_multipler.getSelectedItem().toString();
        double final_amt=multiply_with(bought_value,multiply);
        currentAmount = currentAmount - final_amt;
        sell.clear();
        sell.put(current_player,final_amt);
     //   sell.put("2",final_amt);
        DocumentReference sell_doc = db.collection("Players").document(selectedUser)
                .collection("MyTeam").document("1");

        sell_doc.set(sell,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                updateNumber();
                Toast.makeText(AdminOngoingPlayer.this, "Player sold successfully", Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminOngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateNumber()
    {
        DocumentReference updateNUm_doc = db.collection("Players").document(selectedUser);
        updateNUm_doc.update("players_bought",num_bought);
        updateNUm_doc.update("Current_Amount",currentAmount);
    }

    public void setState(View view) {
        DocumentReference state_doc = db.collection(id).document("State");
        state_doc.update("state",1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fixed.setText("Fixed Players");
            }
        });
        s = 1;
        setFixedPlayer();
    }

    private void amount_multiply_add() {
        amount_multiply.add("Lakhs");
        amount_multiply.add("Crore");

        ArrayAdapter amountspin_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item,amount_multiply );
        amountspin_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        amount_multipler.setAdapter(amountspin_adapter);
    }

    private double multiply_with(Double users_price,String multiply)
    {
        double bid_amt;


        if(multiply.equals("Crore"))
        {

            double multiplier = 10000000;
            bid_amt = (users_price * multiplier);
            return bid_amt;
        }
        else if(multiply.equals("Lakhs"))
        {
            double multiplier = 100000;
            bid_amt =  (users_price * multiplier);
            return bid_amt;

        }
        return 0;

    }


    public void phase_set(View view) {

        DocumentReference state_doc = db.collection(id).document("State");
        state_doc.update("phase",1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                phase.setText("Phase 1");

            }
        });

    }
}
