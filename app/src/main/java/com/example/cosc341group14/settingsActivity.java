package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class settingsActivity extends AppCompatActivity {
    /*
        Settings by position:
        0: Advanced Options (true/false)
        1: Disable Snooze (true/false)
        2: Disable Alarm Bundling (true/false)
        3: Home Screen (0, 1) (index of spinner position)
        4: Enable Monitoring (true/false)
        5: Enable Monitor Edit (true/false)
        6: Enable Notifications (true/false)
        7: Lock Patient Options (true/false)
    */

    Switch advancedOptions;
    Switch disableSnooze;
    Switch disableAlarmBundling;
    Spinner homeScreen;
    Switch enableMonitoring;
    Switch enableMonitorEdit;
    Switch enableNotifications;
    Switch lockPatientOptions;

    View disableSnoozeLayout;
    View disableAlarmBundlingLayout;
    View lockPatientOptionsLayout;
    View enableMonitorEditLayout;
    View enableNotificationsLayout;

    String settings;
    ArrayList<String> settingsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setTitle("Settings");

        disableSnoozeLayout = findViewById(R.id.disableSnoozeLayout);
        disableAlarmBundlingLayout = findViewById(R.id.disableAlarmBundlingLayout);
        lockPatientOptionsLayout = findViewById(R.id.lockPatientOptionsLayout);
        enableMonitorEditLayout = findViewById(R.id.enableMonitorEditLayout);
        enableNotificationsLayout = findViewById(R.id.enableNotificationsLayout);

        advancedOptions = (Switch) findViewById(R.id.advancedOptionsSwitch);
        disableSnooze = (Switch) findViewById(R.id.disableSnoozeSwitch);
        disableAlarmBundling = (Switch) findViewById(R.id.disableAlarmBundlingSwitch);
        homeScreen = (Spinner) findViewById(R.id.homeScreenSpinner);
        enableMonitoring = (Switch) findViewById(R.id.enableMonitoringSwitch);
        enableMonitorEdit = (Switch) findViewById(R.id.enableMonitorEditSwitch);
        enableNotifications = (Switch) findViewById(R.id.enableNotificationsSwitch);
        lockPatientOptions = (Switch) findViewById(R.id.lockPatientOptionsSwitch);

        homeScreen.setOnItemSelectedListener(
            new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    homeScreenChanged(Integer.toString(pos));
                }
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        String file = "settings.txt";
        //File read operation
        try {
            FileInputStream fis = openFileInput(file);  //A FileInputStream obtains input bytes from a file in a file system
            InputStreamReader isr = new InputStreamReader(fis); //An InputStreamReader is a bridge from byte streams to character streams
            BufferedReader br = new BufferedReader(isr);    //Reads text from a character-input stream,
            settings = br.readLine();
        } catch (FileNotFoundException e){
            e.printStackTrace();
            settings = "false,false,false,0,false,false,false,false";
        } catch (IOException e){
            e.printStackTrace();
            settings = "false,false,false,0,false,false,false,false";
        }
        settingsList = new ArrayList<>(Arrays.asList(settings.split(",")));

        advancedOptions.setChecked(Boolean.parseBoolean(settingsList.get(0)));
        disableSnooze.setChecked(Boolean.parseBoolean(settingsList.get(1)));
        disableAlarmBundling.setChecked(Boolean.parseBoolean(settingsList.get(2)));
        homeScreen.setSelection(Integer.parseInt(settingsList.get(3)));
        enableMonitoring.setChecked(Boolean.parseBoolean(settingsList.get(4)));
        enableMonitorEdit.setChecked(Boolean.parseBoolean(settingsList.get(5)));
        enableNotifications.setChecked(Boolean.parseBoolean(settingsList.get(6)));
        lockPatientOptions.setChecked(Boolean.parseBoolean(settingsList.get(7)));


        if(Boolean.parseBoolean(settingsList.get(0))) {
            disableSnoozeLayout.setVisibility(View.VISIBLE);
            disableAlarmBundlingLayout.setVisibility(View.VISIBLE);
            lockPatientOptionsLayout.setVisibility(View.VISIBLE);
        } else {
            disableSnoozeLayout.setVisibility(View.GONE);
            disableAlarmBundlingLayout.setVisibility(View.GONE);
            lockPatientOptionsLayout.setVisibility(View.GONE);
        }

        if(Boolean.parseBoolean(settingsList.get(4))) {
            enableMonitorEditLayout.setVisibility(View.VISIBLE);
            enableNotificationsLayout.setVisibility(View.VISIBLE);
        } else {
            enableMonitorEditLayout.setVisibility(View.GONE);
            enableNotificationsLayout.setVisibility(View.GONE);
        }
        saveSettings();
    }

    public void advancedOptionsChanged(View view) {
        boolean state = ((Switch)view).isChecked();
        settingsList.set(0, Boolean.toString(state));
        if(state) {
            disableSnoozeLayout.setVisibility(View.VISIBLE);
            disableAlarmBundlingLayout.setVisibility(View.VISIBLE);
            lockPatientOptionsLayout.setVisibility(View.VISIBLE);
        } else {
            disableSnoozeLayout.setVisibility(View.GONE);
            disableAlarmBundlingLayout.setVisibility(View.GONE);
            lockPatientOptionsLayout.setVisibility(View.GONE);
        }
        saveSettings();
    }

    public void disableSnoozeChanged(View view) {
        boolean state = ((Switch)view).isChecked();
        settingsList.set(1, Boolean.toString(state));
        saveSettings();
    }

    public void disableAlarmBundlingChanged(View view) {
        boolean state = ((Switch)view).isChecked();
        settingsList.set(2, Boolean.toString(state));
        saveSettings();
    }

    public void homeScreenChanged(String state) {
        settingsList.set(3, state);
        saveSettings();
    }

    public void enableMonitoringChanged(View view) {
        boolean state = ((Switch)view).isChecked();
        settingsList.set(4, Boolean.toString(state));
        if(state) {
            enableMonitorEditLayout.setVisibility(View.VISIBLE);
            enableNotificationsLayout.setVisibility(View.VISIBLE);
        } else {
            enableMonitorEditLayout.setVisibility(View.GONE);
            enableNotificationsLayout.setVisibility(View.GONE);
        }
        saveSettings();
    }

    public void enableMonitorEditChanged(View view) {
        boolean state = ((Switch)view).isChecked();
        settingsList.set(5, Boolean.toString(state));
        saveSettings();
    }

    public void enableNotificationsChanged(View view) {
        boolean state = ((Switch)view).isChecked();
        settingsList.set(6, Boolean.toString(state));
        saveSettings();
    }

    public void lockPatientOptionsChanged(View view) {
        boolean state = ((Switch)view).isChecked();
        settingsList.set(7, Boolean.toString(state));
        saveSettings();
    }

    private void saveSettings() {
        settings = String.join(",", settingsList);
        String filename = "settings.txt";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, MODE_PRIVATE);
            outputStream.write(settings.getBytes());
            outputStream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mainActivity(View view){

        Intent intent = new Intent(settingsActivity.this, MainActivity.class);
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

}