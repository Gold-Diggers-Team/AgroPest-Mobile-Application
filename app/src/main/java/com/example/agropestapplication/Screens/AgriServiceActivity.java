package com.example.agropestapplication.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.agropestapplication.Adapter.Adapter;
import com.example.agropestapplication.Adapter.AgricultureServiceAdapter;
import com.example.agropestapplication.Model.ModelClass;
import com.example.agropestapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class AgriServiceActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    AgricultureServiceAdapter adapter;
    ArrayList<ModelClass> list;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agri_service);

        // Initialize RecyclerView and DatabaseReference
        recyclerView = findViewById(R.id.items);
        databaseReference = FirebaseDatabase.getInstance().getReference("form");

// Set RecyclerView properties
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

// Initialize ImageButton for navigation
        ImageButton imageButton = findViewById(R.id.imageButton);

// Set OnClickListener to navigate to DashboardActivity
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        });

// Initialize list and adapter
        list = new ArrayList<>();
        adapter = new AgricultureServiceAdapter(this, list);
        recyclerView.setAdapter(adapter);

// ValueEventListener to update the list based on changes in the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Create a new list to store updated data
                ArrayList<ModelClass> newList = new ArrayList<>();

                // Iterate through the snapshot data
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // Convert each data snapshot to a ModelClass object
                    ModelClass modelClass = dataSnapshot.getValue(ModelClass.class);
                    // Add the ModelClass object to the new list
                    newList.add(modelClass);
                }

                // Check for new items and update the adapter
                for (ModelClass newItem : newList) {
                    boolean isNewItem = true;

                    // Compare each new item with existing items in the list
                    for (ModelClass existingItem : list) {
                        // Check if department name, location, and telephone number are equal
                        if (newItem.getDepName().equals(existingItem.getDepName()) && newItem.getLocation().equals(existingItem.getLocation()) && newItem.getTel().equals(existingItem.getTel())) {
                            // If equal, mark as not a new item and break the loop
                            isNewItem = false;
                            break;
                        }
                    }

                    // If it's a new item, add it to the existing list and update the adapter
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
