package com.tachyon.techlabs.iplauction;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class PowerCards extends AppCompatActivity {
    public static final String CHANNEL_ID="1001";
    Context context;
    ImageView cardback;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

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

        context = getApplicationContext();

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new cards_adapter(context,imgs,disc,names,price);

        recyclerView.setAdapter(adapter);
        
        /*
        gv= (GridView) findViewById(R.id.gv);

        adapter=new cards_adapter(this,getData());
        gv.setAdapter(adapter);

        */

    }

    public void cardback(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        final Intent cardtomain = new Intent(PowerCards.this,AfterRegistrationMainActivity.class);
        startActivity(cardtomain);
        finish();
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