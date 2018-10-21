package com.tachyon.techlabs.iplauction;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.ArrayList;

import javax.annotation.Nullable;

public class WaitingForPlayersActivity extends AppCompatActivity {

    String member,roomid,key,boss_namee;
    String boss;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView bossTextView,joinCodeDisplay,boss_name;
   ListView members_joined;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_players);

        roomid = getIntent().getExtras().getString("roomId");
        member = getIntent().getExtras().getString("emailId");
        key = getIntent().getExtras().getString("joiningKey");
        members_joined= (ListView) findViewById(R.id.waiting_player_listview);

        bossTextView = findViewById(R.id.boss_text);
        boss_name = findViewById(R.id.boss_name);

        joinCodeDisplay = findViewById(R.id.join_code_display);

       // assert roomid != null;
        DocumentReference docRef = db.collection(roomid).document(member);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                boss = documentSnapshot.getString("Owner");
                setTexts();
            }
        });

        DocumentReference doc = db.collection("keyValues").document(key);
        doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                boss_namee = documentSnapshot.getString("owner");
                setTexts();
            }
        });

    }

    public void setTexts()
    {
        joinCodeDisplay.setText(key);

        if(boss.equals("true"))
        {
            bossTextView.setText(R.string.bosstextview);
            boss_name.setVisibility(View.GONE);
        }
        else
        {
          //  String text = R.string.membertextview+"";
            boss_name.setText(boss_namee);
            bossTextView.setText(R.string.membertextview);
        }
    }
}
