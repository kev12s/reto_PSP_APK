package com.example.tartangastore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class AdviseActivity extends AppCompatActivity {

    private Button btnExitAdvise;
    private TextView textViewMessage;
    private Button btnPrevious, btnNext;
    private VideoView videoView;
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
        videoView = findViewById(R.id.videoSdig);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.salud_digital;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        textViewMessage = findViewById(R.id.textmsg);
        btnPrevious = findViewById(R.id.buttonPrev);
        btnNext = findViewById(R.id.buttonNext);
        showCurrentMessage();
        Button btnOpenGallery = findViewById(R.id.btnOpenGallery);
        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdviseActivity.this, ImageViewerActivity.class);
                startActivity(intent);
            }
        });
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
        setupVideoEvents();
    }
    private void setupVideoEvents() {
        videoView.setOnPreparedListener(mp -> {
            // Video listo para reproducir
            mp.setLooping(true); // Para repetir automáticamente
        });

        videoView.setOnCompletionListener(mp -> {
            // Acciones cuando el video termina
        });

        videoView.setOnErrorListener((mp, what, extra) -> {
            // Manejar errores
            return false;
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
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