package com.example.agropestapplication.Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agropestapplication.Adapter.Adapter;
import com.example.agropestapplication.Model.ModelClass;
import com.example.agropestapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PesticidePrice extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Adapter adapter;
    ArrayList<ModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticide_price);

        // Initialize RecyclerView with the items ID
        recyclerView = findViewById(R.id.items);

// Set up Firebase database reference for "form1"
        databaseReference = FirebaseDatabase.getInstance().getReference("form1");

// Set fixed size and linear layout manager for RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

// Initialize ImageButton for navigation to PesticidesActivity
        ImageButton imageButton = findViewById(R.id.imageButton);

// Set onClickListener for navigating to PesticidesActivity
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PesticidesActivity.class));
            }
        });

// Initialize list and adapter for RecyclerView
        list = new ArrayList<>();
        adapter = new Adapter(this, list);
        recyclerView.setAdapter(adapter);

// Set up ValueEventListener for database changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Create a new list to store data from the database
                ArrayList<ModelClass> newList = new ArrayList<>();

                // Iterate through the database snapshot and populate the new list
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ModelClass modelClass = dataSnapshot.getValue(ModelClass.class);
                    newList.add(modelClass);
                }

                // Compare new items with existing items in the list
                for (ModelClass newItem : newList) {
                    boolean isNewItem = true;
                    for (ModelClass existingItem : list) {
                        // Check if an item with the same name and price already exists
                        if (newItem.getName().equals(existingItem.getName()) && Objects.equals(newItem.getPrice(), existingItem.getPrice())) {
                            isNewItem = false;
                            break;
                        }
                    }

                    // If the item is new, add it to the list and update the adapter
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