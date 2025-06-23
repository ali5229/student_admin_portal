package com.example.studata;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboard extends AppCompatActivity {
    private static final String TAG = "AdminDashboard";

    EditText searchField;
    ListView studentListView;
    StudentListAdapter adapter;

    private FirebaseFirestore db;
    private List<Student> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_dashboard);

        db = FirebaseFirestore.getInstance();

        searchField = findViewById(R.id.search_field);
        studentListView = findViewById(R.id.student_list_view);
        studentList = new ArrayList<>();
        adapter = new StudentListAdapter(this, studentList);
        studentListView.setAdapter(adapter);

        fetchAllStudents();

        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchStudentsByRoll(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void fetchAllStudents() {
        db.collection("students")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        studentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Student student = document.toObject(Student.class);
                            studentList.add(student);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        Toast.makeText(AdminDashboard.this, "Error getting student data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void searchStudentsByRoll(String roll) {
        db.collection("students")
                .whereEqualTo("roll", roll)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        studentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Student student = document.toObject(Student.class);
                            studentList.add(student);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.w(TAG, "Error getting documents.", task.getException());
                        Toast.makeText(AdminDashboard.this, "Error searching student data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
