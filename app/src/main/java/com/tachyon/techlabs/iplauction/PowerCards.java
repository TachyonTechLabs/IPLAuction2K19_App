package com.tachyon.techlabs.iplauction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class PowerCards extends AppCompatActivity {
    public static final String CHANNEL_ID="1001";
    Context context;
    ImageView cardback;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public RelativeLayout bottonSheetLayout;
    public BottomSheetBehavior bottomSheetBehavior;
    View bgView;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView txt_bs_name,txt_bs_desc,txt_bs_value;
    DocumentReference mainDoc;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String userEmail;
    int currentAmount,currentNumOfCards;
    AppConstants appConstants = new AppConstants();
    int card_amount=0;

    /*
    GridView gv;
   static Context c;
    public NotificationManagerCompat notificationManager;
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_recyclerview);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        int[] imgs = {R.drawable.ipl_logo,
                R.drawable.ipl_logo,
                R.drawable.ipl_logo,
                R.drawable.ipl_logo};

        String[] names = {"YORKER",
                "NO BALL",
                "RIGHT TO MATCH",
                "LEGEND CARDS"};

        String[] disc = {"DESCRIPTION",
                "DESCRIPTION",
                "DESCRIPTION",
                "DESCRIPTION"};

        String[] price = {"PRICE",
                "PRICE",
                "PRICE",
                "PRICE"};

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userEmail = Objects.requireNonNull(currentUser).getEmail();

        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        bottonSheetLayout = findViewById(R.id.botton_sheet_layout_id);
        bottomSheetBehavior = BottomSheetBehavior.from(bottonSheetLayout);

        txt_bs_name = findViewById(R.id.card_name_bs);
        txt_bs_desc = findViewById(R.id.card_desc_bs);
        txt_bs_value = findViewById(R.id.card_pricevalue_bs);

        adapter = new cards_adapter(context,imgs,disc,names,price,bottonSheetLayout,bottomSheetBehavior,txt_bs_name,txt_bs_desc,txt_bs_value);

        recyclerView.setAdapter(adapter);

        mainDoc = db.collection("Players").document(userEmail);
        mainDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentAmount = Objects.requireNonNull(documentSnapshot.getLong("Current_Amount")).intValue();
                currentNumOfCards = Objects.requireNonNull(documentSnapshot.getLong("numberOfCards")).intValue();
            }
        });



        bgView = findViewById(R.id.bgVisible);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    bgView.setVisibility(View.GONE);
                    getWindow().setStatusBarColor(ContextCompat.getColor(bottomSheet.getContext(), R.color.white));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                bgView.setVisibility(View.VISIBLE);
                bgView.setAlpha(slideOffset);
            }
        });

        bgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    getWindow().setStatusBarColor(ContextCompat.getColor(v.getContext(), R.color.white));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }

            }
        });

        /*
        gv= (GridView) findViewById(R.id.gv);

        adapter=new cards_adapter(this,getData());
        gv.setAdapter(adapter);

        */

    }

    public void buyCard(View view)
    {
        String cardName = txt_bs_name.getText().toString();
        switch (cardName)
        {
            case "YORKER": card_amount = appConstants.Yorker_price ;
                break;
            case "NO BALL" : card_amount = appConstants.noBall_price;
                break;
            case "RIGHT TO MATCH": card_amount = appConstants.rightToMatch_price;
                break;
            case "LEGEND CARDS": card_amount = appConstants.legendCards;
                break;
        }
        int current_amount = currentAmount - card_amount;
        DocumentReference documentReference = db.collection("Players").document(userEmail);
        documentReference.update("Current_Amount",current_amount);
        documentReference.update("numberOfCards",currentNumOfCards+1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Toast.makeText(context, "Bought", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    public void cardback(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
        {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        else
        {
            super.onBackPressed();
            final Intent cardtomain = new Intent(PowerCards.this,AfterRegistrationMainActivity.class);
            startActivity(cardtomain);
            finish();
        }

    }

    /*
    private ArrayList getData()
    {
        ArrayList<Power_Cards> Power_Cardss=new ArrayList<>();

        Power_Cards s=new Power_Cards();
        s.setName("Yorker");
        s.setDescription("Description");
        s.setImage(R.drawable.ipl_logo);
        s.setPrice(2000);
        Power_Cardss.add(s);

        s=new Power_Cards();
        s.setName("No Ball");
        s.setDescription("Description");
        s.setImage(R.drawable.ipl_logo);
        s.setPrice(2000);
        Power_Cardss.add(s);

        s=new Power_Cards();
        s.setName("Right To Match");
        s.setDescription("Description");
        s.setImage(R.drawable.ipl_logo);
        s.setPrice(200);
        Power_Cardss.add(s);

        s=new Power_Cards();
        s.setName("Legend Cards");
        s.setDescription("Description");
        s.setImage(R.drawable.ipl_logo);
        s.setPrice(200);
        Power_Cardss.add(s);

        return Power_Cardss;
    }

    */

    /*
    NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(c, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Card Purchase")
            .setContentText("You purschased"+"card")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

    public void buy_card(View view) {
       // notificationManager.notify(12345, mBuilder.build());
        Log.d("LOGMessage","Buy card");
        Toast.makeText(this, "buy_card_fn_call", Toast.LENGTH_SHORT).show();



    }
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name =("Cards");
            String description = "heyyy";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
           // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(PowerCards.this);
            notificationManager.createNotificationChannel(channel);
        }
    }

    */



}