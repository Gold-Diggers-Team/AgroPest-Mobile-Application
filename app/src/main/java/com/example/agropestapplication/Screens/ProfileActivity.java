package com.example.agropestapplication.Screens;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.agropestapplication.Model.User;
import com.example.agropestapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final int UPDATE_PROFILE_REQUEST = 1;
    CircleImageView profileImage;
    TextView profileName, profileEmail, profilePhoneNumber;
    ImageButton backButtonFromProfile;
    Button editButtonProfile;
    private FirebaseAuth mAuth;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == UPDATE_PROFILE_REQUEST && resultCode == RESULT_OK) {
            updateDashboardUI();
        }
    }

    //Update dashboard Ui user details when user update his details
    private void updateDashboardUI() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            profileName.setText(user.getUsername());
                            profileEmail.setText(user.getEmail());
                            profilePhoneNumber.setText(user.getPhoneNumber());
                            Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profileImage);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("DatabaseError", "Error reading user details", databaseError.toException());
                }
            });
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileImage = findViewById(R.id.userImageProfile);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber);
        editButtonProfile = findViewById(R.id.editButtonProfile);
        backButtonFromProfile = findViewById(R.id.backButtonFromProfile);

        mAuth = FirebaseAuth.getInstance();

        backButtonFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });

        editButtonProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));
                finish();
            }
        });

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // Retrieve user details from Firebase Realtime Database
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // User details found, update the UI
                                User user = dataSnapshot.getValue(User.class);
                                if (user != null) {
                                    Log.d("DashboardActivity", "User details found. Username: " + user.getUsername());

                                    profileName.setText(user.getUsername());
                                    profileEmail.setText(user.getEmail());
                                    profilePhoneNumber.setText(user.getPhoneNumber());

                                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(profileImage);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the error
                            Log.e("DatabaseError", "Error reading user details", databaseError.toException());
                        }
                    });
                } else {
                    // User is signed out, redirect to the login screen
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });




    }
}