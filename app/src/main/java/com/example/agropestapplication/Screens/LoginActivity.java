package com.example.agropestapplication.Screens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agropestapplication.BottomSheetFragment;
import com.example.agropestapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    public static final String[] Languages = {"Select Language", "English", "Sinhala"};
    // Initialize UI elements
    Button signUp, logIn, forgetPassword;
    EditText username, password;
    FirebaseAuth mAuth;
    Spinner selectLanguage;

    @Override
    public void onStart() {
        super.onStart();
        // Check if the user is signed in and update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI elements
        signUp = findViewById(R.id.btnSignUp);
        logIn = findViewById(R.id.btnSignIn);
        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);
        forgetPassword = findViewById(R.id.forgetPassword);
        selectLanguage = findViewById(R.id.selectLanguage);

        // Populate language array
        Languages[0] = getString(R.string.select_language);
        Languages[1] = getString(R.string.english);
        Languages[2] = getString(R.string.sinhala);

        // Set up the language spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Languages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectLanguage.setAdapter(adapter);
        selectLanguage.setSelection(0);
        selectLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle language selection
                String selectedLag = parent.getItemAtPosition(position).toString();
                if (selectedLag.equals(getString(R.string.english))) {
                    setLocale(LoginActivity.this, "en");
                    finish();
                    startActivity(getIntent());
                } else if (selectedLag.equals(getString(R.string.sinhala))) {
                    setLocale(LoginActivity.this, "si");
                    finish();
                    startActivity(getIntent());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle when nothing is selected
            }
        });

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Set up click listeners for buttons
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the bottom sheet for password recovery
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate and process login credentials
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString();

                // Validation checks
                if (TextUtils.isEmpty(Username)) {
                    username.setError(getString(R.string.Email_is_required));
                    return;
                }
                if (!isValidEmail(Username)) {
                    username.setError(getString(R.string.Enter_a_valid_email));
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    password.setError(getString(R.string.Password_is_required));
                    return;
                }

                // Show a progress dialog
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage(getString(R.string.login));
                progressDialog.show();

                // Authenticate with Firebase
                mAuth.signInWithEmailAndPassword(Username, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login successful, navigate to DashboardActivity
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), getString(R.string.login_successful),
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    startActivity(intent);
                                } else {
                                    // Login failed, show error message
                                    progressDialog.dismiss();
                                    Log.e("AuthenticationFailed", "Authentication failed: " + task.getException());
                                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            private boolean isValidEmail(CharSequence target) {
                return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
            }
        });
    }

    // Set the locale for the application
    private void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration configuration = new Configuration(resources.getConfiguration());
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    // Handle back button press with an alert dialog
    public void onBackPressed() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(LoginActivity.this);
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