package com.tachyon.techlabs.iplauction;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class opponents_team_playerslist_adapter extends ArrayAdapter {

    private String [] playername;
   // String point="33";
    private int [] playervalue,points;
    private TextView txtplayer,txtValue,point;
    private String valueText="";

    public opponents_team_playerslist_adapter(@NonNull Context context, String [] text ,int [] value,int[] points) {
        super(context, R.layout.custom_listview_profile,R.id.listtext,text);
        this.playername = text;
        this.playervalue = value;
        this.points=points;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.custom_listview_opponents_players,parent,false);
        txtplayer = row.findViewById(R.id.listtext);
        txtValue = row.findViewById(R.id.listvalue);
        point=row.findViewById(R.id.circulartext);
        txtplayer.setText(playername[position]);
        txtValue.setText(playervalue[position]+"");
        point.setText(points[position]+"");

       /* if(position<2)
        {
            valueText = "₹ "+listvalue[position];
        }
        else
        {
            valueText = listvalue[position]+"";
        }

        txtValue.setText(valueText);*/

        return row;
    }
}
