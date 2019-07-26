package com.example.feedonomic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    ArrayList<FeedELement> feedELements;
    Context context;

    public MyAdapter(Context context, ArrayList<FeedELement> feedELements) {
        this.feedELements = feedELements;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_listview, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        final FeedELement current = feedELements.get(position);
        holder.Title.setText(current.getTitle());
        holder.Date.setText(current.getPubDate());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(current.getLink());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedELements.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Description, Date;
        CardView cardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.title_text);
            Date = itemView.findViewById(R.id.date_text);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}
