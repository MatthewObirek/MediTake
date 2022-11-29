package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Calendar");


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        LinearLayout altUser = (LinearLayout) findViewById(R.id.headerList);
        if (intent.hasExtra("altUser")) {
            if (bundle.getBoolean("altUser") == true) {
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
        finish();

    }

    public void editActivity(View view){

        Intent intent = new Intent(this, editMedActivity.class);
        startActivity(intent);
        finish();

    }


}