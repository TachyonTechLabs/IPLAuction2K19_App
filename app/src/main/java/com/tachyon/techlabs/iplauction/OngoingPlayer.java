package com.tachyon.techlabs.iplauction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import javax.annotation.Nullable;

public class OngoingPlayer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView name1text,name2text,pointtext,matchtext,runtext,wickettext,basetext;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle mToggle;
    Toolbar ongoing_toolbar;
    AllPlayerInfo allPlayerInfo = new AllPlayerInfo();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userEmail;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id,boss_name;
    int current;
    Bundle extras;
    AfterRegistrationMainActivity afterRegistrationMainActivity = new AfterRegistrationMainActivity();
    TextView toolbar_text;
    FirebaseStorage storage;
    StorageReference storageRef;
    ImageView player_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_player);

        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReference();

        name1text = findViewById(R.id.player_name1);
        name2text = findViewById(R.id.player_name2);
        pointtext = findViewById(R.id.point_text);
        //matchtext = findViewById(R.id.matchtextvalue);
        //runtext = findViewById(R.id.runstextvalue);
        //wickettext = findViewById(R.id.wicketstextvalue);
        basetext = findViewById(R.id.base_price_value);
        player_img = findViewById(R.id.player_img);
        extras = getIntent().getExtras();

        //toolbar_text = findViewById(R.id.app_toolbar_nametxt);

        ongoing_toolbar = (Toolbar) findViewById(R.id.app_toolbar);
        setSupportActionBar(ongoing_toolbar);
        //toolbar_text.setText(R.string.ongoing_player);
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.placeholder);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //getSupportActionBar().setHomeAsUpIndicator(getDrawable(android.R.drawable.ic_menu_close_clear_cancel));


        mDrawerLayout = (DrawerLayout) findViewById(R.id.mydrawer);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,ongoing_toolbar,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        NavigationView nav = (NavigationView) findViewById(R.id.navigation_view);
        //View headerView = navigationView.getHeaderView(0);

        //String name = getIntent().getExtras().getString("name");
        nav.setNavigationItemSelectedListener(this);

        getId();
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
        getCurrent();
        */

        DocumentReference documentReference = db.collection("Players").document(userEmail);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id = documentSnapshot.getString("roomid");
                getCurrent();
            }
        });

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
        String fullname = allPlayerInfo.fullname[current]+"";
        String pt = allPlayerInfo.points[current]+"";
        String match = allPlayerInfo.match[current]+"";
        String run = allPlayerInfo.run[current]+"";
        String wicket = allPlayerInfo.wicket[current]+"";
        String base = allPlayerInfo.basecost[current]+"";
        name1text.setText(allPlayerInfo.fname[current]);
        name2text.setText(allPlayerInfo.sname[current]);
        pointtext.setText(pt);
        //matchtext.setText(match);
        //runtext.setText(run);
        //wickettext.setText(wicket);
        basetext.setText(base);

        //storageRef.child(fullname+".png");
        storageRef.child(fullname+".png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("playerimg",uri.toString());
                Glide.with(getApplicationContext()).load(uri.toString()).into(player_img);
                //GlideApp.with(OngoingPlayer.this).load(storageRef).into(player_img);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("playerimg","fail");
            }
        });
        //GlideApp.with(OngoingPlayer.this).load(storageRef).into(player_img);
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
                //afterRegistrationMainActivity.storagepermission();
                storagepermission();

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
                startActivity(new Intent(OngoingPlayer.this,developers.class));
                finish();
                break;

            case R.id.nav_about_app:
                break;

            case R.id.nav_logout:
                afterRegistrationMainActivity.signOut();

                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mydrawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void storagepermission()
    {

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
                    startActivity(new Intent(OngoingPlayer.this,PowerCards.class));
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
                    startActivity(new Intent(OngoingPlayer.this,PowerCards.class));
                    finish();
                }
            };
            handlerCards.postDelayed(runnableCards,250);


        }
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
