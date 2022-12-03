package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class notificationActivity extends AppCompatActivity {
    private Bundle notificationDetails;

    private TextView timeTextView;
    private TextView medicationNameTextView;
    private TextView dosageTextView;
    private TextView extraInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        Bundle bundle = getIntent().getExtras();
        notificationDetails = bundle;

        timeTextView = (TextView) findViewById(R.id.timeTextView);
        medicationNameTextView = (TextView) findViewById(R.id.medicationNameTextView);
        dosageTextView = (TextView) findViewById(R.id.dosageTextView);
        extraInfoTextView = (TextView) findViewById(R.id.extraInfoTextView);

        timeTextView.setText("");
        medicationNameTextView.setText("");
        dosageTextView.setText("");

        String medicationName = bundle.getString("medication");
        String doses = bundle.getString("doses");
        String extras = bundle.getString("extras");
        int hour = bundle.getInt("hour");
        int minute = bundle.getInt("minute");

        String time = String.format("%02d:%02d", hour, minute);
        timeTextView.setText(time);
        medicationNameTextView.setText(medicationName);
        dosageTextView.setText(doses);
        extraInfoTextView.setText(extras);

        getSupportActionBar().setTitle(medicationName);
    }

    public void takeMedicationButton(View view) {
        finish();
    }

    public void sleepButton(View view) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        int id = notificationDetails.getInt("id") + 1;
        notificationDetails.putInt("id", id);
        if (intent.hasExtra("altUser")) {
            String name = notificationDetails.getString("patientName");
            notificationDetails.putString("extras", name +" is overdue to take their medication.");
        } else {
            notificationDetails.putString("extras", "You are overdue to take your medication.");
        }
        intent.putExtras(notificationDetails);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_MUTABLE);
        long time = System.currentTimeMillis() + 300000;
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        finish();
    }
}