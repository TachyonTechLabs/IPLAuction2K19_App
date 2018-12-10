package com.tachyon.techlabs.iplauction;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PLAYERS extends AppCompatActivity {
    ListView listView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<Object> price = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        final DocumentReference docRef = db.collection("Players").document(Objects.requireNonNull(currentUser.getEmail())).collection("PlayersList").document("All Players");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                   if (task.isSuccessful()) {
                                                       {
                                                           DocumentSnapshot d = task.getResult();
                                                           if (d != null)
                                                           {


                                                               Map<String, Object> map = task.getResult().getData();
                                                              // Toast.makeText(PLAYERS.this, map.toString(), Toast.LENGTH_SHORT).show();
                                                           for (Map.Entry<String, Object> entry : map.entrySet())
                                                           {
                                                               list.add(entry.getKey());
                                                               Toast.makeText(PLAYERS.this, list.toString(), Toast.LENGTH_SHORT).show();
                                                               Toast.makeText(PLAYERS.this, entry.getKey(), Toast.LENGTH_SHORT).show();
                                                               price.add(entry.getValue());
                                                               Toast.makeText(PLAYERS.this, entry.getValue().toString(), Toast.LENGTH_SHORT).show();
                                                           }
                                                       }
                                                       }
                                                   }
                                               }
                                           });


        //String [] players_name = list.toArray(new String[0]);
        //Integer pri[]= price.toArray(new Integer[price.size()]);
        int[] pric={500000,4000000};
        int[] points={30,23};


    listView=findViewById(R.id.listofallplayers);
        listView.setBackgroundColor(getResources().getColor(R.color.colorAccent));


        opponents_team_playerslist_adapter opponents_team_playerslist_adapter = new opponents_team_playerslist_adapter(getApplicationContext(),list.toArray(new String[0]),pric,points);
        listView.setAdapter(opponents_team_playerslist_adapter);


       // listView.setAdapter(opponents_team_playerslist_adapter);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fadein_slidedown);
        listView.startAnimation(animation);

       /* DocumentReference messageRef = db
                .collection("rooms").document("roomA")
                .collection("messages").document("message1");*/


    }
}
