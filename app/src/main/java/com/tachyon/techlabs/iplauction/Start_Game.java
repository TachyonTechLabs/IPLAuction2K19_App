package com.tachyon.techlabs.iplauction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class Start_Game extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigationView;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userEmail;
    Toolbar toolbarAppName;
    Spinner spin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String[] arr = {"Mumbai Indians", "CSK", "RCB"};
    List<String> array = new ArrayList<>();
    AfterRegistrationMainActivity obj = new AfterRegistrationMainActivity();
    String boss_name,roomid,key;
    String id;
    Map<String, Object> curr = new HashMap<>();

    // List<String> teamlist_db = new ArrayList<>();



    final DocumentReference docRef = db.collection("Teams").document(Objects.requireNonNull("All Teams"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__game);
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        DocumentReference docname = db.collection("Players").document(userEmail);
        docname.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                roomid = documentSnapshot.getString("roomid");
                key = documentSnapshot.getString("joinkey");
                //setStart();
                getBossName();
            }
        });

        if(Build.VERSION.SDK_INT>22)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.background_grey));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        //String name = getIntent().getExtras().getString("name");
        navigationView.setNavigationItemSelectedListener(this);


        array.add("Mumbai");
        array.add("CSK");
        spin = findViewById(R.id.team_listview);







    /*    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    {
                        {
                            DocumentSnapshot d = task.getResult();
                            if (d != null) {
                                teamlist_db = (List<String>) d.get("Teams");
                                update(teamlist_db);

                                // for (String item : teamlist_db) {
                                //   Log.d("playername", item);
                                //}
                            }
                        }
                    }
                }
            }

        }); */


    }

    public  void setStart()
    {
        DocumentReference start = db.collection(roomid).document("START GAME");
        start.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(Objects.requireNonNull(documentSnapshot).exists())
                {
                    int value = Objects.requireNonNull(documentSnapshot.getLong("start")).intValue();
                    if(value != 1)
                    {
                        startActivity(new Intent(Start_Game.this,WaitingForPlayersActivity.class));
                        finish();
                    }
                }
                else
                {
                    startActivity(new Intent(Start_Game.this,AfterRegistrationMainActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    
    @Override
    protected void onStart() {
        super.onStart();
        docRef.addSnapshotListener(new com.google.firebase.firestore.EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                   List<String> teamlist_db = (List<String>) documentSnapshot.get("Teams");
                    update(teamlist_db);
                }
            }
        });

    }



  

    public void update(final List<String> list)
    {
//        Log.d("Array",teamlist_db.toString());
        ArrayAdapter spin_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spin_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spin.setAdapter(spin_adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
                String teamname=(String) parent.getItemAtPosition(position);

                if(position>0) {
                    list.remove(position);
                    docRef.update("Teams", list);

                    Toast.makeText(Start_Game.this, "Your team name is"+parent.getItemIdAtPosition(position), Toast.LENGTH_SHORT).show();
                    DocumentReference documentReference = db.collection("Players").document(userEmail);
                    documentReference.update("Team Name",teamname);

                }


            }




            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        switch (id)
        {
            case R.id.nav_home:
                break;

            case R.id.nav_players:
                startActivity(new Intent(this,PLAYERS.class));
                finish();
                break;

            case R.id.nav_profile:
                //Intent prof = new Intent(this,ProfileActivity.class);
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(Start_Game.this,ProfileActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(runnable,250);

                break;

            case R.id.nav_opponents:
                startActivity(new Intent(this,activity_vertical_ntb.class));
                finish();
                break;

            case R.id.nav_payments_info:
                startActivity(new Intent(this,PaymentInfo.class));
                finish();
                break;

            case R.id.nav_cards:
                //obj.storagepermission();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED)
                {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
                }
                else if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
                {
                    Handler handlerCards = new Handler();
                    Runnable runnableCards = new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Start_Game.this,PowerCards.class));
                            finish();
                        }
                    };
                    handlerCards.postDelayed(runnableCards,250);

                }
                else {
                    Handler handlerCards = new Handler();
                    Runnable runnableCards = new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(Start_Game.this,PowerCards.class));
                            finish();
                        }
                    };
                    handlerCards.postDelayed(runnableCards,250);


                }

                break;

            case R.id.nav_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                startActivity(Intent.createChooser(share,"Share Via"));
                break;

            case R.id.nav_about_us:
                startActivity(new Intent(this,About.class));
                finish();
                break;

            case R.id.nav_developer:
                startActivity(new Intent(Start_Game.this,developers.class));
                finish();
                break;

            case R.id.nav_about_app:
                break;

            case R.id.nav_logout:
                obj.signOut();

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(this.mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit Game?");
            builder.setMessage("Do you really wish to leave the room and exit?");
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.collection(roomid).document(userEmail).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if(userEmail.equals(boss_name))
                            {
                                db.collection(roomid).document("CurrentPlayer").delete();
                            }

                            DocumentReference updateRef = db.collection("Players").document(userEmail);
                            updateRef.update("inRoom",0).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Handler handler = new Handler();
                                    Runnable runnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivity(new Intent(Start_Game.this, AfterRegistrationMainActivity.class));
                                            finish();
                                        }
                                    };
                                    handler.postDelayed(runnable, 500);
                                    //Toast.makeText(WaitingForPlayersActivity.this, "Left the room", Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(WaitingForPlayersActivity.this, "User details updated", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

                }
            });
            builder.setNegativeButton(R.string.no,null);
            builder.show();
            //System.exit(0);
        }

    }

    public void ongoing(View view) {
        curr.put("curr",1);
        DocumentReference documentReference = db.collection(roomid).document("CurrentPlayer");
        documentReference.set(curr).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(boss_name.equals(userEmail))
                {
                    Intent admin = new Intent(Start_Game.this,AdminOngoingPlayer.class);
                    admin.putExtra("roomid",roomid);
                    admin.putExtra("userEmail",userEmail);
                    admin.putExtra("boss_name",boss_name);
                    startActivity(admin);
                    finish();
                }
                else
                {
                    Intent member = new Intent(Start_Game.this,OngoingPlayer.class);
                    member.putExtra("roomid",roomid);
                    member.putExtra("userEmail",userEmail);
                    member.putExtra("boss_name",boss_name);
                    startActivity(member);
                    finish();
                }
            }
        });

    }

    public void getBossName()
    {
        DocumentReference doc = db.collection("keyValues").document(key);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                boss_name = documentSnapshot.getString("owner");
            }
        });
    }
}
