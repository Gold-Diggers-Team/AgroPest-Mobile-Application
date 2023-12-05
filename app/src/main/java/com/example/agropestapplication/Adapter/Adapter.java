package com.example.agropestapplication.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agropestapplication.Model.ModelClass;
import com.example.agropestapplication.R;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    ArrayList<ModelClass> list;

    public Adapter(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.peseticdes_fertilizer,parent,false);
        return  new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelClass modelClass = list.get(position);
        holder.name.setText(modelClass.getName());
        holder.price.setText(modelClass.getPrice());
        holder.isAvailable.setText(modelClass.getIsAvilable());
        holder.isAvailableFertilizer.setText(modelClass.getIsAvilableFertlizer());
        Glide.with(context).load(list.get(position).getImage()).into(holder.image);

        Log.d("Availability", "getAvailablePesticides: " + modelClass.getIsAvilable());

        // Check the condition (case-insensitive)
        if ("Available".equalsIgnoreCase(modelClass.getIsAvilable())) {
            // Set a custom Drawable when the condition is true
            holder.cardView.setBackgroundResource(R.color.is_available);
        } else if ("Out-of Stock".equalsIgnoreCase(modelClass.getIsAvilable())) {
            holder.cardView.setBackgroundResource(R.color.is_not_available);

        }

        // Check the condition (case-insensitive)
        if ("Available".equalsIgnoreCase(modelClass.getIsAvilableFertlizer())) {
            // Set a custom Drawable when the condition is true
            holder.cardView.setBackgroundResource(R.color.is_available);
        }else if("Out-of Stock".equalsIgnoreCase(modelClass.getIsAvilableFertlizer())){
            holder.cardView.setBackgroundResource(R.color.is_not_available);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView name,price, isAvailable, isAvailableFertilizer;
        CardView cardView;
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image =itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            isAvailable = itemView.findViewById(R.id.isAvailable);
            isAvailableFertilizer = itemView.findViewById(R.id.isAvailableFertlizer);
            cardView = itemView.findViewById(R.id.isAvailableBack);
        }
    }
}
