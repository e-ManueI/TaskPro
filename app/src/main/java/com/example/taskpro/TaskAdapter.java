package com.example.taskpro;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);
        holder.bind(task);
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
        private TextView contentTextView;
        private TextView timeTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            monthTextView = itemView.findViewById(R.id.monthTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            yearTextView = itemView.findViewById(R.id.yearTextView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
        }

        public void bind(Task task) {
            String month = getMonthString(task.getMonth());
            String day = String.valueOf(task.getDay());
            String year = String.valueOf(task.getYear());

            monthTextView.setText(month);
            dateTextView.setText(day);
            yearTextView.setText(year);
            titleTextView.setText(task.getTitle());
            contentTextView.setText(task.getContent());
            timeTextView.setText(task.getTime());
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