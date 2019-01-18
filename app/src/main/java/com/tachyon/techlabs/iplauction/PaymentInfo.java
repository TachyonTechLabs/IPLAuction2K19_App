package com.tachyon.techlabs.iplauction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.wajahatkarim3.easyflipview.EasyFlipView;
//import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

public class PaymentInfo extends AppCompatActivity  {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    int totalHistory;
    String itemName="";
    int itemValue=0;
    String [] itemNameArray;
    long [] itemValueArray;
    ListView listViewHistory;
    ArrayList<String> item = new ArrayList<>();
    ArrayList<Long> val = new ArrayList<>();
    int index,currentIndex;
    View view;
    MainActivity mainActivity = new MainActivity();
    public TextView cardnum1,cardnum2,cardnum3,cardnum4,cardholder;
    public StringBuilder stringBuilder;
    SharedPreferences pref ;
    // EasyFlipView easyFlipView = (EasyFlipView) findViewById(R.id.cardFlipView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);


        if(Build.VERSION.SDK_INT>22)
        {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        pref= getApplicationContext().getSharedPreferences("Cards_Bid", 0); // 0 - for private mode
       // LayoutInflater li = LayoutInflater.from(this);

//        easyFlipView.flipTheView();


       // View v = li.inflate(R.layout.payment_card_layout,null,false);


     /*   findViewById(R.id.payment_card_front).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                easyFlipView.setFlipDuration(1000);
                easyFlipView.setFlipEnabled(true);
               // Toast.makeText(SimpleViewFlipActivity.this, "Front Card", Toast.LENGTH_SHORT).show();
                easyFlipView.flipTheView();
            }
        }); */



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        listViewHistory = findViewById(R.id.orders_history_listview);
        cardholder = findViewById(R.id.card_holder_name);

        cardholder.setText(Objects.requireNonNull(currentUser.getDisplayName()).toUpperCase());


        cardnum1 = findViewById(R.id.card_num_1);
        cardnum2 = findViewById(R.id.card_num_2);
        cardnum3 = findViewById(R.id.card_num_3);
        cardnum4 = findViewById(R.id.card_num_4);

        readCardNum();
        setHistoryDisplay();
        /*
        cardnum1.setText(mainActivity.card1);
        cardnum2.setText(mainActivity.card2);
        cardnum3.setText(mainActivity.card3);
        cardnum4.setText(mainActivity.card4);
        */

        /*
        DocumentReference docRef = db.collection("Players").document(Objects.requireNonNull(currentUser.getEmail()));
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists())
                {
                    try
                    {
                        totalHistory = Objects.requireNonNull(documentSnapshot.getLong("itemsPurchased")).intValue();
                        itemNameArray = new String[totalHistory];
                        itemValueArray = new int[totalHistory];
                        //Toast.makeText(PaymentInfo.this, totalHistory+"", Toast.LENGTH_SHORT).show();
                        //totalHistory = totalHistory+1;
                        index = totalHistory;
                        if(index!=0)
                            setHistory(totalHistory);
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(mainActivity, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        */





    }


    public void readCardNum()
    {


        try
        {

            File sdCard = getCacheDir();
            File file = new File(sdCard,"CardNum.txt");
            stringBuilder = new StringBuilder();
            BufferedReader br =  new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append('\n');
            }
            br.close();


            cardnum1.setText(stringBuilder.substring(0,4));
            cardnum2.setText(stringBuilder.substring(4,8));
            cardnum3.setText(stringBuilder.substring(8,12));
            cardnum4.setText(stringBuilder.substring(12,16));

        }
        catch (IOException e)
        {
            e.printStackTrace();

            cardnum1.setText("");
            cardnum1.setText("");
            cardnum1.setText("");
            cardnum1.setText("8286");
        }

        //cardnum1.setTextSize(getResources().getDimension(R.dimen.textsize));
       }

    public void setHistory(int total)
    {
        currentIndex = index - total;
            DocumentReference documentReference = db.collection("Players").document(Objects.requireNonNull(currentUser.getEmail()))
                 /*   .collection("paymentHistory").document(total + "")*/;
            documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                    if(documentSnapshot.exists())
                    {
                        try
                        {
                            itemNameArray[currentIndex] = Objects.requireNonNull(documentSnapshot).getString("0");
                            itemValueArray[currentIndex] = Objects.requireNonNull(documentSnapshot.getLong("1")).intValue();
                            //Toast.makeText(PaymentInfo.this, itemName, Toast.LENGTH_SHORT).show();

                            //itemNameArray[0] = itemName;
                            //itemValueArray[0] = itemValue;
                            //itemNameArray[1] = itemName;
                            //itemValueArray[1] = itemValue;
                            //Toast.makeText(PaymentInfo.this, itemNameArray[1], Toast.LENGTH_SHORT).show();
                            //item.add(itemName);
                            //val.add(itemValue);
                            //setHistoryDisplay();
                            totalHistory=totalHistory-1;
                            if(totalHistory>0)
                            {
                                setHistory(totalHistory);
                            }
                            else {
                                setHistoryDisplay();
                            }
                        }
                        catch(Exception exp)
                        {
                            Toast.makeText(mainActivity, exp.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });

    }

    public void setHistoryDisplay()
    {
        //String [] one = {"qwq","qweqwewe","qweqweqwewqewqewq"};
        //int [] two = {2121,12121,3434};
        //Toast.makeText(this, itemNameArray[1], Toast.LENGTH_SHORT).show();
        val.add(pref.getLong("legend",0));
        val.add(pref.getLong("yorker",0));
        val.add(pref.getLong("rtm",0));
        val.add(pref.getLong("freehit",0));
        item.add("Legend Card");
        item.add("Yorker Card");
        item.add("Right to Match Card");
        item.add("Free Hit Card");

        itemNameArray = new String[item.size()];
        itemNameArray = item.toArray(itemNameArray);

        OrderHistoryListViewAdapter orderHistoryListViewAdapter = new OrderHistoryListViewAdapter(getApplicationContext(),itemNameArray,val);
        listViewHistory.setAdapter(orderHistoryListViewAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PaymentInfo.this,OngoingPlayer.class));
        finish();
    }


}
