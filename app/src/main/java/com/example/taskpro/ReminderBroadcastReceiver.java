package com.example.taskpro;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class ReminderBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the task details from the intent
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");

        // Create a notification channel (required for Android Oreo and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    context.getString(R.string.default_notification_channel_id),
                    context.getString(R.string.default_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, context.getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.task_time)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
                .setVibrate(new long[]{0, 100, 200, 300});

        // Check if the app has the required permission before showing the notification
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_NOTIFICATION_POLICY) != PackageManager.PERMISSION_GRANTED) {
            // Handle the case where the permission is not granted
            // You can request the permission from the user or perform any other appropriate action
            return;
        }

        // Show the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        try {
            notificationManager.notify(0, builder.build());
        } catch (SecurityException e) {
            // Handle the SecurityException
            // This exception can occur if the app doesn't have the required permission
            e.printStackTrace();
        }
    }
}
