package com.example.tartangastore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.tartangastore.api.ApiService;
import com.example.tartangastore.api.RetrofitInstance;
import com.example.tartangastore.model.Apk;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private Button btnSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSettings = findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir SettingsActivity
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // 1. Inicializar Retrofit
        apiService = RetrofitInstance.getApiService();

        // 2. Probar conexión obteniendo la lista de APKs
        obtenerListaApks();
    }

    private void obtenerListaApks() {
        Call<List<Apk>> call = apiService.obtenerTodasApks();

        call.enqueue(new Callback<List<Apk>>() {
            @Override
            public void onResponse(Call<List<Apk>> call, Response<List<Apk>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Apk> apks = response.body();
                    Toast.makeText(MainActivity.this,
                            "✅ Conectado! " + apks.size() + " APKs",
                            Toast.LENGTH_LONG).show();

                    // Aquí puedes procesar la lista...

                } else {
                    Log.e("MiApp",
                            "Error en la respuesta: Código " + response.code() +
                                    ", Mensaje: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Apk>> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "❌ Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}