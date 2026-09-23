package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryAlerts;
    private Button buttonBackToPantry;

    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String KEY_EXPIRY_ALERTS = "expiryAlerts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchExpiryAlerts = findViewById(R.id.switchExpiryAlerts);
        buttonBackToPantry = findViewById(R.id.buttonBackToPantry);

        sharedPreferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        // Load the previously saved setting.
        boolean expiryAlertsEnabled =
                sharedPreferences.getBoolean(KEY_EXPIRY_ALERTS, true);

        switchExpiryAlerts.setChecked(expiryAlertsEnabled);

        // Save the setting whenever the switch changes.
        switchExpiryAlerts.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {

                    SharedPreferences.Editor editor =
                            sharedPreferences.edit();

                    editor.putBoolean(KEY_EXPIRY_ALERTS, isChecked);
                    editor.apply();
                }
        );

        // Return to the previous screen.
        buttonBackToPantry.setOnClickListener(v -> finish());
    }
}