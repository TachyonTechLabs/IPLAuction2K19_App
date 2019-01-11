package com.tachyon.techlabs.iplauction;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class about_developers extends AppCompatActivity {
    CardView dev1_card,dev2_card;
    ImageView dev1_img,dev2_img;
    TextView dev1_name_text,dev2_name_text,dev1_desc_text,dev2_desc_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developers);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.oneui_grey_back));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        dev1_card = findViewById(R.id.card_dev1);
        dev2_card = findViewById(R.id.card_dev2);
        dev1_img = findViewById(R.id.dev1_main_img);
        dev2_img = findViewById(R.id.dev2_main_img);
        dev1_name_text = findViewById(R.id.dev1_main_name);
        dev2_name_text = findViewById(R.id.dev2_main_name);
        dev1_desc_text = findViewById(R.id.dev1_main_descc);
        dev2_desc_text = findViewById(R.id.dev2_main_descc);

        dev1_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharedIntent = new Intent(about_developers.this,PranjalActivity.class);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View,String>(dev1_img,"dev_image_transition");
                pairs[1] = new Pair<View,String>(dev1_name_text,"dev_name_transition");
                pairs[2] = new Pair<View,String>(dev1_desc_text,"dev_desc_transition");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(about_developers.this,pairs);

                startActivity(sharedIntent,options.toBundle());
            }
        });

        dev2_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharedIntent = new Intent(about_developers.this,AbhishekActivity.class);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View,String>(dev2_img,"dev_image_transition");
                pairs[1] = new Pair<View,String>(dev2_name_text,"dev_name_transition");
                pairs[2] = new Pair<View,String>(dev2_desc_text,"dev_desc_transition");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(about_developers.this,pairs);

                startActivity(sharedIntent,options.toBundle());
            }
        });
    }
}
