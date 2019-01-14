package com.tachyon.techlabs.iplauction.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tachyon.techlabs.iplauction.R;

public class MyTeamDataAdapter extends RecyclerView.Adapter<MyTeamDataAdapter.ViewHolder>{

    public Context context;
    public int [] players;
    public long [] price;
    View view;
    OpponentsDataAdapter.ViewHolder viewHolder;
    CardView placeholder_player_cardview;
    Resources resources;
    TextView player_name_text;

    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView player_textview;
        public TextView price_textview;
        CardView player_cardview;
        public ViewHolder(View view)
        {
            super(view);

            player_textview = (TextView) view.findViewById(R.id.player_name_mtda);
            price_textview = (TextView) view.findViewById(R.id.price_mtda);
            player_cardview = (CardView) view.findViewById(R.id.custom_myteam_mtda);
            placeholder_player_cardview = player_cardview;
        }
    }

    @NonNull
    @Override
    public MyTeamDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamDataAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
