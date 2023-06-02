package com.example.taskpro;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddTaskFragment extends Fragment {
    // Declare the necessary views and Firebase variables

    public class Task {
        private String title;
        private String content;

        public Task() {
            // Default constructor required for Firebase
        }

        public Task(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the views
        TextInputEditText titleEditText = view.findViewById(R.id.titleEditText);
        TextInputEditText contentEditText = view.findViewById(R.id.contentEditText);
        Button saveButton = view.findViewById(R.id.saveButton);

        // Set click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                String content = contentEditText.getText().toString().trim();

                // Validate the input
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
                    // Show error message if title or content is empty
                    Toast.makeText(getActivity(), "Please enter both title and content", Toast.LENGTH_SHORT).show();
                } else {
                    // Save the task to Firebase
                    saveTaskToFirebase(title, content);
                }
            }
        });
    }

    private void saveTaskToFirebase(String title, String content) {
        // Get the current user ID (assuming you have implemented Firebase Authentication)
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            // User is not authenticated, handle accordingly
            Toast.makeText(getActivity(), "Not logged in!", Toast.LENGTH_SHORT).show();
            // Redirect to login page
            startActivity(new Intent(getActivity(), LoginActivity.class));
            return;
        }
        String userId = currentUser.getUid();

        // Create a reference to the user's tasks node
        DatabaseReference userTasksRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId)
                .child("tasks");

        // Generate a unique key for the task
        String taskId = userTasksRef.push().getKey();

        // Create a Task object
        Task task = new Task(title, content);

        // Save the task to the user's tasks node
        userTasksRef.child(taskId).setValue(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Task saved successfully
                        Toast.makeText(getActivity(), "Task saved successfully", Toast.LENGTH_SHORT).show();
                        navigateToHomeFragment();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error occurred while saving the task
                        Toast.makeText(getActivity(), "Failed to save task: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void navigateToHomeFragment() {
        // Implement the navigation logic to go back to HomeFragment
        // Example: use a FragmentManager to replace the current fragment with HomeFragment

        // Create an instance of the HomeFragment
        HomeFragment homeFragment = new HomeFragment();

        // Get the FragmentManager
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();

        // Replace the current fragment with HomeFragment
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();

    }
}
