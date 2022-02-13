package com.example.whatappclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.whatappclone.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding settingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(settingsBinding.getRoot());
        getSupportActionBar().hide();
        eventsHandle();

    }

    private void eventsHandle() {
        settingsBinding.linear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ProfileDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}