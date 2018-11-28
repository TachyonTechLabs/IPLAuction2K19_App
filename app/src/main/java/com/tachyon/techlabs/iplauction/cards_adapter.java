package com.tachyon.techlabs.iplauction;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class cards_adapter extends BaseAdapter{
    Context c;
    ArrayList<Power_Cards> cards;

    public cards_adapter(Context c, ArrayList<Power_Cards> cards) {
        this.c = c;
        this.cards = cards;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return cards.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.powercard_item,viewGroup,false);
        }

        final Power_Cards s= (Power_Cards) this.getItem(i);

        ImageView img= (ImageView) view.findViewById(R.id.cardimg);
        TextView nameTxt= (TextView) view.findViewById(R.id.name);
        TextView propTxt= (TextView) view.findViewById(R.id.descript);
        TextView price= (TextView) view.findViewById(R.id.price);

        //BIND
        nameTxt.setText(s.getName());
        propTxt.setText(s.getDescription());
        img.setImageResource(s.getImage());
       String pricee= (s.getPrice()+"");
        price.setText(pricee);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}