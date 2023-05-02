package com.example.agropestapplication.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.agropestapplication.MainActivity;
import com.example.agropestapplication.R;
import com.ncorti.slidetoact.SlideToActView;

public class SplashScreenActivity extends AppCompatActivity {


    SlideToActView slideToActView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //Bind elements to activity from xml
        slideToActView = findViewById(R.id.swipeToUnlock);
        slideToActView.setBumpVibration(50);

        //implement slide to unlock button
       slideToActView.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
           @Override
           public void onSlideComplete(@NonNull SlideToActView slideToActView) {
              startActivity(new Intent(getApplicationContext(),LoginActivity.class));
           }
       });

    }

    // Implement device back button.
    // if the user click  the back button, user can see the alert dialog box
    public void onBackPressed(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashScreenActivity.this);
        alertDialog.setTitle("Exit App");
        alertDialog.setMessage("Do you want to exit this app ?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              finishAffinity();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}