package com.example.agropestapplication.Screens;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import com.example.agropestapplication.R;

public class PesticidesActivity extends AppCompatActivity {
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pesticides);

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[] {Color.parseColor("#1FA0FF"), Color.parseColor("#12DAFB"), Color.parseColor("#A7FDCC")}
        );
        View myView = findViewById(R.id.my_view);
        View myView2 = findViewById(R.id.my_view2);
        View myView3 = findViewById(R.id.my_view3);
        View myView4 = findViewById(R.id.my_view4);


        myView.setBackground(gradientDrawable);
        myView2.setBackground(gradientDrawable);
        myView3.setBackground(gradientDrawable);
        myView4.setBackground(gradientDrawable);

    }
}