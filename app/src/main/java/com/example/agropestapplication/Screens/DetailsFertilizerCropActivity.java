package com.example.agropestapplication.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.agropestapplication.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageButton;

import com.example.agropestapplication.Adapter.DetailsAdapter;
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
    DetailsAdapter adapter;
    ArrayList<ModelClass> list;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_fertilizer_crop);

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
        adapter = new DetailsAdapter(this, list);
        recyclerView.setAdapter(adapter);

        // Listen for changes in the database reference
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
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

                // Iterate through the new list to check for new items
                for (ModelClass newItem : newList) {
                    boolean isNewItem = true;

                    // Compare each new item with existing items in the list
                    for (ModelClass existingItem : list) {
                        // Check if the name and price are equal
                        if (newItem.getName().equals(existingItem.getName()) && Objects.equals(newItem.getPrice(), existingItem.getPrice())) {
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