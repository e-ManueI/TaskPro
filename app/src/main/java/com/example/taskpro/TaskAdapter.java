package com.example.taskpro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private DatabaseReference tasksRef;

    public TaskAdapter(List<Task> taskList, DatabaseReference tasksRef) {
        this.taskList = taskList;
        this.tasksRef = tasksRef;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        // Get the reversed position to access the tasks in descending order
        int reversedPosition = getItemCount() - 1 - position;

        Task task = taskList.get(reversedPosition);
        holder.bind(task, tasksRef);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView monthTextView;
        private TextView dateTextView;
        private TextView yearTextView;

        private TextView titleTextView;
        private ImageView deleteImageView;
        private TextView contentTextView;
        private TextView timeTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Task task, DatabaseReference tasksRef) {
            String month = getMonthString(task.getMonth());
            String day = String.valueOf(task.getDay());
            String year = String.valueOf(task.getYear());

            monthTextView.setText(month);
            dateTextView.setText(day);
            yearTextView.setText(year);
            titleTextView.setText(task.getTitle());
            contentTextView.setText(task.getContent());
            timeTextView.setText(task.getTime());

            // Bind the data to the views
            deleteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Delete the task from Firebase
                    tasksRef.child(task.getKey()).removeValue();
                }
            });
        }

        private String getMonthString(int month) {
            String[] monthArray = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
            if (month >= 1 && month <= 12) {
                return monthArray[month - 1];
            } else {
                return "";
            }
        }
    }
}