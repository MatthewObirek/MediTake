package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.FileOutputStream;

public class addMedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med);

        setupSpinners();

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
        String filename = "medication.txt";
        String fileContents = String.format("%s,%s,%s,%s,%s\n", medName, repeat, hour, minute, dose);
        FileOutputStream outputStream;  //Allow a file to be opened for writing

        try {
            outputStream = openFileOutput(filename, Context.MODE_APPEND);
            outputStream.write(fileContents.getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
            outputStream.close();
            Toast.makeText(getApplicationContext(), medName + " successfully added!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }


        finish();

    }

}