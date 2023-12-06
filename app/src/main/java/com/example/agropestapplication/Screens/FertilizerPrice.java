package com.example.agropestapplication.Screens;

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
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class FertilizerPrice extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Adapter adapter;
    ArrayList<ModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_price);

        // Initialize RecyclerView and DatabaseReference
        recyclerView = findViewById(R.id.items);
        databaseReference = FirebaseDatabase.getInstance().getReference("form2");

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
        adapter = new Adapter(this, list);
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
                        // Check if name and price are equal
                        if (newItem.getName().equals(existingItem.getName()) && newItem.getPrice() == existingItem.getPrice()) {
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
