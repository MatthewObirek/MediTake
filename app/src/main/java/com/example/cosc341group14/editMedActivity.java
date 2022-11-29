package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class editMedActivity extends AppCompatActivity {

    String curMed = "";
    String [] medFields;
    List<String> medInfo;
    int size = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_med);

        curMed = getIntent().getStringExtra("med");
        medFields = curMed.split(",");
        medInfo = new ArrayList<>();

        setupSpinners();
        setupFields();
        readData();

    }

    // load data from file into arraylist
    public void readData(){

        String file = "medication.txt";
        String line = "";
        String data = "";

        //File read operation
        try {
            FileInputStream fis = openFileInput(file);  //A FileInputStream obtains input bytes from a file in a file system
            InputStreamReader isr = new InputStreamReader(fis); //An InputStreamReader is a bridge from byte streams to character streams
            BufferedReader br = new BufferedReader(isr);    //Reads text from a character-input stream,
            while ((line = br.readLine()) != null) {
                data = line;
                medInfo.add(data);
                size++;
            }
            // if no file found, informs user there are no records
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    // set up fields with value of medication to be edited
    private void setupFields(){

        EditText medName = findViewById(R.id.etMedName);
        medName.setText(medFields[0]);

        TimePicker picker = findViewById(R.id.datePicker1);
        int hour = Integer.parseInt(medFields[2]);
        int min = Integer.parseInt(medFields[3]);
        picker.setHour(hour);
        picker.setMinute(min);

    }

    private void setupSpinners() {

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

        if (curMed != null){

            int spinnerPos = adapter.getPosition(medFields[1]);
            int spinner2Pos = adapter2.getPosition(medFields[4]);

            spinner.setSelection(spinnerPos);
            spinner2.setSelection(spinner2Pos);

        }


    }

    public void cancel(View view) {

        backToMeds();

    }

    // submit fields, then overwrite file with edit
    public void submit(View view) {

        String medName = ((EditText) findViewById(R.id.etMedName)).getText().toString();

        if (medName.length() <= 0) {
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

        String fileContents = "";

        //File write operation
        String filename = "medication.txt";

        // iterate through meds to prepare filecontents
        for (String s: medInfo) {
            // if med was not being edited, add to file content, otherwise add new edits
            if (!s.equals(curMed)){
                fileContents += s +"\n";
            } else {
                String editContents = String.format("%s,%s,%s,%s,%s\n", medName, repeat, hour, minute, dose);
                fileContents += editContents;
            }
        }

        FileOutputStream outputStream;  //Allow a file to be opened for writing

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Medication edited successfully.", Toast.LENGTH_SHORT).show();
        backToMeds();
    }

    // return back to medicationsActivity
    private void backToMeds(){

        Intent intent = new Intent(this, medicationsActivity.class);
        startActivity(intent);
        finish();

    }

}