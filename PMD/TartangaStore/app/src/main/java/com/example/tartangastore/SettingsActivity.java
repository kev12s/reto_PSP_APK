package com.example.tartangastore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SettingsActivity extends AppCompatActivity {

    private Button btnHome;
    private Button btnAdvice;
    private Switch darkModeSwitch;
    private SeekBar volumeSeekBar;
    private Button applyButton;
    private SharedPreferences sharedPreferences;

    // Para sonido del seekbar
    private SoundPool soundPool;
    private int soundId;
    private boolean isSoundLoaded = false;
    private Handler soundHandler = new Handler();
    private Runnable soundRunnable;
    private int lastProgress = -1;

    // Para control de volumen del sistema
    private AudioManager audioManager;

    private static final String PREFS_NAME = "TartangaStorePrefs";
    private static final String VOLUME_KEY = "volume_level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Inicializar AudioManager
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        darkModeSwitch = findViewById(R.id.switch1);
        volumeSeekBar = findViewById(R.id.seekBar);
        applyButton = findViewById(R.id.btnApply);

        // Inicializar SoundPool para el sonido del seekbar
        initSoundPool();

        boolean isDarkMode = ThemeHelper.isDarkMode(this);
        darkModeSwitch.setChecked(isDarkMode);

        // Configurar estado inicial del SeekBar
        int savedVolume = sharedPreferences.getInt(VOLUME_KEY, 50);
        volumeSeekBar.setProgress(savedVolume);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int seekBarProgress = (currentVolume * 100) / maxVolume;
        volumeSeekBar.setProgress(seekBarProgress);
        // Configurar listener del SeekBar
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Convertir progreso (0-100) a volumen del sistema
                    int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                    int systemVolume = (progress * maxVolume) / 10;

                    // Establecer volumen del sistema
                    audioManager.setStreamVolume(
                            AudioManager.STREAM_MUSIC,
                            systemVolume,
                            AudioManager.FLAG_SHOW_UI // Muestra el indicador visual
                    );

                    // Guardar en preferencias
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(VOLUME_KEY, progress);
                    editor.apply();

                    // Reproducir sonido
                    if (isSoundLoaded && Math.abs(progress - lastProgress) >= 5) {
                        playSeekBarSound(progress);
                        lastProgress = progress;
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Cuando el usuario empieza a tocar la barra
                lastProgress = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Cuando el usuario suelta la barra
                if (isSoundLoaded) {
                    // Reproducir sonido al soltar (opcional)
                    soundPool.play(soundId, 0.3f, 0.3f, 1, 0, 1.0f);
                }
            }
        });

        // Listener para el botón Apply
        applyButton.setOnClickListener(v -> {
            applySettings();
        });

        setupNavigationButtons();
    }

    private void initSoundPool() {
        // Crear SoundPool para reproducción rápida de sonidos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(3) // Permitir hasta 3 sonidos simultáneos
                    .setAudioAttributes(attributes)
                    .build();
        } else {
            // Para versiones anteriores a Lollipop
            soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        }

        // Cargar el sonido
        soundId = soundPool.load(this, R.raw.testsfx, 1);

        // Listener para cuando el sonido esté cargado
        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                isSoundLoaded = true;
            }
        });
    }

    private void playSeekBarSound(int progress) {
        if (!isSoundLoaded) return;

        // Cancelar cualquier sonido pendiente
        if (soundRunnable != null) {
            soundHandler.removeCallbacks(soundRunnable);
        }

        // Calcular volumen del sonido basado en el progreso
        float volume = progress / 100f;

        // Reproducir sonido con retraso para evitar sonidos excesivos
        soundRunnable = () -> soundPool.play(soundId, volume, volume, 1, 0, 1.0f);

        soundHandler.postDelayed(soundRunnable, 50);
    }

    private void setupNavigationButtons() {
        Button btnHome = findViewById(R.id.btnExitAdvise);
        Button btnAdvice = findViewById(R.id.btnAdvice);

        btnHome.setOnClickListener(v -> {
            // Abrir MainActivity
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnAdvice.setOnClickListener(v -> {
            // Abrir AdviseActivity
            Intent intent = new Intent(SettingsActivity.this, AdviseActivity.class);
            startActivity(intent);
        });
    }

    private void applySettings() {
        // Obtener valores actuales
        boolean isDarkMode = darkModeSwitch.isChecked();
        int volumeLevel = volumeSeekBar.getProgress();

        // Guardar configuración de tema
        ThemeHelper.saveThemePreference(this, isDarkMode);

        // Guardar volumen
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VOLUME_KEY, volumeLevel);
        editor.apply();

        // Aplicar el tema
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Forzar a que todas las actividades actuales se actualicen
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        // Reiniciar esta actividad para aplicar cambios inmediatamente
        recreate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Asegurarse de que el tema está aplicado correctamente
        ThemeHelper.applyTheme(this);

        // Cargar volumen guardado
        int savedVolume = sharedPreferences.getInt(VOLUME_KEY, 50);
        volumeSeekBar.setProgress(savedVolume);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Guardar el progreso actual al salir
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(VOLUME_KEY, volumeSeekBar.getProgress());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Liberar recursos de SoundPool
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }

        // Remover cualquier callback pendiente
        if (soundRunnable != null) {
            soundHandler.removeCallbacks(soundRunnable);
        }
    }
}