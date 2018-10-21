package com.tachyon.techlabs.iplauction;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    String member,roomid,key;
    String boss;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView bossTextView,joinCodeDisplay;
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
        
    }

    public void setTexts()
    {
        joinCodeDisplay.setText(key);

        if(boss.equals("true"))
        {
            bossTextView.setText(R.string.bosstextview);
        }
        else
        {
          //  String text = R.string.membertextview+"";
            bossTextView.setText(R.string.membertextview);
        }
    }
}
