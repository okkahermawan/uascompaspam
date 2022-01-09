package com.example.uascompas;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    Context context;
    ArrayList id, time, catatan;

    CustomAdapter(Context context,
                  ArrayList id,
                  ArrayList time,
                  ArrayList catatan) {
        this.context = context;
        this.id = id;
        this.time = time;
        this.catatan = catatan;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.id.setText(String.valueOf(id.get(position)));
        holder.time.setText(String.valueOf(time.get(position)));
        holder.catatan.setText(String.valueOf(catatan.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id,time, catatan;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id= itemView.findViewById(R.id.id);
            time = itemView.findViewById(R.id.time);
            catatan = itemView.findViewById(R.id.catatan2);
        }
    }
}
