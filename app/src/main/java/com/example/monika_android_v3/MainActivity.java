package com.example.monika_android_v3;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.provider.Telephony;
import android.telephony.SmsManager;
//import android.telephony.SmsMessage;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.IntentFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.BufferedReader;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import 	java.io.StringReader;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    IntentFilter intentFilter;
    boolean running = false;
    String busyReason = "busy";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        //declare buttons and get their IDs
        Button awakeButton = (Button) findViewById(R.id.button_available);
        Button busyButton = (Button) findViewById(R.id.button_busy);
        Button textSimulatorButton = (Button) findViewById(R.id.textSimulator);



        awakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = false;
                busyReason = "Available";
                setDisplayReason();
            }
        });

        busyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = true;
                busyReason = "busy";
                setDisplayReason();
                //System.out.println("the user is busy");
            }
        });

        /*
        sleepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                running = true;
                busyReason = "sleeping";
                setDisplayReason();
                //System.out.println("the user is sleeping");

                //Toast toast = Toast.makeText(getApplicationContext(), "Simple Toast", Toast.LENGTH_SHORT);
                //toast.show();
            }
        });
        */

        textSimulatorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast toast = Toast.makeText(getApplicationContext(), readContactList().toString(), Toast.LENGTH_SHORT);
                //toast.show();
                //readContactList()


                contactsChecker("9413437452");

                convertContactList();





            }
        });


        //intent to filter for SMS messages received
        intentFilter = new IntentFilter();
        intentFilter.addAction("SMS_RECEIVED_ACTION");







    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }






    //this runs whenever SmsReceiver.java sends it's broadcast intent (whenever it gets a text)
    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Toast toast = Toast.makeText(getApplicationContext(), "I got a new text!!!: " + intent.getExtras().getString("message"), Toast.LENGTH_LONG);
            //toast.show();

            if (running) {
                System.out.println("Sorry, Johnny is " + busyReason + " right now.");

                //send response
                String senderNumber = intent.getExtras().getString("number");
                sendMsg (senderNumber, generateResponse());

            }

            //display the message in the textView
            //TextView inTxt = (TextView) findViewById(R.id.textMsg);
            //inTxt.setText(intent.getExtras().getString("message"));


        }
    };

    //if the number is new, add it to the list
    //if the number is on the list, do nothing
    private void contactsChecker(String theNumber){

        List<List<String>> ContactsList = convertContactList();

        boolean foundNumber = false;

        for (int contactNumber=0; contactNumber < contactNumber; contactNumber++) {
            if (ContactsList.get(2).get(contactNumber) == theNumber)
                foundNumber = true;
        }



        if (foundNumber){
            System.out.println("Contact List already contains this number.");
        } else {
            appendContactList(theNumber);
            System.out.println("Adding new number to contact list");
        }


        System.out.println(readContactList());


    }

    protected String readContactList(){

        String contactList = "";

        //dealing with internal storage http://www.codebind.com/android-tutorials-and-examples/ndroid-studio-save-file-internal-storage-read-write/
        try {
            FileInputStream fileInputStream= openFileInput("Contacts.txt");
            System.out.println("EXISTING FILE OPENED!");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            String lines;
            while ((lines=bufferedReader.readLine())!=null) {
                stringBuffer.append(lines+"\n");
            }

            System.out.println(stringBuffer.toString());

            contactList = stringBuffer.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Contact List is: " + contactList);

        return contactList;

    }


    //does the same thing as readcontactlist but spits out a 2D arraylist of the contactslist string instead of one long string
    protected List<List<String>> convertContactList(){

        List<List<String>> ContactsList = new ArrayList<List<String>>();

        //dealing with internal storage http://www.codebind.com/android-tutorials-and-examples/ndroid-studio-save-file-internal-storage-read-write/
        try {
            FileInputStream fileInputStream= openFileInput("Contacts.txt");
            System.out.println("EXISTING FILE OPENED!");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String lines;
            while ((lines=bufferedReader.readLine())!=null) {
                List<String> contact = Arrays.asList(lines.split("\\s*,\\s*"));//split a single contact line into an array list
                ContactsList.add(contact); //put the above array list into the 2d array list of all contacts
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //Toast toast = Toast.makeText(getApplicationContext(), ContactsList.get(1).get(1), Toast.LENGTH_SHORT);
        //toast.show();

        return ContactsList;


    }


    protected void appendContactList(String newNumber){

        String newContactList = readContactList() +
                "full name here" + "," + newNumber + "," + "name monika calls them" + "," + "20" + "," + "time of last text";

        //full name, phone number, name Monika calls them, trust level, time of last text
        //"full name here" + "," + "phone number" + "," + "name monika calls them" + "," + "20" + "," + "time of last text"


        try {
            FileOutputStream fileOutputStream = openFileOutput("Contacts.txt",MODE_PRIVATE);

            fileOutputStream.write(newContactList.getBytes());

            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //returns true if the contact list contains the given string "value"
    protected boolean checkContactList(String value){
        if (readContactList().contains(value)){
            return true;
        } else {
            return false;
        }
    }

    protected void sendMsg (String theNumber, String myMsg){
        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(theNumber, null, myMsg, sentPI, deliveredPI);
    }


    @Override
    protected void onResume() {
        //register the receiver
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }


    protected String generateResponse() {



        return "So sorry! Johnny is " + busyReason + " right now.\n-Monika";
    }

    protected void setDisplayReason() {
        final TextView displayReason = (TextView) findViewById(R.id.reasonDisplayer);
        displayReason.setText("Status: " + busyReason);
    }




    //****************************************************************
    //I HAVE TO MODIFY THE apendContactList SO IT DOESN'T USE THE OLD readContactList!!!!!!!
    //****************************************************************






    //turns off the receiver when the app is paused (not running in foreground)
    //I don't want this in the final code so Monika can run in the background
    /*@Override
    protected void onPause() {
        //unregister the receiver
        unregisterReceiver(intentReceiver);
        super.onPause();
    }*/


}
