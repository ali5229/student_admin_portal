package com.example.studata;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
public class Register_Or_Login extends AppCompatActivity {
    Button login, register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_or_login);
        login = findViewById(R.id.login_btn);
        register = findViewById(R.id.register_btn);
        login.setOnClickListener((v)->{
            Intent intent = new Intent(Register_Or_Login.this, LoginAs.class);
            startActivity(intent);
        });
        register.setOnClickListener((v)->{
            Intent intent = new Intent(Register_Or_Login.this, RegisterAs.class);
            startActivity(intent);
        });
    }
}