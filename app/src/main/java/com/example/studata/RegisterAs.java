package com.example.studata;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class RegisterAs extends AppCompatActivity {
    Button register;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_as);
        register = findViewById(R.id.student_register_btn);
        register.setOnClickListener((v)->{
            Intent intent = new Intent(RegisterAs.this, StudentRegister.class);
            startActivity(intent);
        });
    }
}
