package com.example.tartangastore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.tartangastore.api.ApiService;
import com.example.tartangastore.api.RetrofitInstance;
import com.example.tartangastore.model.Apk;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ApiService apiService;
    private Button btnSettings;
    private Button btnAdvice;
    private RecyclerView recyclerView;
    private ItemAdapter itemAdapter;
    private List<Apk> listaApks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_main);
        
        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerView);
        btnAdvice = findViewById(R.id.btnAdvice);
        btnSettings = findViewById(R.id.btnSettings);
        
        // Configurar RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        
        // Inicializar adapter con lista vacía - SOLO onItemClick
        itemAdapter = new ItemAdapter(this, listaApks, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Apk apk) {
                // pasar parametros a la ventana de descarga
                Intent intent = new Intent(MainActivity.this, AppDownloadActivity.class);
                intent.putExtra("APK_ID", apk.getId());
                intent.putExtra("APK_NOMBRE", apk.getNombre());
                intent.putExtra("APK_DESCRIPCION", apk.getDescripcion());
                intent.putExtra("APK_FILENAME", apk.getNombreApk());
                startActivity(intent);

                // Opcional: Toast de confirmación
                Toast.makeText(MainActivity.this,
                        "Abriendo: " + apk.getNombre(),
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        recyclerView.setAdapter(itemAdapter);

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AdviseActivity.class);
                startActivity(intent);
            }
        });

        // Inicializar Retrofit
        apiService = RetrofitInstance.getApiService();

        // Obtener lista de APKs
        obtenerListaApks();
    }

    private void obtenerListaApks() {
        Call<List<Apk>> call = apiService.obtenerTodasApks();

        call.enqueue(new Callback<List<Apk>>() {
            @Override
            public void onResponse(Call<List<Apk>> call, Response<List<Apk>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaApks = response.body();
                    
                    if (listaApks.isEmpty()) {
                        Toast.makeText(MainActivity.this,
                                "No hay APKs disponibles",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        itemAdapter.updateData(listaApks);
                        
                        Toast.makeText(MainActivity.this,
                                 listaApks.size() + " APKs cargadas",
                                Toast.LENGTH_SHORT).show();
                        
                        // Log para debugging
                        for (Apk apk : listaApks) {
                            Log.d("MainActivity", "APK cargada: " + apk.getNombre() + 
                                  " - ID: " + apk.getId());
                        }
                    }

                } else {
                    String errorMsg = "Error " + response.code();
                    if (response.message() != null) {
                        errorMsg += ": " + response.message();
                    }
                    
                    Toast.makeText(MainActivity.this,
                            errorMsg,
                            Toast.LENGTH_LONG).show();
                    
                    Log.e("MainActivity", "Error respuesta: " + response.code() + 
                          " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Apk>> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "❌ Error de conexión",
                        Toast.LENGTH_LONG).show();
                
                Log.e("MainActivity", "Error de conexión: ", t);
            }
        });
    }

    /*private void manejarClicApk(Apk apk) {
        // Por ahora, solo descargar cuando se hace clic
        // Más adelante puedes separar: clic largo para detalles, clic corto para descargar

        // Descargar APK directamente
        descargarApk(apk);
    }

    private void descargarApk(Apk apk) {
        // Implementar descarga
        Intent intent = new Intent(MainActivity.this, AppDownloadActivity.class);
        intent.putExtra("APK_ID", apk.getId());
        intent.putExtra("APK_NOMBRE", apk.getNombre());
        intent.putExtra("APK_FILENAME", apk.getNombreApk());
        startActivity(intent);
        
        Toast.makeText(this, "Descargando: " + apk.getNombre(), Toast.LENGTH_SHORT).show();
    }*/
}