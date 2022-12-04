package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.util.Date;

public class addMedActivity extends AppCompatActivity {
    private String filename;
    private String patientName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);

        getSupportActionBar().setTitle("Add Medication");

        setupSpinners();

        Bundle bundle = getIntent().getExtras();
        filename = bundle.getString("filename");
        if(getIntent().hasExtra("patientName")) {
            patientName = bundle.getString("patientName");
        } else {
            patientName = "";
        }

    }

    private void setupSpinners(){

        Spinner spinner = (Spinner) findViewById(R.id.repeatSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.repeat_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner spinner2 = (Spinner) findViewById(R.id.doseSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.dose_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

    }

    // close activity
    public void cancel(View view){
        Intent intent = new Intent(this, medicationsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("filename", filename);
        if(patientName!=null) {
            bundle.putString("patientName", patientName);
            bundle.putBoolean("altUser", true);
        }
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    // submit form and write to file
    public void submit(View view){

        String medName = ((EditText)findViewById(R.id.etMedName)).getText().toString();

        if (medName.length() <= 0 ){
            Toast.makeText(getApplicationContext(), "Please enter a valid medication name.", Toast.LENGTH_SHORT).show();
            return;
        }

        Spinner spinner = findViewById(R.id.repeatSpinner);
        String repeat = spinner.getSelectedItem().toString();

        TimePicker picker = findViewById(R.id.datePicker1);
        int hr = picker.getHour();
        int min = picker.getMinute();

        String hour = Integer.toString(hr);
        String minute = Integer.toString(min);

        Spinner spinner2 = findViewById(R.id.doseSpinner);
        String dose = spinner2.getSelectedItem().toString();


        //File write operation
        String fileContents = String.format("%s,%s,%s,%s,%s,%s\n", medName, repeat, hour, minute, dose,"0");
        FileOutputStream outputStream;  //Allow a file to be opened for writing

        try {
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(fileContents.getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
            outputStream.close();
            Toast.makeText(getApplicationContext(), medName + " successfully added!", Toast.LENGTH_SHORT).show();

            //Set reminder
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlarmReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putString("medication", medName);
            bundle.putString("doses", dose);
            bundle.putInt("hour", Integer.parseInt(hour));
            bundle.putInt("minute", Integer.parseInt(minute));
            int notificationId = (medName+dose+hour+minute).hashCode();
            bundle.putInt("id", notificationId);
            bundle.putString("patientName", patientName);
            bundle.putString("extras", "");
            intent.putExtras(bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, notificationId, intent, PendingIntent.FLAG_MUTABLE);

            Date date = new Date();
            date.setHours(Integer.parseInt(hour));
            date.setMinutes(Integer.parseInt(minute));
            date.setSeconds(0);

            long time = date.getTime();

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, medicationsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("filename", filename);
        if(patientName!=null) {
            bundle.putString("patientName", patientName);
            bundle.putBoolean("altUser", true);
        }
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

}