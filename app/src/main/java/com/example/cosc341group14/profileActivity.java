package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
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

import java.util.ArrayList;

public class profileActivity extends AppCompatActivity {

    boolean editMode = false;

    LinearLayout linearLayout;
    AlertDialog addDialog;
    ArrayList<AlertDialog> deleteDialog;
    Button addButton;
    Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Patients");
        linearLayout = findViewById(R.id.linearLayout);
        deleteDialog = new ArrayList<AlertDialog>();

        addButton = findViewById(R.id.add_Btn);
        editButton = findViewById(R.id.edit_Btn);

        //Read patients from file

        buildAddDialog();
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addDialog.show();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = linearLayout.getChildCount();
                if(editMode==false) {
                    editButton.setText("Done");
                    for (int i = 0; i < count; i++) {
                        linearLayout.getChildAt(i).findViewById(R.id.btnDelete).setVisibility(View.VISIBLE);
                        linearLayout.getChildAt(i).setClickable(false);

                    }
                    editMode = true;
                } else {
                    editButton.setText("Edit");
                    for (int i = 0; i < count; i++) {
                        linearLayout.getChildAt(i).findViewById(R.id.btnDelete).setVisibility(View.GONE);
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

        final EditText name = view.findViewById(R.id.idEdit);
        final EditText id = view.findViewById(R.id.nameEdit);

        builder.setView(view);
        builder.setTitle("Enter Patient Information")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addCard(id.getText().toString(), name.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        addDialog = builder.create();
    }

    private void buildDeleteDialog(View patientView){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //View view = getLayoutInflater().inflate(R.layout.dialog_profile, null);

        //builder.setView(view);
        builder.setTitle("Delete Patient " + ((TextView)patientView.findViewById(R.id.detailsPatient)).getText().toString() + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        linearLayout.removeView(patientView);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        deleteDialog.add(builder.create());
    }


    private void addCard(String name, String details) {
        final View view = getLayoutInflater().inflate(R.layout.card_profile, null);

        View profileButton = view.findViewById(R.id.profileCard);
        TextView nameView = view.findViewById(R.id.namePatient);
        TextView detailView = view.findViewById(R.id.detailsPatient);

        Button delete = view.findViewById(R.id.btnDelete);

        nameView.setText(name);
        detailView.setText(details);

        buildDeleteDialog(view);
        linearLayout.addView(view);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int before = linearLayout.getChildCount();
                deleteDialog.get(linearLayout.indexOfChild(view)).show();
                int after = linearLayout.getChildCount();
                linearLayout.removeView(view);
                if(before != after)
                    //deleteDialog.get(0).show();
                    deleteDialog.remove(linearLayout.indexOfChild(view));
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //! NOTE: the step 4 pdf requests that we do not create a database for our prototype
                // I will be using some txt files instead of a database for this section of the prototype.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("patientName", nameView.getText().toString());
                //bundle.putString("FileName", fileList.get());

                startActivity(intent);
            }
        });


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



    // Abstracting Patient Data to clean up code and make it more usable
    public class Profile{
        public String name;
        public String id;
        public String details;
        public String filename;
        public AlertDialog deleteDialog;

        Profile(String name, String id, String details){
            this.name = name;
            this.id = id;
            this.details = details;
            this.filename = id+".txt";
        }

        //Initialize Delete Dialog.
        private void buildDeleteDialog(View patientView){
            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
            //View view = getLayoutInflater().inflate(R.layout.dialog_profile, null);

            //builder.setView(view);
            builder.setTitle("Delete Patient " + ((TextView)patientView.findViewById(R.id.detailsPatient)).getText().toString() + "?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            linearLayout.removeView(patientView);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            deleteDialog = builder.create();
        }
    }
}
