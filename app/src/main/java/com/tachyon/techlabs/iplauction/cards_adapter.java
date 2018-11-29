package com.tachyon.techlabs.iplauction;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class cards_adapter extends BaseAdapter{
    private Context c;
    ArrayList<Power_Cards> cards;
    private String CHANNEL_ID="Abhishek";
    PowerCards pc_object;
    private NotificationManager nm;
    private  NotificationCompat.Builder mBuilder;

     cards_adapter(Context c, ArrayList<Power_Cards> cards) {
        this.c = c;
        this.cards = cards;
           mBuilder = new NotificationCompat.Builder(c, 1234)
                 .setSmallIcon(R.mipmap.ic_launcher)
                 .setContentTitle("Card Purchase")
                 .setContentText("You purschased"+"card")
                 .setPriority(NotificationCompat.PRIORITY_DEFAULT);

         nm=(NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
         Toast.makeText(c, "Entered this function", Toast.LENGTH_SHORT).show();
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
}