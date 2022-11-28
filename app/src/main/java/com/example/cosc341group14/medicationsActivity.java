package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class medicationsActivity extends AppCompatActivity {

    int size = 0;
    int count = 0;
    TextView txtOutput;
    List<String> medInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medications);

        medInfo = new ArrayList<>();

        txtOutput = findViewById(R.id.tv_Empty);

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

    private void showData(){

        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout_meds);

        for (String med: medInfo) {

            String [] medData = med.split(",");

            String medName = medData[0];
            String medDose = medData[4];
            String repeat = medData[1];
            String idName = medName + "Button";

            Button currentMedBtn = new Button(this);
            currentMedBtn.setText(medName);
            //currentMedBtn.setId(idName);

            layout.addView(currentMedBtn);


        }



    }

    public void addMed(View view){

        Intent intent = new Intent(this, addMedActivity.class);
        startActivity(intent);

    }

    public void done(View view){

        finish();

    }
}