package com.example.agropestapplication.Screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

        slideToActView = findViewById(R.id.swipeToUnlock);

       slideToActView.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
           @Override
           public void onSlideComplete(@NonNull SlideToActView slideToActView) {
              startActivity(new Intent(getApplicationContext(),LoginActivity.class));
           }
       });

    }
}