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

public class FAQuestionAdapter extends RecyclerView.Adapter<FAQuestionAdapter.ViewHolder> {

    Context context;
    ArrayList<ModelClass> list;

    public FAQuestionAdapter(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FAQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.question_layout,parent,false);
        return  new FAQuestionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQuestionAdapter.ViewHolder holder, int position) {
        ModelClass modelClass = list.get(position);
        holder.question.setText(modelClass.getQuestion());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends  RecyclerView.ViewHolder{

        TextView question;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
        }
    }
}
