package com.example.tartangastore;

import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                    Toast.makeText(MainActivity.this,
                            "❌ Error: " + response.code(),
                            Toast.LENGTH_SHORT).show();
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