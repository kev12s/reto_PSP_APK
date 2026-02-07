package com.example.tartangastore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdviseActivity extends AppCompatActivity {

    private Button btnExitAdvise;
    private TextView textViewMessage;
    private Button btnPrevious, btnNext;

    private int currentIndex = 0;
    // Array de IDs de recursos para los mensajes
    private int[] messageIds = {
            R.string.message_1,
            R.string.message_2,
            R.string.message_3,
            R.string.message_4
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_advise);

        btnExitAdvise = findViewById(R.id.btnExitAdvise);

        textViewMessage = findViewById(R.id.textmsg);
        btnPrevious = findViewById(R.id.buttonPrev);
        btnNext = findViewById(R.id.buttonNext);
        showCurrentMessage();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToNextMessage();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPreviousMessage();
            }
        });


        btnExitAdvise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir MainActivity
                Intent intent = new Intent(AdviseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
    private void showCurrentMessage() {
        textViewMessage.setText(getString(messageIds[currentIndex]));
    }

    private void goToNextMessage() {
        currentIndex++;
        if (currentIndex >= messageIds.length) {
            currentIndex = 0; // Volver al inicio
        }
        showCurrentMessage();
    }

    private void goToPreviousMessage() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = messageIds.length - 1; // Ir al final
        }
        showCurrentMessage();
    }

}