package com.example.takeeverythingwithu;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    String distance;
    String notification;
    Integer viewChoice = null;
    SharedPreferences mySharedPreference3;

    EditText distanceInput;
    EditText notificationInput;

    Button distanceSet;
    Button notificationSet;
    ImageButton Standard;
    ImageButton Retro;
    ImageButton Night;
    ImageButton distanceRefresh;
    ImageButton notificationRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        mySharedPreference3 = getSharedPreferences("settingsChoice", Context.MODE_PRIVATE);
        SharedPreferences InternalSharedPreferences = getApplicationContext().getSharedPreferences("settingsChoice", Context.MODE_PRIVATE);

        distanceInput = (EditText) findViewById(R.id.distanceInput);
        notificationInput = (EditText) findViewById(R.id.notificationtext);

        distanceSet = (Button) findViewById(R.id.setdistancebutton);
        notificationSet = (Button) findViewById(R.id.setnoficationbutton);
        Standard = (ImageButton) findViewById(R.id.standardbutton);
        Retro = (ImageButton) findViewById(R.id.retrobutton);
        Night = (ImageButton) findViewById(R.id.nightbutton);
        distanceRefresh = (ImageButton) findViewById(R.id.distancerefresh);
        notificationRefresh = (ImageButton) findViewById(R.id.notifyrefresh);
        Retro.setImageAlpha(120);
        Night.setImageAlpha(120);

        viewChoice = InternalSharedPreferences.getInt("viewchoice", 0);

        if (viewChoice != null) {
            if (viewChoice == 0) {
                Standard.setImageAlpha(255);
                Retro.setImageAlpha(120);
                Night.setImageAlpha(120);
            } else if (viewChoice == 1) {
                Retro.setImageAlpha(255);
                Standard.setImageAlpha(120);
                Night.setImageAlpha(120);
            } else if (viewChoice == 2) {
                Night.setImageAlpha(255);
                Standard.setImageAlpha(120);
                Retro.setImageAlpha(120);
            }
        }

        distanceSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDistance();
            }
        });

        notificationSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingNotification();
            }
        });

        Standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                standardmap();
            }
        });

        Retro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retromap();
            }
        });

        Night.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nightmap();
            }
        });

        distanceRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                distanceRefreshing();
            }
        });

        notificationRefresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                notificationRefreshing();
            }
        });
    }

    void settingDistance(){
        distance = distanceInput.getText().toString();
        SharedPreferences.Editor HomeEditor = mySharedPreference3.edit();
        HomeEditor.putFloat("distance", Float.parseFloat(distance));
        HomeEditor.apply();
        Toast.makeText(SettingsActivity.this, "Distance for Reminder Set", Toast.LENGTH_LONG).show();
    }
    void settingNotification(){
        notification = notificationInput.getText().toString();
        SharedPreferences.Editor HomeEditor = mySharedPreference3.edit();
        HomeEditor.putString("notification", notification);
        HomeEditor.apply();
        Toast.makeText(SettingsActivity.this, "Notification Message Set", Toast.LENGTH_LONG).show();
    }
    void standardmap(){
        viewChoice = 0;
        SharedPreferences.Editor HomeEditor = mySharedPreference3.edit();
        HomeEditor.putInt("viewchoice", viewChoice);
        HomeEditor.apply();
        Toast.makeText(SettingsActivity.this, "Standard Map View Set", Toast.LENGTH_LONG).show();
        Standard.setImageAlpha(255);
        Retro.setImageAlpha(120);
        Night.setImageAlpha(120);
    }
    void retromap(){
        viewChoice = 1;
        SharedPreferences.Editor HomeEditor = mySharedPreference3.edit();
        HomeEditor.putInt("viewchoice", viewChoice);
        HomeEditor.apply();
        Toast.makeText(SettingsActivity.this, "Retro Map View Set", Toast.LENGTH_LONG).show();
        Retro.setImageAlpha(255);
        Standard.setImageAlpha(120);
        Night.setImageAlpha(120);
    }
    void nightmap(){
        viewChoice = 2;
        SharedPreferences.Editor HomeEditor = mySharedPreference3.edit();
        HomeEditor.putInt("viewchoice", viewChoice);
        HomeEditor.apply();
        Toast.makeText(SettingsActivity.this, "Night Map View Set", Toast.LENGTH_LONG).show();
        Night.setImageAlpha(255);
        Standard.setImageAlpha(120);
        Retro.setImageAlpha(120);
    }
    void distanceRefreshing(){
        distance = "0.1";
        SharedPreferences.Editor HomeEditor = mySharedPreference3.edit();
        HomeEditor.putFloat("distance", Float.parseFloat(distance));
        HomeEditor.apply();
        Toast.makeText(SettingsActivity.this, "Distance for Reminder Reset to Default", Toast.LENGTH_LONG).show();
    }
    void notificationRefreshing(){
        notification = "Your journey is ending, so grab all your valuables!";
        SharedPreferences.Editor HomeEditor = mySharedPreference3.edit();
        HomeEditor.putString("notification", notification);
        HomeEditor.apply();
        Toast.makeText(SettingsActivity.this, "Notification Message Reset to Default", Toast.LENGTH_LONG).show();
    }

};