package com.example.apekshaapplications;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPref = getSharedPreferences("Register", MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();
    boolean Registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Registered = sharedPref.getBoolean("Registered", false);
/*
        if (!Registered) {

            startActivity(new Intent(SplashScreenActivity.this, RegisterActivity.class));
        } else {

            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));

        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*editor.putBoolean("Registered", true);
                editor.commit();*/
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
            }
        }, 3000);

    }
}