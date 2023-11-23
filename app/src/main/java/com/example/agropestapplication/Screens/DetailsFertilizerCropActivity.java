package com.example.agropestapplication.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.agropestapplication.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.agropestapplication.Adapter.Adapter;
import com.example.agropestapplication.Adapter.AgricultureServiceAdapter;
import com.example.agropestapplication.Adapter.DetalisAdapter;
import com.example.agropestapplication.Model.ModelClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DetailsFertilizerCropActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    DetalisAdapter adapter;
    ArrayList<ModelClass> list;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_crop);

        recyclerView = findViewById(R.id.items);
        databaseReference = FirebaseDatabase.getInstance().getReference("form2");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FertilizerActivity.class));
            }
        });

        list = new ArrayList<>();
        adapter = new DetalisAdapter(this, list);
        recyclerView.setAdapter(adapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<ModelClass> newList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelClass modelClass = dataSnapshot.getValue(ModelClass.class);
                    newList.add(modelClass);
                }

                for (ModelClass newItem : newList) {
                    boolean isNewItem = true;
                    for (ModelClass existingItem : list) {
                        if (newItem.getName().equals(existingItem.getName()) && Objects.equals(newItem.getPrice(), existingItem.getPrice())) {
                            isNewItem = false;
                            break;
                        }
                    }

                    if (isNewItem) {
                        list.add(newItem);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });

    }
}