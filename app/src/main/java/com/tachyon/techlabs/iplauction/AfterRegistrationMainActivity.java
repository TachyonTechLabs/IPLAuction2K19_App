package com.tachyon.techlabs.iplauction;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class AfterRegistrationMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FirebaseAuth mAuth;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    NavigationView navigationView;
    Toolbar toolbarAppName;
    TextView textViewAppName,textname,textedit;
    EditText user_entered_code;
    String appName;
    Map<String, Object> user = new HashMap<>();
    Map<String, Object> members = new HashMap<>();
    Map<String, Object> keyvalues = new HashMap<>();
    Map<String, Object> used = new HashMap<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String userEmail,numUsed;
    String user_joincode,used_joinKey,joinkey,roomID,numOfMembers;
    int newjoinkey,numofmem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_registration_main);

        if(Build.VERSION.SDK_INT>22)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.background_grey));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        appName = getString(R.string.app_name);
        toolbarAppName = findViewById(R.id.app_toolbar);
        textViewAppName = findViewById(R.id.app_toolbar_nametxt);
        textViewAppName.setText(appName);

        setSupportActionBar(toolbarAppName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(getDrawable(android.R.drawable.ic_menu_close_clear_cancel));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);

        //String name = getIntent().getExtras().getString("name");
        navigationView.setNavigationItemSelectedListener(this);

        textname = findViewById(R.id.text);
        textedit = findViewById(R.id.edittext);





        // Access a Cloud Firestore instance from your Activity



        // Create a new user with a first and last name



// Add a new document with a generated ID



    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }
    // [END on_start_check_user]

    public void updateUI(FirebaseUser user) {
        if (user != null) {

            userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            DocumentReference docRef = db.collection("users").document(userEmail);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot.exists())
                    {
                        textname.setText(documentSnapshot.getString("first"));
                    }
                }
            });
        }
        else
        {
            goToLogin();
        }
    }

    public void goToLogin()
    {
        Intent log = new Intent(AfterRegistrationMainActivity.this,MainActivity.class);
        startActivity(log);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(this.mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            this.mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            onResume();
            Intent lastActivity = new Intent(Intent.ACTION_MAIN);
            lastActivity.addCategory(Intent.CATEGORY_HOME);
            lastActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            lastActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            lastActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(lastActivity);
            finish();
            //System.exit(0);
        }

    }

    public void pushData(View view) {

        user.put("first", textedit.getText().toString());
        user.put("last", "Two");
        user.put("born", 3);

        db.collection("users").document(userEmail)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        readData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public void readData()
    {
        DocumentReference docRef = db.collection("users").document(userEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        textname.setText(document.getString("first"));
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                    //Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void create_room(View view) {
        Random_id_generate obj=new Random_id_generate();
        long random_id=obj.id();
        final String id=random_id+"";
        textname.setText(id);

        user.put("1",userEmail);
        user.put("numberOfMembers",1+"");

        db.collection(id).document("Members")
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error adding document", e);
                    }
                });

        joinkey = id.substring(5);

        keyvalues.put("roomId",id);
        keyvalues.put("joinKey",joinkey);
        keyvalues.put("owner",userEmail);

        if(checkAvailability(joinkey))
        {
            db.collection("keyValues").document(joinkey)
                    .set(keyvalues)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void v) {
                            // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            saveUsedKey();

                            Intent roomCreated = new Intent(AfterRegistrationMainActivity.this,WaitingForPlayersActivity.class);
                            roomCreated.putExtra("emailId",userEmail);
                            roomCreated.putExtra("roomId",id);
                            roomCreated.putExtra("joiningKey",joinkey);
                            startActivity(roomCreated);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log.w(TAG, "Error adding document", e);
                        }
                    });


        }


    }

    public void saveUsedKey()
    {
        int number = Integer.parseInt(numUsed) + 1;
        used.put(number+"",joinkey);
        used.put("numUsed",number+"");
        db.collection("keyValues").document("usedKey")
                .set(used)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
    }

    public boolean checkAvailability(String joinkey)
    {
        DocumentReference docRef = db.collection("keyValues").document("usedKey");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    //textname.setText(documentSnapshot.getString(""));
                    numUsed = documentSnapshot.getString("numUsed");
                    checkFurther(documentSnapshot.getString("numUsed"));
                }
            }
        });

        return true;
    }

    public void checkFurther(final String num)
    {
        DocumentReference docRef = db.collection("keyValues").document("usedKey");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    //textname.setText(documentSnapshot.getString(""));
                    used_joinKey = documentSnapshot.getString(num);
                    checkStep2(Integer.parseInt(num));
                }
            }
        });


    }

    public void checkStep2(int num)
    {
        if(joinkey.equals(used_joinKey))
        {
            Toast.makeText(this, used_joinKey, Toast.LENGTH_SHORT).show();
            newjoinkey = Integer.parseInt(joinkey) + 1;
            joinkey = newjoinkey+"";
            //checkAvailability(joinkey);
            num = num - 1;
            if(num>0)
            checkFurther(num+"");
        }
    }

    public void Join_Room(View view)  {
        //Dialog_fragment dialog=new Dialog_fragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        final View viewlayout = inflater.inflate(R.layout.join_room_dialog, null);



        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(viewlayout)
                // Add action buttons
                .setPositiveButton(R.string.joinroom, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        user_entered_code = viewlayout.findViewById(R.id.code);
                        user_joincode = user_entered_code.getText().toString();

                        DocumentReference docRef = db.collection("keyValues").document(user_joincode);
                        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                if(documentSnapshot.exists())
                                {
                                    roomID = documentSnapshot.getString("roomId");
                                    enterRoom();
                                }
                            }
                        });

                        dialog.cancel();
                        // sign in the user ...
                       // Toast.makeText(this,"Joined successfully",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
       AlertDialog alert=builder.create();
       alert.show();

    }

    public void enterRoom()
    {
        DocumentReference docRef2 = db.collection(roomID).document("Members");
        docRef2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    numOfMembers = documentSnapshot.getString("numberOfMembers");
                    proceedFurther();

                }
            }
        });

    }

    public void proceedFurther()
    {
        numofmem = Integer.parseInt(numOfMembers)+1;

        members.put(numofmem+"",userEmail);
        members.put("numberOfMembers",numofmem+"");

        db.collection(roomID).document("Members")
                .set(keyvalues)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                        Intent roomCreated = new Intent(AfterRegistrationMainActivity.this,WaitingForPlayersActivity.class);
                        roomCreated.putExtra("emailId",userEmail);
                        roomCreated.putExtra("roomId",roomID);
                        roomCreated.putExtra("joiningKey",joinkey);
                        startActivity(roomCreated);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}
