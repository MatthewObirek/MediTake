package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class profileActivity extends AppCompatActivity {

    String WriteData;
    String file;

    boolean editMode = false;
    LinearLayout linearLayout;
    TextView EText;
    AlertDialog addDialog;
    ArrayList<Profile> patientList;
    Button addButton;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Patients");
        linearLayout = findViewById(R.id.linearLayout);
        EText = findViewById(R.id.ExplainerText);
        patientList = new ArrayList<Profile>();

        addButton = findViewById(R.id.add_Btn);
        editButton = findViewById(R.id.edit_Btn);

        //Read patients from file

        file = "connection.txt";
        //File read operation
        try {
            FileInputStream fis = openFileInput(file);  //A FileInputStream obtains input bytes from a file in a file system
            InputStreamReader isr = new InputStreamReader(fis); //An InputStreamReader is a bridge from byte streams to character streams
            BufferedReader br = new BufferedReader(isr);    //Reads text from a character-input stream,
            String[] Array = new String[3];
            String line;
            while ((line = br.readLine()) !=null){
                Array = line.split("[,]", 0);
                patientList.add(new Profile(Array));
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }


        if(patientList.size() == 0) {
            EText.setVisibility(View.VISIBLE);
        } else {
            EText.setVisibility(View.GONE);
        }

        for(Profile patient: patientList) {
            addCard(patient);
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                buildAddDialog();
                addDialog.show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = linearLayout.getChildCount();
                if(editMode==false) {
                    editButton.setText("Save");
                    for (int i = 1; i < count; i++) {
                        linearLayout.getChildAt(i).findViewById(R.id.editButtonsLayout).setVisibility(View.VISIBLE);
                        linearLayout.getChildAt(i).setClickable(false);
                    }
                    editMode = true;
                } else {
                    FileOutputStream outputStream;  //Allow a file to be opened for writing
                        try {
                            outputStream = openFileOutput(file, Context.MODE_PRIVATE);
                            for(Profile patient : patientList){
                                WriteData += patient.toString();
                            }

                            outputStream.write(WriteData.getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
                            outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    editButton.setText("Edit");
                    for (int i = 1; i < count; i++) {
                        linearLayout.getChildAt(i).findViewById(R.id.editButtonsLayout).setVisibility(View.GONE);
                        linearLayout.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        linearLayout.getChildAt(i).setClickable(true);
                    }
                    editMode = false;
                }
            }
        });
    }

    private void buildAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_profile, null);

        final EditText id = view.findViewById(R.id.idEdit);
        final EditText name = view.findViewById(R.id.nameEdit);
        id.setText("");
        name.setText("");

        builder.setView(view);
        builder.setTitle("Enter Patient Information")
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EText.setVisibility(view.GONE);
                        patientList.add(new Profile(name.getText().toString(), id.getText().toString(), "Up to date"));
                        addCard(patientList.get(patientList.size()-1));
                        FileOutputStream outputStream;  //Allow a file to be opened for writing
                        try {
                            outputStream = openFileOutput(file, Context.MODE_APPEND);
                            outputStream.write(patientList.get(patientList.size()-1).toString().getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        addDialog = builder.create();
    }

    private void addCard(Profile patient) {


        final View view = getLayoutInflater().inflate(R.layout.card_profile, null);

        View profileButton = view.findViewById(R.id.profileCard);
        TextView nameView = view.findViewById(R.id.namePatient);
        TextView detailView = view.findViewById(R.id.detailsPatient);

        Button delete = view.findViewById(R.id.btnDelete);
        Button detailbtn = view.findViewById(R.id.btnDetails);

        nameView.setText(patient.name);
        detailView.setText(patient.details);
        patient.buildDeleteDialog(view);
        linearLayout.addView(view);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient.deleteDialog.show();
            }
        });

        detailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patient.buildDetailsDialog(nameView, detailView);
                patient.detailsDialog.show();

                nameView.setText(patient.name);
                detailView.setText(patient.details);
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //! NOTE: the step 4 pdf requests that we do not create a database for our prototype
                // I will be using some txt files instead of a database for this section of the prototype.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("altUser", true);
                bundle.putString("patientName", patient.name);
                bundle.putString("filename", patient.filename);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public void mainActivity(View view){

        Intent intent = new Intent(profileActivity.this, MainActivity.class);
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



    // Abstracting Patient Data to clean up code and make it more usable
    public class Profile{
        public String name;
        public String id; // the id would Ideally be used to link Patients to "Nurses"
        public String details;
        public String filename;
        public AlertDialog deleteDialog;
        public AlertDialog detailsDialog;

        Profile(String name, String id, String details){
            this.name = name;
            this.id = id;
            this.details = details;
            this.filename = id+".txt";
        }

        Profile(String[] InfoArray)
        {
            this(InfoArray[0], InfoArray[1], InfoArray[2]);
        }

        public String toString(){
            return this.name + "," + this.id + "," + this.details +"\n";
        }

        //Initialize Delete Dialog.
        public void buildDeleteDialog(View patientView){
            AlertDialog.Builder builder = new AlertDialog.Builder(profileActivity.this);
            //View view = getLayoutInflater().inflate(R.layout.dialog_profile, null);

            //builder.setView(view);
            builder.setTitle("Delete Patient " + name + ", id:" + id + "?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            linearLayout.removeView(patientView);
                            patientList.remove(Profile.this);
                            if(patientList.size() == 0){
                                EText.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            deleteDialog = builder.create();
        }
        //Initalize Edit and Details dialog.
        public void buildDetailsDialog(TextView nameView, TextView detailView){
            AlertDialog.Builder builder = new AlertDialog.Builder(profileActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile, null);

            final EditText id = dialogView.findViewById(R.id.idEdit);
            final EditText name = dialogView.findViewById(R.id.nameEdit);

            id.setText(this.id);
            name.setText(this.name);

            builder.setView(dialogView);
            builder.setTitle("Change Patient Information")
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Profile.this.name = name.getText().toString();
                            Profile.this.id = id.getText().toString();
                            nameView.setText(name.getText().toString());
                            detailView.setText(Profile.this.details);

                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            this.detailsDialog = builder.create();
        }
    }
}
