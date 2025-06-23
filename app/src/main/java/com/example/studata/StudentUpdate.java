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
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StudentUpdate extends AppCompatActivity {
    private static final String TAG = "StudentUpdate";

    EditText email, name, cgpa, roll, semester;
    ImageView img;
    Button update;
    Uri imageUri;
    String userId, imageUrl;
    ProgressBar progressBar;

    private FirebaseFirestore db;
    private StorageReference storageReference;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_update);

        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        progressBar = findViewById(R.id.progressBar);
        email = findViewById(R.id.student_email);
        name = findViewById(R.id.student_name);
        cgpa = findViewById(R.id.student_cgpa);
        roll = findViewById(R.id.student_roll);
        semester = findViewById(R.id.student_semester);
        img = findViewById(R.id.empty_image);
        update = findViewById(R.id.update_btn);


        userId = getIntent().getStringExtra("USER_ID");


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

        update.setOnClickListener(v -> {
            showProgressBar();
            updateUserDetails();
        });

        fetchUserData();
    }
    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
    private void fetchUserData() {
        if(userId != null) {
            db.collection("students").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());

                            email.setText(documentSnapshot.getString("email"));
                            name.setText(documentSnapshot.getString("name"));
                            cgpa.setText(documentSnapshot.getString("cgpa"));
                            roll.setText(documentSnapshot.getString("roll"));
                            semester.setText(documentSnapshot.getString("semester"));
                            imageUrl = documentSnapshot.getString("imageUrl");

                            if (imageUrl != null && !imageUrl.isEmpty()) {
                                Glide.with(StudentUpdate.this).load(imageUrl).into(img);
                            }
                        } else{
                            hideProgressBar();
                            Log.d(TAG, "No such document");
                            Toast.makeText(StudentUpdate.this, "No such document", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG, "Error fetching user data: " + e.getMessage()));
        }else {
            hideProgressBar();
            Log.d(TAG, "User ID is null");
        }
    }

    private void updateUserDetails() {
        String userEmail = email.getText().toString().trim();
        String userName = name.getText().toString().trim();
        String userCgpa = cgpa.getText().toString().trim();
        String userRoll = roll.getText().toString().trim();
        String userSemester = semester.getText().toString().trim();

        if (imageUri != null) {

            String uniqueID = UUID.randomUUID().toString();
            StorageReference imageRef = storageReference.child("images/" + uniqueID);
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl()
                            .addOnSuccessListener(uri -> {

                                imageUrl = uri.toString();
                                saveUserData(userEmail, userName, userCgpa, userRoll, userSemester);
                            }))
                    .addOnFailureListener(e -> Log.e(TAG, "Image upload failed: " + e.getMessage()));
        } else {

            saveUserData(userEmail, userName, userCgpa, userRoll, userSemester);
        }
    }

    private void saveUserData(String email, String name, String cgpa, String roll, String semester) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        user.put("cgpa", cgpa);
        user.put("roll", roll);
        user.put("semester", semester);
        user.put("imageUrl", imageUrl);

        db.collection("students").document(userId)
                .set(user, SetOptions.merge())
                .addOnSuccessListener(aVoid -> {
                    hideProgressBar();
                    Toast.makeText(StudentUpdate.this, "User updated successfully", Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> Log.e(TAG, "Error updating user: " + e.getMessage()));
    }
}
