package com.example.agropestapplication.Screens;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.agropestapplication.R;

public class FertilizerActivity extends AppCompatActivity {

    @Override
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer);
        CardView checkFertilizer = findViewById(R.id.checkFertilizer);
        CardView detailsFertilizer = findViewById(R.id.detailsFertilizer);
        CardView about = findViewById(R.id.about);

        checkFertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FertilizerPrice.class));
            }
        });
        detailsFertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DetailsFertilizerCropActivity.class));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AboutFertlizerActivity.class));
            }
        });
    }
}