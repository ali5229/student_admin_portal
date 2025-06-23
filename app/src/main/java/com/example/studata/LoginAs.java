package com.example.studata;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginAs extends AppCompatActivity {
    Button student_login,admin_login;
    @Override
    protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.login_as);
         student_login = findViewById(R.id.student_login_btn);
         admin_login = findViewById(R.id.admin_login_btn);
        student_login.setOnClickListener((v)->{
            Intent intent = new Intent(LoginAs.this, StudentLogin.class);
            startActivity(intent);
        });
        admin_login.setOnClickListener((v)->{
            Intent intent = new Intent(LoginAs.this, AdminLogin.class);
            startActivity(intent);
        });

    }
}
