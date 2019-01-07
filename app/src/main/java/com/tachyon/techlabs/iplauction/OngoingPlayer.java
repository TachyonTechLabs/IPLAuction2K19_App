package com.tachyon.techlabs.iplauction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import javax.annotation.Nullable;

public class OngoingPlayer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView name1text,name2text,pointtext,matchtext,runtext,wickettext,basetext;
    AllPlayerInfo allPlayerInfo = new AllPlayerInfo();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id,boss_name;
    int current;
    Bundle extras;
    AfterRegistrationMainActivity afterRegistrationMainActivity = new AfterRegistrationMainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_player);

        name1text = findViewById(R.id.name1);
        name2text = findViewById(R.id.name2);
        pointtext = findViewById(R.id.pointtext);
        matchtext = findViewById(R.id.matchtext);
        runtext = findViewById(R.id.runstext);
        wickettext = findViewById(R.id.wicketstext);
        basetext =findViewById(R.id.basepricetext);
        extras = getIntent().getExtras();

        getId();
    }

    public void getId()
    {
        /*
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = Objects.requireNonNull(currentUser).getEmail();
        */

        id = extras.getString("roomid");
        userEmail = extras.getString("userEmail");
        boss_name = extras.getString("boss_name");
        getCurrent();

        /*
        DocumentReference documentReference = db.collection("Players").document(useremail);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id = documentSnapshot.getString("roomid");
                getCurrent();
            }
        });
        */
    }

    public void getCurrent()
    {
        DocumentReference documentReference = db.collection(id).document("CurrentPlayer");
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                current = Objects.requireNonNull(Objects.requireNonNull(documentSnapshot).getLong("curr")).intValue();
                //Toast.makeText(OngoingPlayer.this, current+"", Toast.LENGTH_SHORT).show();
                setText();
            }
        });
    }

    public void setText()
    {
        String pt = allPlayerInfo.points[current]+"";
        String match = allPlayerInfo.match[current]+"";
        String run = allPlayerInfo.run[current]+"";
        String wicket = allPlayerInfo.wicket[current]+"";
        String base = allPlayerInfo.basecost[current]+"";
        name1text.setText(allPlayerInfo.fname[current]);
        name2text.setText(allPlayerInfo.sname[current]);
        pointtext.setText(pt);
        matchtext.setText(match);
        runtext.setText(run);
        wickettext.setText(wicket);
        basetext.setText(base);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        switch (id)
        {
            case R.id.nav_home:
                break;

            case R.id.nav_players:
                startActivity(new Intent(OngoingPlayer.this,PLAYERS.class));
                finish();
                break;

            case R.id.nav_profile:
                //Intent prof = new Intent(AfterRegistrationMainActivity.this,ProfileActivity.class);
                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(OngoingPlayer.this,ProfileActivity.class));
                        finish();
                    }
                };
                handler.postDelayed(runnable,250);

                break;

            case R.id.nav_opponents:
                startActivity(new Intent(OngoingPlayer.this,activity_vertical_ntb.class));
                finish();
                break;

            case R.id.nav_payments_info:
                startActivity(new Intent(OngoingPlayer.this,PaymentInfo.class));
                finish();
                break;

            case R.id.nav_cards:
                afterRegistrationMainActivity.storagepermission();

                break;

            case R.id.nav_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                startActivity(Intent.createChooser(share,"Share Via"));
                break;

            case R.id.nav_about_us:
                startActivity(new Intent(OngoingPlayer.this,About.class));
                finish();
                break;

            case R.id.nav_developer:
                startActivity(new Intent(OngoingPlayer.this,about_developers.class));
                finish();
                break;

            case R.id.nav_about_app:
                break;

            case R.id.nav_logout:
                afterRegistrationMainActivity.signOut();

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                        DocumentReference updateRef = db.collection("Players").document(userEmail);
                        updateRef.update("inRoom", 0).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Handler handler = new Handler();
                                Runnable runnable = new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(OngoingPlayer.this, AfterRegistrationMainActivity.class));
                                        finish();
                                    }
                                };
                                handler.postDelayed(runnable, 500);
                                //finish();
                                //Toast.makeText(WaitingForPlayersActivity.this, "Left the room", Toast.LENGTH_SHORT).show();
                                //Toast.makeText(WaitingForPlayersActivity.this, "User details updated", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }                     //DocumentReference start_game = db.collection(id).document(Objects.requireNonNull("START GAME"));
                });
            }
        });
        builder.setNegativeButton(R.string.no,null);
        builder.show();
    }
}
