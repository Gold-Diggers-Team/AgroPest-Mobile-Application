package com.example.agropestapplication.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agropestapplication.Model.ModelClass;
import com.example.agropestapplication.R;

import java.util.ArrayList;

public class FAQuestionAdapter extends RecyclerView.Adapter<FAQuestionAdapter.ViewHolder> {

    // Initializing the context and list variables in the FAQuestionAdapter
    Context context;
    ArrayList<ModelClass> list;

    // FAQuestionAdapter constructor to set the context and list
    public FAQuestionAdapter(Context context, ArrayList<ModelClass> list) {
        this.context = context;
        this.list = list;
    }

    // Inflating the view for each item in the RecyclerView
    @NonNull
    @Override
    public FAQuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.question_layout, parent, false);
        return new FAQuestionAdapter.ViewHolder(v);
    }

    // Binding the data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull FAQuestionAdapter.ViewHolder holder, int position) {
        // Getting the ModelClass object for the current position
        ModelClass modelClass = list.get(position);

        // Setting question text to the TextView in the ViewHolder
        holder.question.setText(modelClass.getQuestion());
        Log.d("AnswerDebug", "Answer: " + modelClass.getAnswer());

        // Adding OnClickListener for the answer view button
        holder.answerViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog(modelClass.getAnswer());
            }

            // Method to show a custom dialog with the answer
            private void showCustomDialog(String answer) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Answer");
                Log.d("AnswerDebug", "Answer: " + answer);

                builder.setMessage(answer);
                builder.setIcon(R.drawable.question_answer);

                // Set positive button and its click listener
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing on OK button click
                    }
                });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    // Getting the total number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView question;
        ImageButton answerViewButton;

        // ViewHolder constructor to initialize views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answerViewButton = itemView.findViewById(R.id.openQuestionButton);
        }
    }

}
