package com.example.agropestapplication.Adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.agropestapplication.Model.ModelClass;
import com.example.agropestapplication.R;

import java.util.ArrayList;

public class AgricultureServiceAdapter extends RecyclerView.Adapter<AgricultureServiceAdapter.ViewHolder> {

    // Initializing the context and list variables in the AgricultureServiceAdapter
    Context context;
    ArrayList<ModelClass> list;

    // AgricultureServiceAdapter constructor to set the context and list
    public AgricultureServiceAdapter(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
    }

    // Inflating the view for each item in the RecyclerView
    @NonNull
    @Override
    public AgricultureServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.agriculture_service, parent, false);
        return new AgricultureServiceAdapter.ViewHolder(v);
    }

    // Binding the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Getting the ModelClass object for the current position
        ModelClass modelClass = list.get(position);

        // Setting data to the views in the ViewHolder
        holder.depName.setText(modelClass.getDepName());
        // Set the phone number and make it clickable
        holder.contact.setText(modelClass.getTel());

        // Set the link color
        int linkColor = ContextCompat.getColor(context, R.color.purple_700); // Replace with your color resource
        holder.contact.setLinkTextColor(linkColor);

        Linkify.addLinks(holder.contact, Linkify.PHONE_NUMBERS);
        holder.location.setText(modelClass.getLocation());
    }

    // Getting the total number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView depName, contact, location;

        // ViewHolder constructor to initialize views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            depName = itemView.findViewById(R.id.department);
            contact = itemView.findViewById(R.id.contactNumber);
            location = itemView.findViewById(R.id.location);
        }
    }
}