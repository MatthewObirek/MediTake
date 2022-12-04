package com.example.cosc341group14;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent fullScreenIntent = new Intent();
        fullScreenIntent.setClassName("com.example.cosc341group14", "com.example.cosc341group14.notificationActivity");
        fullScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = intent.getExtras();
        int id = bundle.getInt("id");
        String patientName = bundle.getString("patientName");
        String medName = bundle.getString("medication");
        fullScreenIntent.putExtras(bundle);

        String contentText = "Time to take your dose of " + medName + ".";
        if (!patientName.equals("")) {
            contentText = "Time for " + patientName + " to take their " + medName + ".";
        }

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, id,
                fullScreenIntent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                .setSmallIcon(R.drawable.icons8_automatic_80)
                .setContentTitle("Medication Reminder")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setFullScreenIntent(fullScreenPendingIntent, true);

        NotificationManagerCompat nmc = NotificationManagerCompat.from(context);
        nmc.notify(NOTIFICATION_ID, builder.build());
    }
}
