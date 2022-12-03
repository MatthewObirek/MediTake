package com.example.cosc341group14;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class medicationsActivity extends AppCompatActivity {

    int size = 0;
    TextView txtOutput;
    List<String> medInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);

        medInfo = new ArrayList<>();

        txtOutput = findViewById(R.id.tv_Empty);

        String filename = getIntent().getExtras().getString("filename");
        readData(filename);
    }

    // load data from file into arraylist
    public void readData(String file){

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
            showData();
            // if no file found, informs user there are no records
        }catch (FileNotFoundException e){
            txtOutput.setText("No records stored, please add at least 1 record.");
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    // programmatically create layout based on medication file
    private void showData(){

        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout_meds);

        for (String med: medInfo) {

            String [] medData = med.split(",");

            String medName = medData[0];
            String medDose = medData[4];
            String repeat = medData[1];

            // create medication button
            Button currentMedBtn = new Button(this);
            currentMedBtn.setText(medName + "\n" + medDose + " - " + repeat);
            currentMedBtn.setTextSize(18);

            // add med button to layout
            layout.addView(currentMedBtn);

            // create layout for edit and delete
            LinearLayout options = new LinearLayout(this);
            options.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            options.setOrientation(LinearLayout.HORIZONTAL);
            options.setVisibility(View.GONE);

            layout.addView(options);

            // create option buttons along with their onClicks
            Button editBtn = new Button(this);
            editBtn.setText("Edit Details");
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(medicationsActivity.this, editMedActivity.class);
                    intent.putExtra("med", med);
                    startActivity(intent);
                    finish();
                }
            });

            Button delBtn = new Button(this);
            delBtn.setText("Delete");
            delBtn.setBackgroundColor(Color.parseColor("#ff0000"));
            delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert(view, medName, med);
                }
            });

            currentMedBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (options.getVisibility() == View.VISIBLE){
                        options.setVisibility(View.GONE);
                    }else {
                        options.setVisibility(View.VISIBLE);
                    }

                }
            });

            options.addView(editBtn);
            options.addView(delBtn);
        }
    }

    // confirmation for deletion
    public void alert(View view, String medName, String medData) {


        AlertDialog.Builder builder = new AlertDialog.Builder(medicationsActivity.this);

        builder.setCancelable(true);
        builder.setTitle("Delete Medication Warning");
        builder.setMessage("Are you sure you want to delete your medication: " + medName + " ?");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Delete!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMed(medData);
            }
        });
        builder.show();
    }

    // rewrites file without med
    private void deleteMed(String medData){

        //List <String> updateMedInfo = new ArrayList<>();
        String fileContents = "";

        for (String s: medInfo) {
            if (!s.equals(medData)){
                fileContents += s +"\n";
            }
        }



        //File write operation
        String filename = "medication.txt";
        FileOutputStream outputStream;  //Allow a file to be opened for writing

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(fileContents.getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
            outputStream.close();
            Toast.makeText(getApplicationContext(), "Medication successfully deleted", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }
        reloadActivity();
    }

    // "reloads" medicationsActivity
    private void reloadActivity(){
        Intent intent = new Intent(medicationsActivity.this, medicationsActivity.class);
        startActivity(intent);
        finish();
    }

    // start addMedActivity
    public void addMed(View view){

        Intent intent = new Intent(this, addMedActivity.class);
        startActivity(intent);
        finish();

    }

    // close activity
    public void done(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }
}