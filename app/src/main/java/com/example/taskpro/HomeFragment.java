package com.example.taskpro;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton addTaskButton;
    private DatabaseReference tasksRef;
    private ValueEventListener tasksValueEventListener;
    private List<Task> taskList;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String userId = firebaseAuth.getCurrentUser().getUid();

        // Create a reference to the tasks node in the Firebase database
        tasksRef = FirebaseDatabase.getInstance().getReference("users")
                .child(userId) // Replace with your user ID or appropriate path
                .child("tasks");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        addTaskButton = view.findViewById(R.id.addTaskButton);
        addTaskButton.setOnClickListener(this);

        // Initialize the RecyclerView and its adapter
        recyclerView = view.findViewById(R.id.recyclerView);
        taskList = new ArrayList<>();
        // Pass the tasksRef to the TaskAdapter
        taskAdapter = new TaskAdapter(taskList, tasksRef, getContext());

        // Set the adapter for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(taskAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // Initialize the ValueEventListener to listen for changes in the tasks node
        tasksValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskList.clear();
                for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
                    Task task = taskSnapshot.getValue(Task.class);
                    task.setKey(taskSnapshot.getKey()); // Set the key for each task
                    taskList.add(task);
                }
                taskAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        };

        // Start listening for changes in the tasks node
        tasksRef.addValueEventListener(tasksValueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Stop listening for changes in the tasks node
        if (tasksValueEventListener != null) {
            tasksRef.removeEventListener(tasksValueEventListener);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addTaskButton) {
            // Open the AddTaskFragment
            AddTaskFragment addTaskFragment = new AddTaskFragment();
            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, addTaskFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}