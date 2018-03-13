package com.jenshen.awesomeanimation.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jenshen.awesomeanimation.AwesomeAnimation;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = findViewById(R.id.text);
        new AwesomeAnimation.Builder(text)
                .setX(AwesomeAnimation.CoordinationMode.COORDINATES, 300)
                .setDuration(1000)
                .build()
                .start();
        new AwesomeAnimation.Builder()
                .setY(AwesomeAnimation.CoordinationMode.COORDINATES, 300)
                .setDuration(1000)
                .build()
                .start(text);
    }
}
