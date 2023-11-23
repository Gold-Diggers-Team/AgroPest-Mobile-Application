package com.example.agropestapplication.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.agropestapplication.Adapter.AgricultureServiceAdapter;
import com.example.agropestapplication.Adapter.FAQuestionAdapter;
import com.example.agropestapplication.Model.ModelClass;
import com.example.agropestapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FaqActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FAQuestionAdapter adapter;
    ArrayList<ModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        recyclerView = findViewById(R.id.items);
        databaseReference = FirebaseDatabase.getInstance().getReference("FAQ");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        });

        list = new ArrayList<>();
        adapter = new FAQuestionAdapter(this, list);
        recyclerView.setAdapter(adapter);

        // Show a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(FaqActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    progressDialog.dismiss();
                    ModelClass modelClass = dataSnapshot.getValue(ModelClass.class);
                    list.add(modelClass);
                }
                progressDialog.dismiss();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
