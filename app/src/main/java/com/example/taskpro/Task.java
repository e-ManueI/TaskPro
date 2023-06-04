package com.example.taskpro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Task {
    private String key; // Firebase key associated with the task
    private String date;
    private String title;
    private String content;
    private String time;

    public Task(String key, String date, String title, String content, String time) {
        this.key = key;
        this.date = date;
        this.title = title;
        this.content = content;
        this.time = time;
    }

    // Add the no-argument constructor
    public Task() {
        // Required empty constructor for Firebase
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDate() {
        return date;
    }

    public int getDay() {
        return getParsedDate().get(Calendar.DAY_OF_MONTH);
    }

    public int getMonth() {
        return getParsedDate().get(Calendar.MONTH) + 1;
    }

    public int getYear() {
        return getParsedDate().get(Calendar.YEAR);
    }

    public Calendar getParsedDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            if (date != null) {
                calendar.setTime(sdf.parse(date));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }
}
