package com.example.tartangastore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

    private static final String PREFS_NAME = "TartangaStorePrefs";
    private static final String VOLUME_KEY = "volume_level";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);



        darkModeSwitch = findViewById(R.id.switch1);
        volumeSeekBar = findViewById(R.id.seekBar);
        applyButton = findViewById(R.id.button7);
        boolean isDarkMode = ThemeHelper.isDarkMode(this);
        darkModeSwitch.setChecked(isDarkMode);

        // Configurar estado inicial del SeekBar
        int savedVolume = sharedPreferences.getInt(VOLUME_KEY, 3);
        volumeSeekBar.setProgress(savedVolume);

        // Listener para el botón Apply
        applyButton.setOnClickListener(v -> {
            applySettings();
        });
        setupNavigationButtons();


    }
    private void setupNavigationButtons() {
        Button btnHome = findViewById(R.id.btnExitAdvise);
        Button btnAdvice = findViewById(R.id.btnAdvice);

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir MainActivity
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        btnAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir MainActivity
                Intent intent = new Intent(SettingsActivity.this, AdviseActivity.class);
                startActivity(intent);
            }
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

        // OPCIONAL: Mostrar mensaje de confirmación
        // Toast.makeText(this, "Configuración aplicada", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Asegurarse de que el tema está aplicado correctamente
        ThemeHelper.applyTheme(this);
    }
}