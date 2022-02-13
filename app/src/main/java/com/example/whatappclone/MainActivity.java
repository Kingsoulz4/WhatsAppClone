package com.example.whatappclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.whatappclone.adapters.FragmentAdapter;
import com.example.whatappclone.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;
    FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        myAuth = FirebaseAuth.getInstance();
        controlsHandle();
        eventsHandle();
    }

    private void controlsHandle() {
        mainBinding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        mainBinding.tabLayout.setupWithViewPager(mainBinding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.itemSignOut:
                signOut();
                break;
            case R.id.itemSetting:
                setting();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setting() {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    private void eventsHandle() {

    }

    private void signOut() {
        myAuth.signOut();
        Intent signOut = new Intent(MainActivity.this, SignInActivity.class);
        startActivity(signOut);
    }
}