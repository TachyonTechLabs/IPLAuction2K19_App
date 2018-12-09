package com.tachyon.techlabs.iplauction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.google.firebase.firestore.DocumentReference;

public class PLAYERS extends AppCompatActivity {
    String [] players_name = {"Virat Kohli","MS Dhoni","Ravindra Jadeja"};
    int [] value = {10000000,10000000,2344553};
    int[] points={30,50,10};
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_players);
        listView=findViewById(R.id.listofallplayers);
        listView.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        opponents_team_playerslist_adapter opponents_team_playerslist_adapter = new opponents_team_playerslist_adapter(getApplicationContext(),players_name,value,points);
        listView.setAdapter(opponents_team_playerslist_adapter);


       // listView.setAdapter(opponents_team_playerslist_adapter);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fadein_slidedown);
        listView.startAnimation(animation);

       /* DocumentReference messageRef = db
                .collection("rooms").document("roomA")
                .collection("messages").document("message1");*/


    }
}
