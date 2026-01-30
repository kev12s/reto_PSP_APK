package com.example.tartangastore;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.filament.View;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public abstract class SettingsActivity  extends AppCompatActivity {

    private Button btnHome;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnHome=findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            private void onClick(View v) {
                // Abrir segunda Activity
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
