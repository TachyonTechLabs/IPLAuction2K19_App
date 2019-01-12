package com.tachyon.techlabs.iplauction;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Objects;

public class PranjalActivity extends AppCompatActivity {
    ImageView twitter,google_plus,facebook,instagram,dev;
    Button email_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pranjal_developer);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        dev = findViewById(R.id.dev_img);

        twitter = findViewById(R.id.twitter);
        google_plus = findViewById(R.id.google_plus);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        email_btn = findViewById(R.id.email_btn);


        for(int i=0;i<2;i++)
        {
            setColor(i);
        }

        Drawable drawable = getDrawable(R.drawable.round_blue_btn);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable;
        Objects.requireNonNull(gradientDrawable).setColor(getResources().getColor(R.color.email_btn_back));
        email_btn.setBackground(gradientDrawable);

    }

    public void setColor(int i)
    {
        Drawable drawable = getDrawable(R.drawable.circular_bg);
        GradientDrawable gradientDrawable = (GradientDrawable) drawable;
        if(i%2==0)
        {
            Objects.requireNonNull(gradientDrawable).setColor(getResources().getColor(R.color.twitter_facebook));
            twitter.setBackground(gradientDrawable);
            facebook.setBackground(gradientDrawable);
        }
        else
        {
            Objects.requireNonNull(gradientDrawable).setColor(getResources().getColor(R.color.google_insta));
            google_plus.setBackground(gradientDrawable);
            instagram.setBackground(gradientDrawable);
        }
    }

}

