package com.example.takeeverythingwithu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class OpeningPage extends AppCompatActivity {

    private Button journeyButton;
    private Button homeButton;
    private Button workButton;
    private Button distanceButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_page);

        journeyButton = (Button) findViewById(R.id.journey);
        homeButton = (Button) findViewById(R.id.homebutton);
        workButton = (Button) findViewById(R.id.workbutton);
        distanceButton = (Button) findViewById(R.id.notificationsettings);

        journeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapsActivity();
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeActivity();
            }
        });

        workButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWorkActivity();
            }
        });

        distanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotificationSettingsActivity();
            }
        });

    }

    public void openMapsActivity() {
        Intent intent = new Intent(OpeningPage.this, MapsActivity.class);
        startActivity(intent);
    }

    public void openHomeActivity() {
        Intent intent = new Intent(OpeningPage.this, HomeActivity.class);
        startActivity(intent);
    }
    public void openWorkActivity() {
        Intent intent = new Intent(OpeningPage.this, WorkActivity.class);
        startActivity(intent);
    }

    public void openNotificationSettingsActivity() {
        Intent intent = new Intent(OpeningPage.this, SettingsActivity.class);
        startActivity(intent);
    }
}
