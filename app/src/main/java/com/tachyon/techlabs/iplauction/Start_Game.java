package com.tachyon.techlabs.iplauction;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

public class Start_Game extends AppCompatActivity {
    Spinner spin;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String[] arr = {"Mumbai Indians", "CSK", "RCB"};
    List<String> array = new ArrayList<>();
    private List<String> teamlist_db = new ArrayList<>();

    final DocumentReference docRef = db.collection("Teams").document(Objects.requireNonNull("All Teams"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__game);
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

    @Override
    protected void onStart() {
        super.onStart();
        docRef.addSnapshotListener(new com.google.firebase.firestore.EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    teamlist_db = (List<String>) documentSnapshot.get("Teams");
                    update(teamlist_db);
                }
            }
        });

    }


  

    public void update(List<String> list)
    {
        Log.d("Array",teamlist_db.toString());
        ArrayAdapter spin_adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, list);
        spin_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spin.setAdapter(spin_adapter);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(parent.getContext(),
                        "OnItemSelectedListener : " + parent.getItemAtPosition(position).toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
