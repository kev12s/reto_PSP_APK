package com.example.tartangastore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdviseActivity extends AppCompatActivity {

    private Button btnExitAdvise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_advise);

        btnExitAdvise = findViewById(R.id.btnExitAdvise);

        btnExitAdvise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir MainActivity
                Intent intent = new Intent(AdviseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}