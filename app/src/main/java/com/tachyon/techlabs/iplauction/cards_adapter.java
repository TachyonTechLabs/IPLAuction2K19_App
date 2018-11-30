package com.tachyon.techlabs.iplauction;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class cards_adapter extends RecyclerView.Adapter<cards_adapter.ViewHolder>{

    private Context c;
    private int[] carsimgs;
    private String[] names;
    private String[] disc;
    private String[] price;
    View view;
    ViewHolder viewHolder;

    /*
    ArrayList<Power_Cards> cards;
    private String CHANNEL_ID="Abhishek";
    PowerCards pc_object;
    private NotificationManager nm;
    private  NotificationCompat.Builder mBuilder;
    */


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageViewCard;
        public TextView textnamesCard;
        public TextView textdiscCard;
        public TextView textpriceCard;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.cardviewid);
            imageViewCard = (ImageView) itemView.findViewById(R.id.cardimg);
            textnamesCard = (TextView) itemView.findViewById(R.id.cardname);
            textdiscCard = (TextView) itemView.findViewById(R.id.carddescript);
            textpriceCard = (TextView) itemView.findViewById(R.id.cardprice);

        }
    }

    public cards_adapter(Context context,int[]img, String[] disc, String[] name,String [] price)
    {
        this.c = context;
        this.carsimgs = img;
        this.disc = disc;
        this.names = name;
        this.price = price;
    }

    /*

     cards_adapter(Context c, ArrayList<Power_Cards> cards) {
        this.c = c;
        this.cards = cards;
           mBuilder = new NotificationCompat.Builder(c, 1234+"")
                 .setSmallIcon(R.mipmap.ic_launcher)
                 .setContentTitle("Card Purchase")
                 .setContentText("You purschased"+"card")
                 .setPriority(NotificationCompat.PRIORITY_DEFAULT);

         nm=(NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
         Toast.makeText(c, "Entered this function", Toast.LENGTH_SHORT).show();
     }

     */


/*
    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int i) {
        return cards.get(i);
    }

    */

    @NonNull
    @Override
    public cards_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.powercard_item,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull cards_adapter.ViewHolder holder, int position) {
        holder.imageViewCard.setImageResource(carsimgs[position]);
        holder.textdiscCard.setText(disc[position]);
        holder.textnamesCard.setText(names[position]);
        holder.textpriceCard.setText(price[position]);
    }

    /*
    @Override
    public long getItemId(int i) {
        return i;
    }
    */

    @Override
    public int getItemCount() {
        return disc.length;
    }


    /*

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
        ImageButton buy=(ImageButton) view.findViewById(R.id.buy_button);

        //BIND
        nameTxt.setText(s.getName());
        propTxt.setText(s.getDescription());
        img.setImageResource(s.getImage());
       String pricee= (s.getPrice()+"");
        price.setText(pricee);




        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


// notificationId is a unique int for each notification that you must define
               // pc_object.notificationManager.notify(1234, pc_object.mBuilder.build());

                nm.notify(1234, mBuilder.build());
                Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();

                            }
        });



        return view;
    }

    */
}