package com.example.cosc341group14;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
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
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    String filenameMedication;
    String patientName = null;
    //format for medication is this.
    //MedName, Repeat, Hour, Minute, Dose
    ArrayList<String> medList;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDate();

        cal = Calendar.getInstance();

        getSupportActionBar().setTitle("Calendar");
        medList= new ArrayList<String>();
        filenameMedication = "medication.txt";

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
                patientName = bundle.getString("patientName");
                altUserName.setText(patientName);
                if (patientName.equals("")) {
                    altUser.setVisibility(View.GONE);
                } else {
                    altUser.setVisibility(View.VISIBLE);
                }
                filenameMedication = bundle.getString("filename"); //Demo not not
            } else {
                altUser.setVisibility(View.GONE);
            }
        } else {
            altUser.setVisibility(View.GONE);
        }

        //Reads file and adds example data for prototype.
        //File read operation
        String line = "";
        String data = "";
        try {
            FileInputStream fis = openFileInput(filenameMedication);  //A FileInputStream obtains input bytes from a file in a file system
            InputStreamReader isr = new InputStreamReader(fis); //An InputStreamReader is a bridge from byte streams to character streams
            BufferedReader br = new BufferedReader(isr);    //Reads text from a character-input stream,
            while ((line = br.readLine()) != null) {
                data = line;
                medList.add(data);
            }
            // if no file found, informs user there are no records
        }catch (FileNotFoundException e){
            medList.add("Aleve,Daily,8,0,1 doses,0");
            medList.add("Advil,Daily,18,0,2 doses,0");
            FileOutputStream outputStream;  //Allow a file to be opened for writing
            try {
                outputStream = openFileOutput(filenameMedication, Context.MODE_PRIVATE);
                for(String elem: medList)
                {
                    data += (elem +"\n");
                }

                outputStream.write(data.getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
                outputStream.close();
            } catch (Exception ex) {
                e.printStackTrace();
            }
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        for(String elem: medList){
            addCard(elem);
        }

    }


    private void addCard(String med) {
        final View view = getLayoutInflater().inflate(R.layout.card_med, null);
        LinearLayout reminderOptions = (LinearLayout) view.findViewById(R.id.editButtonsLayout);
        LinearLayout due = (LinearLayout) findViewById(R.id.dueList);
        LinearLayout taken = (LinearLayout) findViewById(R.id.takenList);
        LinearLayout later = (LinearLayout) findViewById(R.id.laterTodayList);
        View medButton = view.findViewById(R.id.medCard);
        TextView nameView = view.findViewById(R.id.nameMed);

        Button take = view.findViewById(R.id.btnTake);
        Button snooze = view.findViewById(R.id.btnSnooze);
        Button skip = view.findViewById(R.id.btnSkip);

        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int currentMinute = cal.get(Calendar.MINUTE);
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);



        String[] array = med.split("[,]",0);

        String category;
        if(array[5].equals(Integer.toString(dayOfMonth))) {
            category = "taken";
        } else if(currentHour > Integer.parseInt(array[2]) || (currentHour == Integer.parseInt(array[2]) && currentMinute >= Integer.parseInt(array[3]))) {
            category = "due";
        } else {
            category = "later";
        }

        String meridiem = "am";
        int timeHour;
        if ((timeHour = (Integer.parseInt(array[2]))) > 12) {
            meridiem = "pm";
            timeHour -= 12;
        }
        int timeMinute = Integer.parseInt(array[3]);
        nameView.setText(String.format("%02d:%02d", timeHour, timeMinute) + " " + meridiem + " - " + array[0] + " (" + array[4] + " - " + array[1] + ")");

        if(category == "due"){
            //added to due
            due.addView(view);
        } else if(category == "later") {
            //added to later today
            later.addView(view);
            view.findViewById(R.id.btnSnooze).setBackgroundColor(Color.parseColor("#777777"));
        } else if(category == "taken") {
            taken.addView(view);
            view.findViewById(R.id.btnTake).setBackgroundColor(Color.parseColor("#777777"));
            view.findViewById(R.id.btnSnooze).setBackgroundColor(Color.parseColor("#777777"));
            view.findViewById(R.id.btnSkip).setBackgroundColor(Color.parseColor("#777777"));
        }

        //linearLayout.addView(view);
        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getParent() == taken) {
                    Toast.makeText(getApplicationContext(), "You already completed this reminder!", Toast.LENGTH_SHORT).show();
                } else {
                    if (view.getParent() == due) {
                        due.removeView(view);
                    } else if (view.getParent() == later) {
                        later.removeView(view);
                    }
                    taken.addView(view);
                    view.findViewById(R.id.btnTake).setBackgroundColor(Color.parseColor("#777777"));
                    view.findViewById(R.id.btnSnooze).setBackgroundColor(Color.parseColor("#777777"));
                    view.findViewById(R.id.btnSkip).setBackgroundColor(Color.parseColor("#777777"));
                    reminderOptions.setVisibility(View.GONE);
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    ArrayList<String> medInfo = readData(filenameMedication);
                    for (int i = 0; i < medInfo.size(); i++) {
                        if (medInfo.get(i).equals(med)){
                            String[] medArray = med.split(",");
                            medArray[5] = Integer.toString(dayOfMonth);
                            medInfo.set(i, String.join(",", medArray));
                        }
                    }
                    writeData(filenameMedication, medInfo);
                }
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getParent() == taken) {
                    Toast.makeText(getApplicationContext(), "You already completed this reminder!", Toast.LENGTH_SHORT).show();
                } else {
                    if (view.getParent() == due) {
                        due.removeView(view);
                    } else if (view.getParent() == later) {
                        later.removeView(view);
                    }
                    taken.addView(view);
                    view.findViewById(R.id.btnTake).setBackgroundColor(Color.parseColor("#777777"));
                    view.findViewById(R.id.btnSnooze).setBackgroundColor(Color.parseColor("#777777"));
                    view.findViewById(R.id.btnSkip).setBackgroundColor(Color.parseColor("#777777"));
                    reminderOptions.setVisibility(View.GONE);
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
                    ArrayList<String> medInfo = readData(filenameMedication);
                    for (int i = 0; i < medInfo.size(); i++) {
                        if (medInfo.get(i).equals(med)){
                            String[] medArray = med.split(",");
                            medArray[5] = Integer.toString(dayOfMonth);
                            medInfo.set(i, String.join(",", medArray));
                        }
                    }
                    writeData(filenameMedication, medInfo);
                }
            }
        });

        snooze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.getParent() == taken) {
                    Toast.makeText(getApplicationContext(), "You already completed this reminder!", Toast.LENGTH_SHORT).show();
                } else if (view.getParent() == later){
                    Toast.makeText(getApplicationContext(), "Task Is not due, and cannot be snoozed!", Toast.LENGTH_SHORT).show();
                }else {
                    due.removeView(view);
                    later.addView(view);
                    view.findViewById(R.id.btnSnooze).setBackgroundColor(Color.parseColor("#777777"));
                    reminderOptions.setVisibility(View.GONE);

                }
            }
        });

        medButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (reminderOptions.isShown() == false) {
                    reminderOptions.setVisibility(View.VISIBLE);
                } else {
                    reminderOptions.setVisibility(View.GONE);
                }
            }
        });

    }

    // load data from file into arraylist
    public ArrayList<String> readData(String fileName){

        String file = fileName;
        String line = "";
        String data = "";

        ArrayList<String> medInfo = new ArrayList<>();

        //File read operation
        try {
            FileInputStream fis = openFileInput(file);  //A FileInputStream obtains input bytes from a file in a file system
            InputStreamReader isr = new InputStreamReader(fis); //An InputStreamReader is a bridge from byte streams to character streams
            BufferedReader br = new BufferedReader(isr);    //Reads text from a character-input stream,
            while ((line = br.readLine()) != null) {
                data = line;
                medInfo.add(data);
            }
            // if no file found, informs user there are no records
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        return medInfo;
    }

    private void writeData(String fileName, ArrayList<String> fileInfo) {
        FileOutputStream outputStream;  //Allow a file to be opened for writing
        String data = "";
        for(String elem: fileInfo)
        {
            data += (elem +"\n");
        }
        try {
            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(data.getBytes());    //FileOutputStream is meant for writing streams of raw bytes.
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //TODO: 1 shift this into the take button at line 141
    public void takeMedicine (View view) {
        LinearLayout due = (LinearLayout) findViewById(R.id.dueList);
        LinearLayout taken = (LinearLayout) findViewById(R.id.takenList);
        LinearLayout reminderOptions = (LinearLayout) findViewById(R.id.dueOptions);
        Button medication = (Button) due.getChildAt(0);

        due.removeView(medication);
        taken.addView(medication);

        due.invalidate();
        taken.invalidate();
        reminderOptions.setVisibility(View.GONE);
        medication.setTextColor(Color.parseColor("#FFFFFF"));
    }

    public void mainActivity(View view){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    // sets month and day at top of screen
    private void setDate(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month = month_date.format(c.getTime());
        int day = c.get(Calendar.DAY_OF_MONTH);

        TextView date = findViewById(R.id.dateView);
        date.setText(String.format("%s %d", month, day));

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
        Bundle bundle = new Bundle();
        bundle.putString("filename", filenameMedication);
        if(getIntent().hasExtra("patientName"))
            bundle.putString("patientName", patientName);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    public void editActivity(View view){

        Intent intent = new Intent(this, medicationsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("filename", filenameMedication);
        if(getIntent().hasExtra("patientName"))
            bundle.putString("patientName", patientName);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

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