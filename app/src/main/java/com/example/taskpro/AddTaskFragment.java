package com.example.taskpro;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

public class AddTaskFragment extends Fragment {
    // Declare the necessary views and Firebase variables
    public class Task {
        private String title;
        private String content;
        private String date;
        private String time;

        public Task() {
            // Default constructor required for Firebase
        }
        public Task(String title, String date, String time,String content) {
            this.title = title;
            this.date = date;
            this.time = time;
            this.content = content;
        }
        public String getTitle() {
            return title;
        }
        public String getContent() {
            return content;
        }
        public String getDate() {
            return date;
        }
        public String getTime() {
            return time;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_task, container, false);
    }

    // Declare variables to store selected date and time
    private int year, month, day, hour, minute;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Find the views
        TextInputEditText titleEditText = view.findViewById(R.id.titleEditText);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        ImageView calendarImageView = view.findViewById(R.id.calendarImageView);
        TextView timeTextView = view.findViewById(R.id.timeTextView);
        ImageView clockImageView = view.findViewById(R.id.clockImageView);
        TextInputEditText contentEditText = view.findViewById(R.id.contentEditText);
        Button saveButton = view.findViewById(R.id.saveButton);

        // Set click listener for the calendar image view
        calendarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                final Calendar calendar = Calendar.getInstance();
                int currentYear = calendar.get(Calendar.YEAR);
                int currentMonth = calendar.get(Calendar.MONTH);
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog to allow the user to select a date
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Create a Calendar object with the selected date
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);

                        // Get the current date
                        Calendar currentDate = Calendar.getInstance();

                        // Compare the selected date with the current date
                        if (selectedDate.before(currentDate)) {
                            // Selected date is in the past, show an error message
                            Toast.makeText(getActivity(), "Please select a date in the future", Toast.LENGTH_SHORT).show();
                        } else {
                            // Store the selected date in variables
                            AddTaskFragment.this.year = year;
                            month = monthOfYear;
                            day = dayOfMonth;

                            // Display the selected date
                            String selectedDateStr = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                            dateTextView.setText(selectedDateStr);
                        }
                    }
                }, currentYear, currentMonth, currentDay);

                // Show the DatePickerDialog
                datePickerDialog.show();
            }
        });

        // Set click listener for the clock image view
        clockImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current time
                final Calendar currentTime = Calendar.getInstance();

                // Create a TimePickerDialog to allow the user to select a time
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Create a calendar object with the selected time
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selectedTime.set(Calendar.MINUTE, minute);

                        // Compare the selected time with the current time
                        if (selectedTime.after(currentTime)) {
                            // The selected time is in the future
                            // Store the selected time in variables
                            hour = hourOfDay;
                            AddTaskFragment.this.minute = minute;

                            // Display the selected time
                            String selectedTimeStr = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                            timeTextView.setText(selectedTimeStr);
                        } else {
                            // The selected time is in the past or too close to the current time
                            Toast.makeText(getActivity(), "Please select a time in the future", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);

                // Show the TimePickerDialog
                timePickerDialog.show();
            }
        });



        // Set click listener for the save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                String date = dateTextView.getText().toString().trim();
                String time = timeTextView.getText().toString().trim();
                String content = contentEditText.getText().toString().trim();

                // Validate the input
                if (TextUtils.isEmpty(title) || TextUtils.isEmpty(date) || TextUtils.isEmpty(time)) {
                    // Show error message if title, date, or time is empty
                    Toast.makeText(getActivity(), "Please enter both title, date and time", Toast.LENGTH_SHORT).show();
                } else {
                    // Check if content exceeds the maximum number of lines
                    int lineCount = contentEditText.getLineCount();
                    if (lineCount > 3) {
                        // Show an error message to the user
                        Toast.makeText(getActivity(), "Content should not exceed 3 lines/30 words", Toast.LENGTH_SHORT).show();
                    }else{
                        // Get the current time
                        final Calendar currentTime = Calendar.getInstance();

                        // Get the selected date and time
                        Calendar selectedDateTime = Calendar.getInstance();
                        selectedDateTime.set(year, month, day, hour, minute);

                        // Calculate the time difference between the selected time and current time
                        long timeDifferenceInMillis = selectedDateTime.getTimeInMillis() - currentTime.getTimeInMillis();
                        int timeDifferenceInMinutes = (int) (timeDifferenceInMillis / (60 * 1000));

                        // Check if the selected time is at least six minutes later
                        if (timeDifferenceInMinutes < 6) {
                            // Show an error message to the user
                            Toast.makeText(getActivity(), "Please select a time at least six minutes later", Toast.LENGTH_SHORT).show();
                        } else {
                            // Save the task to Firebase
                            saveTaskToFirebase(title, date, time, content);
                        }
                    }
                }
            }
        });
    }

    private void saveTaskToFirebase(String title, String date, String time, String content) {
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
        Task task = new Task(title, date, time, content);

        // Check for internet connection availability
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Internet connection is available, save the task to Firebase
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
        } else {
            // No internet connection, save the task locally and redirect to the homepage
            userTasksRef.child(taskId).setValue(task);
            Toast.makeText(getActivity(), "Task saved locally. No internet connection.", Toast.LENGTH_SHORT).show();
            navigateToHomeFragment();
        }
        // For the reminder...
        // Create a Calendar object with the selected date and time
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute);
        calendar.set(Calendar.SECOND, 0); //ensure that the notification will trigger at the exact minute, without any seconds delay.

        // Get the current time in milliseconds
        long currentTimeMillis = System.currentTimeMillis();

        // Calculate the time difference between the current time and the selected time
        long timeDifference = calendar.getTimeInMillis() - currentTimeMillis;

        // Create an Intent to trigger the ReminderBroadcastReceiver
        Intent reminderIntent = new Intent(getActivity(), ReminderBroadcastReceiver.class);
        reminderIntent.putExtra("title", title);
        reminderIntent.putExtra("content", content);

        // Create a PendingIntent to wrap the reminderIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, reminderIntent, PendingIntent.FLAG_IMMUTABLE);

        // Get the AlarmManager
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        // Schedule the alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, currentTimeMillis + timeDifference, pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, currentTimeMillis + timeDifference, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, currentTimeMillis + timeDifference, pendingIntent);
        }

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
