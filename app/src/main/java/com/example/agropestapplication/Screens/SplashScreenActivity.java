package com.example.agropestapplication.Screens;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.agropestapplication.R;
import com.ncorti.slidetoact.SlideToActView;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {

    SlideToActView slideToActView;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Check internet connection
        if (isNetworkAvailable()) {
            // Internet connection is available, proceed with your app logic
            setupSlideToUnlock();
        } else {
            // No internet connection, show a warning message
            showNoInternetDialog();
        }

        // Register BroadcastReceiver for network state changes
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, intentFilter);
    }

    private void setupSlideToUnlock() {
        // Bind elements to activity from xml
        slideToActView = findViewById(R.id.swipeToUnlock);
        slideToActView.setBumpVibration(50);

        // Implement slide to unlock button
        slideToActView.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView slideToActView) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("No Internet Connection");
        alertDialogBuilder.setMessage("Please check your network settings.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Close the app or handle as needed
                        finishAffinity();
                    }
                });

        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    // Implement device back button.
    // If the user clicks the back button, the user can see the alert dialog box
    public void onBackPressed() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        } else {
            super.onBackPressed();
        }
    }

    // Check internet connection method
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    // BroadcastReceiver to handle network state changes
    private final BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isNetworkAvailable()) {
                // Internet connection is restored, dismiss the dialog
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    setupSlideToUnlock();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        // Unregister the BroadcastReceiver when the activity is destroyed
        unregisterReceiver(networkStateReceiver);
        super.onDestroy();
    }
}
