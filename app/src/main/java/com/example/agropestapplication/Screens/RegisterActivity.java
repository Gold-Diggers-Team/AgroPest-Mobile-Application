package com.example.agropestapplication.Screens;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agropestapplication.Model.User;
import com.example.agropestapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    EditText username, email, password, phoneNumber, confirmPassword;
    Button btnReg;
    ImageButton selectImage;
    Uri filePath;
    Bitmap bitmap;
    CircleImageView userImage;
    FirebaseAuth mAuth;
    FirebaseStorage storage;
    StorageReference storageReference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.passwordConfirmReg);
        phoneNumber = findViewById(R.id.phoneNumberReg);
        username = findViewById(R.id.usernameReg);
        btnReg = findViewById(R.id.btnReg);
        selectImage = findViewById(R.id.selectImage);
        userImage = findViewById(R.id.userImage);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void openFileChooser() {
        // Start image picker
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.Select_a_Picture)), PICK_IMAGE_REQUEST);
    }

        @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser() {
        final String emailText = email.getText().toString().trim();
        final String passwordText = password.getText().toString().trim();
        final String confirmPasswordText = confirmPassword.getText().toString().trim();
        final String phoneNumberText = phoneNumber.getText().toString().trim();
        final String usernameText = username.getText().toString().trim();

        if (TextUtils.isEmpty(usernameText)) {
            username.setError( getString(R.string.username_is_required));
            return;
        }
        if (TextUtils.isEmpty(emailText)) {
            email.setError( getString(R.string.Email_is_required));
            return;
        }

        if (!isValidEmail(emailText)) {
            email.setError(getString(R.string.Enter_a_valid_email));
            return;
        }
        if (TextUtils.isEmpty(phoneNumberText)) {
            phoneNumber.setError( getString(R.string.phone_number_is_required));
            return;
        }
        if (TextUtils.isEmpty(passwordText)) {
            password.setError( getString(R.string.Password_is_required));
            return;
        }
        if (passwordText.length() <= 6) {
            password.setError( getString(R.string.password_validation));
            return;
        }

        if (!confirmPasswordText.matches(passwordText)) {
            confirmPassword.setError( getString(R.string.password_match));
            return;
        }


        if (filePath == null) {
            Toast.makeText(this,  getString(R.string.Please_select_an_image), Toast.LENGTH_SHORT).show();
            return;
        }

        // Show a progress dialog
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage( getString(R.string.registering));
        progressDialog.show();

        // Check if the user already exists
        mAuth.fetchSignInMethodsForEmail(emailText).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    if (result != null && result.getSignInMethods() != null && result.getSignInMethods().size() > 0) {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        // User does not exist, proceed with registration
                        mAuth.createUserWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss(); // Dismiss the progress dialog
                                if (task.isSuccessful()) {
                                    uploadImage(emailText, usernameText, phoneNumberText, passwordText);
                                } else {
                                    Toast.makeText(RegisterActivity.this,  getString(R.string.register_warning), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    // Error occurred while checking for user existence
                    progressDialog.dismiss(); // Dismiss the progress dialog
                }
            }




        });
    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private void uploadImage(final String emailText, final String usernameText, final String phoneNumberText, final String passwordText) {
        if (filePath != null) {
            StorageReference ref = storageReference.child("images/" + emailText + ".jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = ref.putBytes(data);
            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Image upload successful, now save user details to the database
                        FirebaseUser user = mAuth.getCurrentUser();

                        // Get the download URL
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadUrl) {
                                // Set imageUrl in the userProfile
                                User userProfile = new User(usernameText, emailText, phoneNumberText, downloadUrl.toString(), passwordText);

                                // Save user details to the database
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                                usersRef.child(user.getUid()).setValue(userProfile);

                                // Show a success message
                                Toast.makeText(RegisterActivity.this, getString(R.string.registration_successful), Toast.LENGTH_SHORT).show();

                                // Redirect to login activity
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        // Image upload failed
                        Toast.makeText(RegisterActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}