package com.example.agropestapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agropestapplication.Model.ModelClass;
import com.example.agropestapplication.R;

import java.util.ArrayList;

public class AgricultureServiceAdapter extends RecyclerView.Adapter<AgricultureServiceAdapter.ViewHolder> {

    Context context;
    ArrayList<ModelClass> list;

    public AgricultureServiceAdapter(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AgricultureServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.agriculture_service,parent,false);
        return  new AgricultureServiceAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelClass modelClass = list.get(position);
        holder.depName.setText(modelClass.getDepName());
        holder.contact.setText(modelClass.getTel());
        holder.location.setText(modelClass.getLocation());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView depName,contact,location;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           depName =itemView.findViewById(R.id.department);
           contact =itemView.findViewById(R.id.contactNumber);
           location = itemView.findViewById(R.id.location);
        }
    }
}
