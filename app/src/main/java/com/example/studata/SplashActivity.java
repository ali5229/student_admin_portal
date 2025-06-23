package com.example.studata;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.NoCopySpan;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        int Splash_display_timer = 3000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mainintent = new Intent(SplashActivity.this, Register_Or_Login.class);
                SplashActivity.this.startActivity(mainintent);
                SplashActivity.this.finish();
            }
        }, Splash_display_timer);
    }
}
