package com.tachyon.techlabs.iplauction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import java.util.ArrayList;

public class PowerCards extends AppCompatActivity {

    cards_adapter adapter;
    GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_power_cards);

        gv= (GridView) findViewById(R.id.gv);

        adapter=new cards_adapter(this,getData());
        gv.setAdapter(adapter);

    }
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

}