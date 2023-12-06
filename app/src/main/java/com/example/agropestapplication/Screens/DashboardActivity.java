package com.example.agropestapplication.Screens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.agropestapplication.Model.User;
import com.example.agropestapplication.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    private static final int UPDATE_PROFILE_REQUEST = 1;
    ImageButton drawerButton, updateProfileButton;
    CardView pesticides, fertilizer, profile, agriInfo;
    ImageSlider imageSlider;
    TextView name, username, email;
    CircleImageView userImage, image;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
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
                            name.setText(user.getUsername());
                            username.setText(user.getUsername());
                            Glide.with(getApplicationContext()).load(user.getImageUrl()).into(userImage);
                            Glide.with(getApplicationContext()).load(user.getImageUrl()).into(image);
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

    private void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        fertilizer = findViewById(R.id.fertilizer);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        drawerButton = findViewById(R.id.drawerButton);
        pesticides = findViewById(R.id.pesticides);
        profile = findViewById(R.id.profile);
        imageSlider = findViewById(R.id.imageSlider);
        agriInfo = findViewById(R.id.agriInfo);
        name = findViewById(R.id.textView5);
        userImage = findViewById(R.id.userImage);

        //Access elements of the drawer header section
        View header = navigationView.getHeaderView(0);
        image = header.findViewById(R.id.userImage);
        username = header.findViewById(R.id.name);
        email = header.findViewById(R.id.email);
        updateProfileButton = header.findViewById(R.id.updateProfileButton);

        mAuth = FirebaseAuth.getInstance();
        // Inside mAuth.addAuthStateListener
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

                                    name.setText(user.getUsername());
                                    email.setText(user.getEmail());
                                    username.setText(user.getUsername());

                                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(userImage);
                                    Glide.with(getApplicationContext()).load(user.getImageUrl()).into(image);

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
                    // Don't call finish() here
                }
            }
        });


        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), UpdateProfileActivity.class), UPDATE_PROFILE_REQUEST);
            }
        });


        // Set up the first image slider
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.banner0, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.banner2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image4, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);


        //Implement drawer button to get drawer menu
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });


        // Implement sliding function to get the drawer menu
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        logout();
                        break;
                    case R.id.nav_faq:
                        Intent intent = new Intent(getApplicationContext(), FaqActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.feedback:
                        SendFeedback();
                        break;
                    case R.id.nav_lang:
                        showLanguageSelectionDialog();
                        break;
                    case R.id.nav_profile:
                        Intent intent1 = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent1);
                        break;
                    default:
                        return false;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }

            private void showLanguageSelectionDialog() {
                String[] languageOptions = {"English", "Sinhala"};
                languageOptions[0] = getString(R.string.english);
                languageOptions[1] = getString(R.string.sinhala);
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle(getString(R.string.select_language)) // Replace this with your desired title
                        .setItems(languageOptions, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String selectedLanguage = (which == 0) ? "en" : "si"; // English or Sinhala
                                setLocale(DashboardActivity.this, selectedLanguage);
                                // Reload the activity
                                recreate();
                            }
                        });
                builder.show();

                // Close the drawer after language selection
                closeDrawer();
            }

            private void closeDrawer() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }


            private void SendFeedback() {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto: " + Uri.encode("sathsara.psf99@gmail.com") + "?subject=" +
                        Uri.encode("Feedback about agroPEST");
                Uri uri = Uri.parse(uriText);
                intent.setData(uri);
                startActivity(Intent.createChooser(intent, "Send email"));
            }

            private void logout() {
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }

        });


        //implement pesticides screen open button
        pesticides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PesticidesActivity.class));
            }
        });

        //implement fertilizer screen open button
        fertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), FertilizerActivity.class));
            }
        });


        //Implement profile button
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
            }
        });

        //Implement agriInfo button
        agriInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AgriServiceActivity.class));
            }
        });


    }

    // Implement device back button.
    // if the user click  the back button, user can see the alert dialog box
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle(getString(R.string.exit_app));
        alertDialog.setMessage(getString(R.string.exit_app_warning));
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
