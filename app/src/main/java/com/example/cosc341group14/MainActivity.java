package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Calendar");

        //create settings file if it does not exist
        File file = new File(getApplicationContext().getFilesDir(),"settings.txt");
        if(!file.exists()){
            String settings = "false,false,false,0,false,false,false,false";
            String filenameSettings = "settings.txt";
            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(filenameSettings, MODE_PRIVATE);
                outputStream.write(settings.getBytes());
                outputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        //setup the notification channel
        createNotificationChannel();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        LinearLayout altUser = (LinearLayout) findViewById(R.id.headerList);
        if (intent.hasExtra("altUser")) {
            altUser.setVisibility(View.VISIBLE);
            if (bundle.getBoolean("altUser") == true) {
                TextView altUserName = altUser.findViewById(R.id.currentUserTextView);
                altUserName.setText(bundle.getString("patientName"));

                altUser.setVisibility(View.VISIBLE);
            } else {
                altUser.setVisibility(View.GONE);

            }
        } else {
            altUser.setVisibility(View.GONE);
        }
    }

    public void reminderClick (View view) {
        LinearLayout reminderOptions = (LinearLayout) findViewById(R.id.dueOptions);
        if (reminderOptions.isShown() == false) {
            reminderOptions.setVisibility(View.VISIBLE);
        } else {
            reminderOptions.setVisibility(View.GONE);
        }
    }

    public void takeMedicine (View view) {
        LinearLayout due = (LinearLayout) findViewById(R.id.dueList);
        LinearLayout taken = (LinearLayout) findViewById(R.id.takenList);
        LinearLayout reminderOptions = (LinearLayout) findViewById(R.id.dueOptions);
        Button aleve = (Button) findViewById(R.id.aleveButton);

        due.removeView(aleve);
        taken.addView(aleve);

        due.invalidate();
        taken.invalidate();
        reminderOptions.setVisibility(View.GONE);
        aleve.setTextColor(Color.parseColor("#FFFFFF"));
    }

    public void mainActivity(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    public void profileActivity(View view){

        Intent intent = new Intent(this, profileActivity.class);
        startActivity(intent);
        finish();

    }

    public void settingsActivity(View view){

        Intent intent = new Intent(this, settingsActivity.class);
        startActivity(intent);
        finish();
    }


    public void addActivity(View view){

        Intent intent = new Intent(this, addMedActivity.class);
        startActivity(intent);

    }

    public void editActivity(View view){

        Intent intent = new Intent(this, medicationsActivity.class);
        startActivity(intent);

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "meds";
            String description = "Medication Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}