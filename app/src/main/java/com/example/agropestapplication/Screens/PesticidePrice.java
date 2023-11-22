package com.example.agropestapplication.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

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
import java.util.Objects;

public class PesticidePrice extends AppCompatActivity {

    private static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 1;

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    Adapter adapter;
    ArrayList<ModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticide_price);

        recyclerView = findViewById(R.id.items);
        databaseReference = FirebaseDatabase.getInstance().getReference("form1");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new Adapter(this, list);
        recyclerView.setAdapter(adapter);

        // Set up Firebase Cloud Messaging
        FirebaseApp.initializeApp(this);

        // Add this line to subscribe to a topic (you can use a topic related to your new items)
        FirebaseMessaging.getInstance().subscribeToTopic("new_items");

        createNotificationChannel();


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

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Channel";
            String description = "Channel Description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}