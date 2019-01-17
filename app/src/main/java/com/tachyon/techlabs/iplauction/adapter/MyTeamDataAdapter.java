package com.tachyon.techlabs.iplauction.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tachyon.techlabs.iplauction.MyTeamActivity;
import com.tachyon.techlabs.iplauction.OngoingPlayer;
import com.tachyon.techlabs.iplauction.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MyTeamDataAdapter extends RecyclerView.Adapter<MyTeamDataAdapter.ViewHolder>{

    public Context context;
    public String [] players;
    //public long [] price;
    String user_email;
    int point1,point2,point3;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Long> price;
    View view;
    MyTeamDataAdapter.ViewHolder viewHolder;
    CardView placeholder_player_cardview;
    Resources resources;
    TextView player_name_text;
    MyTeamActivity myTeamActivity = new MyTeamActivity();

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

            player_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int pos = getAdapterPosition();
                  //  myTeamActivity.showAlert(pos,context);
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("Sell Player");
                    builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            user_email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            DocumentReference docRef = db.collection("Players").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("MyTeam").document("1");

// Remove the 'capital' field from the document
                            calculate(price.get(pos));
                            Map<String,Object> updates = new HashMap<>();

                            updates.put(players[pos], FieldValue.delete());
                            docRef.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(context, "Successfully Sold", Toast.LENGTH_SHORT).show();

                                }
                            });



                        }
                    });
                    builder.setNegativeButton(R.string.CANCEL,null);
                    builder.show();



                }
            });
        }
    }

    private void calculate(Long aLong) {


        DocumentReference documentReference = db.collection("Players").document(user_email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {
                    try
                    {
                        //id = documentSnapshot.getString("roomid");
                       long currentAmount = Objects.requireNonNull(documentSnapshot.getLong("Current_Amount")).intValue();
                    }
                    catch(Exception e)
                    {
                      //  Toast.makeText(OngoingPlayer.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });



    /*    if(phase_current==1)
        {
            int diff = point2 - point1;
            switch (diff)
            {
                case 1: loss=(0.9*);


                case  3:

                    loss=(0.2*);
                case 5:
                    loss=(0.3*);
            }
        }
        else
        {
            int diff = point3 - point2;

            switch (diff)
            {


                case 1: profit=(0.1*);


                case  3: profit=(0.2*);


                case 5:profit =(0.3*);

            }


        }
*/



    }

    public MyTeamDataAdapter(Context context, String[] players,List<Long> price, Resources resources)
    {
        this.context = context;
        this.players = players;
        this.price = price;
        this.resources = resources;
    }

    @NonNull
    @Override
    public MyTeamDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_team_data_adapter,parent,false);
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyTeamDataAdapter.ViewHolder holder, int position) {
        holder.player_textview.setText(players[position]);
        holder.price_textview.setText(String.valueOf(price.get(position)));

    }

    @Override
    public int getItemCount() {
        return players.length;
    }

    private  View.OnClickListener onClickListener(final int position){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Sell Player");
                builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {

                    }
                });
                builder.setNegativeButton(R.string.CANCEL,null);
                builder.show();


            }
        };
    }


}
