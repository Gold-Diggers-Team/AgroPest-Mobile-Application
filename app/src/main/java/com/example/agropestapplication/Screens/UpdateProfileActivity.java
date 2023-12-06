package com.example.agropestapplication.Screens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agropestapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    EditText updateName, updatePhoneNumber;
    ImageButton updateImage;
    CircleImageView userUpdateImage;
    Button updateProfile;
    FirebaseAuth firebaseAuth;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri imageUri;
    Bitmap bitmap;
    ProgressDialog progressDialog; // Declare ProgressDialog

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        updateName = findViewById(R.id.updateName);
        updatePhoneNumber = findViewById(R.id.updatePhoneNumber);
        updateImage = findViewById(R.id.updateImage);
        updateProfile = findViewById(R.id.updateProfile);
        userUpdateImage = findViewById(R.id.userUpdateImage);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference().child("user_images");

        progressDialog = new ProgressDialog(this); // Initialize ProgressDialog

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    // Handling the result of an activity started for result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the image selection request
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the selected image URI
            imageUri = data.getData();
            try {
                // Convert the URI to a bitmap and set it to the ImageView
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                userUpdateImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void updateProfile() {
        String updateNameText = updateName.getText().toString().trim();
        String updatePhoneNumberText = updatePhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(updateNameText)) {
            updateName.setError(getString(R.string.username_is_required));
            return;
        }
        if (TextUtils.isEmpty(updatePhoneNumberText)) {
            updatePhoneNumber.setError(getString(R.string.phone_number_is_required));
            return;
        }

        if (currentUser != null) {
            progressDialog.setMessage(getString(R.string.update_profile));
            progressDialog.show();

            String userId = currentUser.getUid();
            Map<String, Object> updates = new HashMap<>();
            updates.put("username", updateNameText);
            updates.put("phoneNumber", updatePhoneNumberText);

            if (imageUri != null) {
                // Upload the new image to Firebase Storage
                uploadImage(userId, updates);
            } else {
                // Update the user details without uploading a new image
                updateDetails(userId, updates);
            }
        } else {
            // Handle the case where the user is not logged in
        }
    }

    private void uploadImage(final String userId, final Map<String, Object> updates) {
        StorageReference imageRef = storageReference.child(userId + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Image upload success
                    // Get the updated image URL
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Update the image URL in the Realtime Database
                        updates.put("imageUrl", uri.toString());

                        // Update user details in the Realtime Database
                        updateDetails(userId, updates);
                    });
                })
                .addOnFailureListener(e -> {
                    // Handle image upload failure
                    progressDialog.dismiss(); // Dismiss ProgressDialog on failure
                    Toast.makeText(UpdateProfileActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateDetails(String userId, Map<String, Object> updates) {
        // Update user details in the Realtime Database
        databaseReference.child(userId).updateChildren(updates)
                .addOnSuccessListener(aVoid -> {
                    // User details updated successfully
                    progressDialog.dismiss(); // Dismiss ProgressDialog on success
                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.update_successful), Toast.LENGTH_SHORT).show();

                    // Notify the DashboardActivity that the profile is updated
                    setResult(Activity.RESULT_OK);
                    finish();
                })
                .addOnFailureListener(e -> {
                    // Handle failure to update user details
                    progressDialog.dismiss(); // Dismiss ProgressDialog on failure
                    Toast.makeText(UpdateProfileActivity.this, getString(R.string.update_failed), Toast.LENGTH_SHORT).show();
                });
    }
}