package com.tachyon.techlabs.iplauction;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class WaitingForPlayersActivity extends AppCompatActivity {

    String boss,member,roomid,key;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView bossTextView,joinCodeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_for_players);

        roomid = getIntent().getExtras().getString("roomId");
        member = getIntent().getExtras().getString("emailId");
        key = getIntent().getExtras().getString("joiningKey");

        bossTextView = findViewById(R.id.boss_text);
        joinCodeDisplay = findViewById(R.id.join_code_display);

       // assert roomid != null;
        DocumentReference docRef = db.collection(roomid).document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists())
                {
                    boss = documentSnapshot.getString("1");
                    setTexts();
                }
            }
        });


    }

    public void setTexts()
    {
        joinCodeDisplay.setText(key);

        if(member.equals(boss))
        {
            bossTextView.setText(R.string.bosstextview);
        }
        else
        {
            String text = boss+" "+R.string.membertextview;
            bossTextView.setText(text);
        }
    }
}
