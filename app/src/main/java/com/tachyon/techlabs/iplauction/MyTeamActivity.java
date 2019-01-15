package com.tachyon.techlabs.iplauction;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tachyon.techlabs.iplauction.adapter.MyTeamDataAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyTeamActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String team_name;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userEmail;
    ImageView team_img_view;
    List<String> list;
    int bought,total;
    String [] playernameArray;
    long [] playerpriceArray;
    RecyclerView my_players_view;
    int sizes , index;
    RecyclerView.LayoutManager layoutManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_team);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = Objects.requireNonNull(currentUser).getEmail();

        if(Build.VERSION.SDK_INT>22)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        my_players_view = findViewById(R.id.my_team_recycler_view);
        team_img_view = findViewById(R.id.my_team_img);

        context = getApplicationContext();

        layoutManager = new LinearLayoutManager(context);
        my_players_view.setLayoutManager(layoutManager);

        getTeamImg();
    }

    public void getTeamImg()
    {
        DocumentReference team_img_doc = db.collection("Players").document(userEmail);
        team_img_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        team_name = documentSnapshot.getString("myteam");
                        //Log.d("qwertyuiop",team_name);
                        setTeamImg();
                    }
                    catch(Exception exp)
                    {
                        String error = exp.toString();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    public  void setTeamImg()
    {
        switch (team_name)
        {
            case "MI": team_img_view.setBackground(getDrawable(R.drawable.mumbai_indians_min));
                break;
            case "CSK": team_img_view.setBackground(getDrawable(R.drawable.csk));
                break;
            case "KKR": team_img_view.setBackground(getDrawable(R.drawable.kkr));
                break;
            case "RR": team_img_view.setBackground(getDrawable(R.drawable.mumbai_indians_min));
                break;
            case "RCB": team_img_view.setBackground(getDrawable(R.drawable.rcb));
                break;
            case "DC": team_img_view.setBackground(getDrawable(R.drawable.mumbai_indians_min));
                break;
            case "KXIP": team_img_view.setBackground(getDrawable(R.drawable.kings11_min));
                break;
            case "GL": team_img_view.setBackground(getDrawable(R.drawable.mumbai_indians_min));
                break;
            case "SH": team_img_view.setBackground(getDrawable(R.drawable.mumbai_indians_min));
                break;
            case "RPS": team_img_view.setBackground(getDrawable(R.drawable.mumbai_indians_min));
                break;
        }

        getPlayerBoughtNum();
    }

    public void getPlayerBoughtNum()
    {
        DocumentReference get_num_doc = db.collection("Players").document(userEmail);
        get_num_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        bought = Objects.requireNonNull(documentSnapshot.getLong("players_bought")).intValue();
                        getListOfPlayers();
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void getListOfPlayers()
    {
        db.collection("Players").document(userEmail).collection("MyTeam")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    try
                    {
                        list = new ArrayList<>();
                        for(QueryDocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()))
                        {
                            list.add(documentSnapshot.getId());
                        }
                        total = list.size();
                        //Log.d("qwertyuiop",total+"");
                        playernameArray = new String[total];
                        playerpriceArray = new long[total];
                        setListOfPlayers(1);
                    }
                    catch(Exception ex)
                    {
                        Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }

    public void setListOfPlayers(int size)
    {
        index = size - 1 ;
        sizes = size;
        final String num = size+"";

        DocumentReference setplayer_doc = db.collection("Players").document(userEmail).collection("MyTeam").document(num);
        setplayer_doc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        String n = documentSnapshot.getString("1");
                        //Log.d("qwertyuiop",n);
                        Long l = Long.parseLong(documentSnapshot.getString("2"));
                        playernameArray[index] = n;//documentSnapshot.getString("1");
                        playerpriceArray[index] = l;//Long.parseLong(documentSnapshot.getString("2"));
                        //Toast.makeText(MyTeamActivity.this, playernameArray[index], Toast.LENGTH_SHORT).show();

                        if(sizes<total)
                        {
                            setListOfPlayers(sizes+1);
                        }
                        else
                        {
                            showPLayerData();
                        }
                    }
                    catch(Exception exp)
                    {
                        Toast.makeText(context, exp.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    public void showPLayerData()
    {
        MyTeamDataAdapter myTeamDataAdapter = new MyTeamDataAdapter(getApplicationContext(),playernameArray,playerpriceArray,getResources());
        my_players_view.setAdapter(myTeamDataAdapter);
    }
}
