package com.example.agropestapplication.Screens;



import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.agropestapplication.R;


public class PesticidesActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticides);

        CardView pesticidesCheck = findViewById(R.id.checkFertilizer);
        CardView detailsPesticides = findViewById(R.id.detailsPesticides);
        CardView aboutPest = findViewById(R.id.aboutPest);
        ImageButton imageButton = findViewById(R.id.imageButton);


        pesticidesCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),PesticidePrice.class));
            }
        });

        detailsPesticides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DetailsPesticidesCropActivity.class));
            }
        });
        aboutPest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutPesticidesActivity.class));
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
            }
        });




    }


}