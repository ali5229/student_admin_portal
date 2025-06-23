package com.example.studata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StudentRegister extends AppCompatActivity {
    private static final String TAG = "StudentRegister";

    Button register;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    EditText email, password, name, cgpa, roll, semester;
    ImageView img;
    Uri imageUri;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.student_email);
        password = findViewById(R.id.student_password);
        name = findViewById(R.id.student_name);
        cgpa = findViewById(R.id.student_cgpa);
        roll = findViewById(R.id.student_roll);
        semester = findViewById(R.id.student_semester);
        register = findViewById(R.id.register_btn);
        img = findViewById(R.id.empty_image);


        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        img.setImageURI(imageUri);
                    }
                });

        img.setOnClickListener(v -> {
            Intent galleryIntent = new Intent();
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType("image/*");
            activityResultLauncher.launch(galleryIntent);
        });

        register.setOnClickListener(v -> {
            showProgressBar();
            registerUser();
        });
    }
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void registerUser() {
        String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userName = name.getText().toString().trim();
        String userCgpa = cgpa.getText().toString().trim();
        String userRoll = roll.getText().toString().trim();
        String userSemester = semester.getText().toString().trim();

        if (imageUri != null && !userEmail.isEmpty() && !userPassword.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userId = mAuth.getCurrentUser().getUid();
                                uploadImageAndSaveUser(userId,userEmail, userName, userCgpa, userRoll, userSemester);
                            } else {
                                hideProgressBar();
                                Toast.makeText(StudentRegister.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            hideProgressBar();
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImageAndSaveUser(String userid,String email, String name, String cgpa, String roll, String semester) {
        String uniqueID = UUID.randomUUID().toString();
        StorageReference imageRef = storageReference.child("images/" + uniqueID);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            String imageUrl = uri.toString();
                            saveUserToFirestore(userid,email, name, cgpa, roll, semester, imageUrl);
                        }))
                .addOnFailureListener(e -> Log.e(TAG, "Image upload failed: " + e.getMessage()));
    }

    private void saveUserToFirestore(String userid, String email, String name, String cgpa, String roll, String semester, String imageUrl) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        user.put("cgpa", cgpa);
        user.put("roll", roll);
        user.put("semester", semester);
        user.put("imageUrl", imageUrl);

        db.collection("students").document(userid)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    hideProgressBar();
                    Toast.makeText(StudentRegister.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(StudentRegister.this, StudentUpdate.class);
                    intent.putExtra("USER_ID", userid);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Log.e(TAG, "Error adding user to Firestore: " + e.getMessage()));
    }

}
