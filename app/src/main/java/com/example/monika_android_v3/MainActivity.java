package com.example.monika_android_v3;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import android.widget.EditText;
import android.widget.ListView;
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

//query phone contacts
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;

import android.media.ToneGenerator;



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
                //busyReason = "busy";
                EditText textInput = (EditText) findViewById(R.id.busyReason);
                busyReason = textInput.getText().toString();
                setDisplayReason();

                Toast toast = Toast.makeText(getApplicationContext(), "Setting busy reason", Toast.LENGTH_SHORT);
                toast.show();


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

                clearContactList();



                //send test text
                //sendMsg ("9413437452", busyReason);
                //works: 160
                //too long: 161




                //Toast toast = Toast.makeText(getApplicationContext(), readContactList().toString(), Toast.LENGTH_SHORT);
                //toast.show();
                //readContactList()


                //clearContactList();

                //show the current contact list monika has
                //Toast toast = Toast.makeText(getApplicationContext(), convertContactList().toString(), Toast.LENGTH_SHORT);
                //toast.show();




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
            if (running) {
                //send response
                String senderNumber = intent.getExtras().getString("number");
                String senderMessage = intent.getExtras().getString("message");
                if (!senderNumber.equals("9413437452")) { //ensure this isn't a text from myself

                    String response = generateResponse(senderNumber, senderMessage);
                    if (!response.equals("")) {
                        sendMsg(senderNumber, response);
                    }
                }

            }

            //display the message in the textView
            //TextView inTxt = (TextView) findViewById(R.id.textMsg);
            //inTxt.setText(intent.getExtras().getString("message"));
        }
    };


    //is the number in monika's contact list
    private boolean contactsChecker(String theNumber){
        List<List<String>> ContactsList = convertContactList();
        boolean foundNumber = false;

        for (int contactNumber=0; contactNumber < ContactsList.size(); contactNumber++) {
            if (ContactsList.get(contactNumber).get(1).equals(theNumber)) {
                foundNumber = true; //found the number in the existing contacts list
            }
        }

        return foundNumber;
    }

    //returns the contact list as a string
    protected String readContactList(){

        String contactList = "";

        //dealing with internal storage http://www.codebind.com/android-tutorials-and-examples/ndroid-studio-save-file-internal-storage-read-write/
        try {
            FileInputStream fileInputStream= openFileInput("Contacts.txt");
            //System.out.println("EXISTING FILE OPENED!");
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


    //add new number to monika's contact list if it's in the phone's contacts list
    protected void appendContactList(String newNumber){

        Long timeStampLong = System.currentTimeMillis()/1000;
        String timeStamp = timeStampLong.toString();

        String fullName = getPhoneContactName(newNumber);

        if (!fullName.equals("")) { //if there's a contact for this number in my phone, add it to monika's list

            String[] parts = fullName.split(" ");
            String firstName = parts[0];


            String newContactList = readContactList() +
                    fullName + "," + newNumber + "," + firstName + "," + "20" + "," + timeStamp + "," + "Null";
            /* Each contact line has the following information:
             * 0 - full name
             * 1 - phone number
             * 2 - name Monika calls them
             * 3 - trust level
             * 4 - time of last text (in seconds since the epoch - System.currentTimeMillis()/1000)
             * 5 - current state
                * Null - the value put in when the contact is first added to the list and when the prior conversation is older than an alotted time
                * Started - already sent "hi is this important" text
                * Alerted - already sent me an emergency or important alert
                * Ended - monika already said goodbye in response to the user bidding her farewell
             */


            try {
                FileOutputStream fileOutputStream = openFileOutput("Contacts.txt", MODE_PRIVATE);

                fileOutputStream.write(newContactList.getBytes());

                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    protected void sendMsg (String theNumber, String myMsg){

        String SENT = "Message Sent";
        String DELIVERED = "Message Delivered";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);

        SmsManager sms = SmsManager.getDefault();

        if (myMsg.length() > 160){ //if the message is too long (161 characters or longer), break it down into multiple chunks and send them as individual texts
            String firstChunk = myMsg.substring(0, 159) + "-";
            String remainder = myMsg.substring(159);

            sms.sendTextMessage(theNumber, null, firstChunk, sentPI, deliveredPI);

            sendMsg (theNumber, remainder); //run this function again to send the remainder of the message (Recursion!)

        } else {
            sms.sendTextMessage(theNumber, null, myMsg, sentPI, deliveredPI);
        }

    }


    @Override
    protected void onResume() {
        //register the receiver
        registerReceiver(intentReceiver, intentFilter);
        super.onResume();
    }


    //return the trust value for a specific contact
    //if the contact list doesn't contain the number, assume the trust level is the default of 20
    protected int getTrust(String senderNumber){
        List<List<String>> contactList = convertContactList();

        int trust = 20;

        if (contactsChecker(senderNumber)){ //ensure that the number exists in the contact list

            for (int contactNumber=0; contactNumber < contactList.size(); contactNumber++) {
                if (contactList.get(contactNumber).get(1).equals(senderNumber)) {

                    try {
                        trust = Integer.parseInt(contactList.get(contactNumber).get(3));
                    }
                    catch (NumberFormatException e)
                    {
                        trust = 20;
                    }


                }
            }


        }

        return trust;

    }

    protected String generateResponse(String senderNumber, String senderMessage) {

        if (contactsChecker(senderNumber)){ //if the number is recognized

            Long timeStampLong = System.currentTimeMillis()/1000;
            String timeStamp = timeStampLong.toString();


            //if the last text this sender sent was longer than a 20 minutes ago, mark it as null (the timestamp is recorded in seconds so it's * 60)
            if (getTimestamp(senderNumber) < Integer.parseInt(timeStamp) - 60 * 20) {
                setConversationState(senderNumber, "Null");
            }


            //if this is the first sentence of a new conversation
            if (getConversationState(senderNumber).equals("Null")) {
                setTimestamp(senderNumber);

                int trust = getTrust(senderNumber);

                setConversationState(senderNumber, "Started");

                //first sentence to someone who's just texted that monika already knows
                if (trust >= 70) {
                    return "Hey " + getFirstName(senderNumber) + "! Monika here! I'm really sorry but Johnny's " + busyReason + " right now. Hopefully he'll get back to you as soon as he's free. Although if you want to get a hold of him sooner, just let me know and I'll try to contact him for you. Is this time sensitive?\n-Monika";
                } else if (trust >= 20) {
                    return "Hello again " + getFirstName(senderNumber) + ", it's Monika. Unfortunately Johnny's " + busyReason + " right now. If you need, I can try to get his attention now. Is this time sensitive?\n-Monika";
                } else if (trust >= -20) {
                    return "Hello, this is Monika. Johnny is " + busyReason + " right now so he won't be able to respond.\n-Monika";
                } else {
                    return "This is Johnny's digital assistant. He can't respond at the moment.\n-Monika";
                }

            }

            //if a text was sent within 20 minutes after monika's first response, and johnny hasn't been alerted yet, check for emergency or important
            if (getConversationState(senderNumber).equals("Started")) {
                setTimestamp(senderNumber);

                if (isTimeSensitive(senderMessage)){
                    timeSensitiveAlert();
                    //return "Yikes, good luck with that.\n-Monika";
                    int trust = getTrust(senderNumber);

                    setConversationState(senderNumber, "Alerted");

                    //informing that the time sensitive alert has been sent
                    if (trust >= 70) {
                        return "Okay, I did my best trying to let him know! I really hope he heard me.\n-Monika";
                    } else if (trust >= 20) {
                        return "I just let him know, hopefully he heard me.\n-Monika";
                    } else if (trust >= -20) {
                        return "Johnny has been alerted.\n-Monika";
                    } else {
                        return "Your alert has been sent. Goodbye.\n-Monika";
                    }

                }

                //if is emergency goes here


            }


            //check if the user is saying goodbye here and if so, return a goodbye message



            return "";


        } else { //if the number isn't in monika's contact list
            appendContactList(senderNumber); //add new number to monika's contact list if it's in the phone's contacts list
            if (contactsChecker(senderNumber)) { //if the number is now in monika's contact list (meaning it's the first time monika will speak to them but i know them already)
                setTimestamp(senderNumber);
                setConversationState(senderNumber, "Started");
                return "Hi " + getFirstName(senderNumber) + "! I'm Monika, Johnny's digital assistant. It's very nice to meet you! Unfortunately Johnny's " + busyReason + " right now. If you need, I can try to get his attention. Is your message time sensitive?\n-Monika";
            } else { //if the number wasn't in my phone contacts list
                return ""; //don't send a response
            }
        }
    }


    //check if it was an affirmative to the emergency question
    protected boolean isTimeSensitive(String senderMessage){

        String[] affirmativeWords = new String[] {"yes", "yup", "correct", "now", "ye", "now", "hurry"};

        String modifiedMessage = senderMessage.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        String[] words = modifiedMessage.split("\\s+");
        for (String word: words) {
            for (String affirmativeWord: affirmativeWords) {
                if (word.equals(affirmativeWord)) {
                    return true;
                }
            }
        }
        return false;

    }


    //check if it was an affirmative to the emergency question
    protected boolean isEmergency(String senderMessage){

        String[] affirmativeWords = new String[] {"emergency", "hospital", "dying", "dead", "police"};

        String modifiedMessage = senderMessage.replaceAll("[^a-zA-Z ]", "").toLowerCase();

        String[] words = modifiedMessage.split("\\s+");
        for (String word: words) {
            for (String affirmativeWord: affirmativeWords) {
                if (word.equals(affirmativeWord)) {
                    return true;
                }
            }
        }
        return false;

    }

    //plays beep if the ringtone volume isn't on silent
    protected void timeSensitiveAlert() {

        //get current ringtone volume (https://stackoverflow.com/questions/4593552/how-do-you-get-set-media-volume-not-ringtone-volume-in-android)
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int ringtoneVolume = audio.getStreamVolume(AudioManager.STREAM_RING);

        //Toast toast = Toast.makeText(getApplicationContext(), "ringtone: " + ringtoneVolume, Toast.LENGTH_SHORT);
        //toast.show();

        if (ringtoneVolume >= 1){ //if the ringtone isn't set to silent or vibrate

            //the initial music volume
            int musicVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

            //set music volume to max
            int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);

            //play beep
            int streamType = AudioManager.STREAM_MUSIC;
            ToneGenerator beep = new ToneGenerator(streamType, 45);
            int beepDuration = 1000; //milliseconds
            beep.startTone(ToneGenerator.TONE_CDMA_HIGH_L, beepDuration);

            //wait until the tone is done playing before proceeding
            try
            {
                Thread.sleep(beepDuration);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }

            //turn music volume back down to it's initial value
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, musicVolume, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    //plays loud long beep no matter what
    protected void emergencyAlert() {
        AudioManager audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //the initial music volume
        int musicVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        //set music volume to max
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);

        //play beep
        int streamType = AudioManager.STREAM_MUSIC;
        ToneGenerator beep = new ToneGenerator(streamType, 100);
        int beepDuration = 5000; //milliseconds
        beep.startTone(ToneGenerator.TONE_CDMA_HIGH_L, beepDuration);

        //wait until the tone is done playing before proceeding
        try
        {
            Thread.sleep(beepDuration);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }

        //turn music volume back down to it's initial value
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, musicVolume, AudioManager.FLAG_PLAY_SOUND);

    }

    protected void setDisplayReason() {
        final TextView displayReason = (TextView) findViewById(R.id.reasonDisplayer);
        displayReason.setText("Status: " + busyReason);
    }

    //delete the contents of the contacts list
    //only to be used for hard resets of her personality or when the variables for the contacts list change
    protected void clearContactList(){
        try {
            FileOutputStream fileOutputStream = openFileOutput("Contacts.txt",MODE_PRIVATE);

            byte[] emptyArray = new byte[0];
            fileOutputStream.write(emptyArray);

            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //take a 2d array list and convert it into the format used by contacts.txt (commas separating elements in a contacxt and \n between each contact
    protected void setContactList(List<List<String>> newContactList){

        String contactListString = "";

        for (int contactNumber = 0; contactNumber < newContactList.size(); contactNumber++) {
            for (int elementNumber = 0; elementNumber < newContactList.get(contactNumber).size(); elementNumber++) {
                contactListString += newContactList.get(contactNumber).get(elementNumber).toString();

                if (elementNumber < newContactList.get(contactNumber).size() -1 ) {
                    contactListString += ",";
                }

            }

            if (contactNumber < newContactList.size() -1 ) {
                contactListString += "\n";
            }

        }


        try {
            FileOutputStream fileOutputStream = openFileOutput("Contacts.txt",MODE_PRIVATE);

            fileOutputStream.write(contactListString.getBytes());

            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //remove the return once testing is done
    protected void setTimestamp(String senderNumber) {
        List<List<String>> contactList = convertContactList();

        if (contactsChecker(senderNumber)) { //ensure that the number exists in the contact list

            for (int contactNumber = 0; contactNumber < contactList.size(); contactNumber++) {
                if (contactList.get(contactNumber).get(1).equals(senderNumber)) {

                    Long timeStampLong = System.currentTimeMillis()/1000;
                    String timeStamp = timeStampLong.toString();

                    contactList.get(contactNumber).set(4, timeStamp);

                }
            }


        }

        setContactList(contactList);


    }


    protected int getTimestamp(String senderNumber) {
        List<List<String>> contactList = convertContactList();
        String timeStamp = "";

        if (contactsChecker(senderNumber)) { //ensure that the number exists in the contact list
            for (int contactNumber = 0; contactNumber < contactList.size(); contactNumber++) {
                if (contactList.get(contactNumber).get(1).equals(senderNumber)) {
                    timeStamp = contactList.get(contactNumber).get(4).toString();
                }
            }
        }

        return Integer.parseInt(timeStamp);
    }

    protected void setConversationState(String senderNumber, String newState) {
        List<List<String>> contactList = convertContactList();
        if (contactsChecker(senderNumber)) { //ensure that the number exists in the contact list
            for (int contactNumber = 0; contactNumber < contactList.size(); contactNumber++) {
                if (contactList.get(contactNumber).get(1).equals(senderNumber)) {
                    contactList.get(contactNumber).set(5, newState);
                }
            }
        }
        setContactList(contactList);
    }

    protected String getConversationState(String senderNumber) {
        List<List<String>> contactList = convertContactList();
        String conversationState = "";
        if (contactsChecker(senderNumber)) { //ensure that the number exists in the contact list
            for (int contactNumber = 0; contactNumber < contactList.size(); contactNumber++) {
                if (contactList.get(contactNumber).get(1).equals(senderNumber)) {
                    conversationState = contactList.get(contactNumber).get(5);
                }
            }
        }
        return conversationState;
    }

    protected String getPhoneContactName(String senderNumber) {
        Uri lookupUri = Uri.withAppendedPath(
                PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(senderNumber));
        String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
        Cursor cur = getApplicationContext().getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                //cur.getCount() == 4
                //cur.getColumnName(2) == display_name
                String name = cur.getString(2);
                cur.close();
                return name;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return "";
    }


    protected String getFirstName(String senderNumber) {
        List<List<String>> contactList = convertContactList();
        String firstName = "";

        if (contactsChecker(senderNumber)) { //ensure that the number exists in the contact list
            for (int contactNumber = 0; contactNumber < contactList.size(); contactNumber++) {
                if (contactList.get(contactNumber).get(1).equals(senderNumber)) {
                    firstName = contactList.get(contactNumber).get(2);
                }
            }
        }

        return firstName;
    }



    //****************************************************************
    //I HAVE TO MODIFY THE apendContactList SO IT DOESN'T USE THE OLD readContactList!!!!!!!
    //****************************************************************


}
