package com.example.tartangastore;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.tartangastore.api.ApiService;
import com.example.tartangastore.api.RetrofitInstance;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppDownloadActivity extends AppCompatActivity {

    private ApiService apiService;
    private Button btnHome;
    private Button btnSettings;
    private Button btnDownload;
    private ImageView imgAppIcon;
    private Button btnCerrar;
    private Button btnAdvice;
    private Button btnOpenGallery;
    private TextView txtAppDescription;
    private TextView txtAppName;
    private static final String BASE_IMAGE_URL = "http://192.168.1.95:8080/apks/imagenAPK/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ThemeHelper.applyTheme(this);
        setContentView(R.layout.activity_app_download);

        apiService = RetrofitInstance.getApiService();

        // --- AÑADE ESTO: Obtener datos de la APK ---
        Intent intent = getIntent();
        int apkId = intent.getIntExtra("APK_ID", -1);
        String apkName = intent.getStringExtra("APK_NOMBRE");
        String apkDescription = intent.getStringExtra("APK_DESCRIPCION");
        String apkFileName = intent.getStringExtra("APK_FILENAME");

        Log.d("AppDownloadActivity",
                "Datos recibidos - ID: " + apkId +
                        ", Nombre: " + apkName +
                        ", Desc: " + apkDescription);
        btnOpenGallery = findViewById(R.id.btnOpenGallery);
        btnHome = findViewById(R.id.btnExitAdvise);
        btnSettings = findViewById(R.id.btnSettings);
        btnDownload = findViewById(R.id.button4);  // Tu botón de descarga
        btnCerrar = findViewById(R.id.btnCerrar);
        imgAppIcon = findViewById(R.id.imageView3); // Usando tu imageView3 del XML
        btnAdvice = findViewById(R.id.btnAdvice);

        txtAppName = findViewById(R.id.textViewAppName);
        txtAppDescription = findViewById(R.id.textViewAppDescription);
        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AppDownloadActivity.this, ImageViewerActivity.class);
                startActivity(intent);
            }
        });
        //configurar lo que se muestra en la tarjeta
        if (apkId != -1) {
            // Cargar imagen de la APK
            String imageUrl = BASE_IMAGE_URL + apkId;
            Log.d("AppDownloadActivity", "Cargando imagen desde: " + imageUrl);

            Glide.with(this)
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(imgAppIcon);

            // Mostrar nombre y descripción
            if (txtAppName != null && apkName != null) {
                txtAppName.setText(apkName);
            }
            if (txtAppDescription != null && apkDescription != null) {
               txtAppDescription.setText(apkDescription);
             }
        }

        //boton de descarga
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica de descarga
                if (apkId != -1) {
                    Log.d("AppDownloadActivity",
                            "Iniciando descarga - ID: " + apkId +
                                    ", Archivo: " + apkFileName);

                    iniciarDescarga(apkId, apkName,apkFileName);
                }
            }
        });

        btnAdvice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir MainActivity
                Intent intent = new Intent(AppDownloadActivity.this, AdviseActivity.class);
                startActivity(intent);
            }
        });
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir MainActivity
                Intent intent = new Intent(AppDownloadActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir MainActivity
                Intent intent = new Intent(AppDownloadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarDialogoConfirmacion();
            }
        });
    }

    private void iniciarDescarga(int apkId, String apkName, String apkFileName) {
        Log.d("AppDownload", "Iniciando descarga de APK ID: " + apkId);
        mostrarMensaje("Descargando " + apkName + "...");

        // PASO 1: Descargar APK (equivalente a tu método descargarApk())
        descargarAPKBytes(apkId, apkName, apkFileName);
    }

    private void descargarAPKBytes(int apkId, String apkName, String apkFileName) {
        Call<ResponseBody> call = apiService.descargarApk(apkId);

        Log.d("AppDownload", "Suscrito a descarga - ID: " + apkId);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        // Leer todos los bytes del ResponseBody
                        byte[] apkBytes = leerBytesDeResponseBody(response.body());

                        if (apkBytes != null && apkBytes.length > 0) {
                            Log.d("AppDownload", "Descarga completada: " + apkBytes.length + " bytes");
                            mostrarMensaje("APK: (" + apkBytes.length + " bytes). Verificando hash...");

                            // PASO 2: Verificar hash con el servidor
                            verificarHashConServidor(apkId, apkBytes, apkFileName);

                        } else {
                            mostrarMensaje("ERROR: No se recibieron datos del servidor");
                            Log.e("AppDownload", "APK bytes nulos o vacíos");
                        }

                    } catch (IOException e) {
                        mostrarMensaje("Error al procesar la APK");
                        Log.e("AppDownload", "Error leyendo bytes: " + e.getMessage());
                    }

                } else {
                    String errorMsg = "Error " + response.code();
                    if (response.errorBody() != null) {
                        try {
                            errorMsg += ": " + response.errorBody().string();
                        } catch (IOException e) {
                            errorMsg += ": " + response.message();
                        }
                    }
                    mostrarMensaje(errorMsg);
                    Log.e("AppDownload", "Error respuesta: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                mostrarMensaje("Error de conexión: " + t.getMessage());
                Log.e("AppDownload", "Error durante descarga: " + t.getMessage());
            }
        });
    }

    private byte[] leerBytesDeResponseBody(ResponseBody body) throws IOException {
        try (InputStream inputStream = body.byteStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            long totalBytes = 0;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }

            outputStream.flush();
            byte[] result = outputStream.toByteArray();

            Log.d("HashDebug", "Bytes leídos: " + totalBytes + ", array size: " + result.length);
            return result;
        }
    }
    private void verificarHashConServidor(int apkId, byte[] apkBytes, String apkFileName) {
        Log.d("AppDownload", "Verificando hash para APK ID: " + apkId);
        Log.d("AppDownload", "Bytes a verificar: " + apkBytes.length);

        // --- CAMBIO CLAVE: Envolver los bytes en un RequestBody ---
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), apkBytes);

        // Enviar el RequestBody al servidor
        Call<Boolean> call = apiService.verificarHash(apkId, requestBody);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean hashValido = response.body();

                    Log.d("AppDownload", "Resultado verificación: " + (hashValido ? "COINCIDEN" : "NO COINCIDEN"));

                    if (hashValido) {
                        mostrarMensaje("Hash verificado correctamente. Guardando APK...");

                        // PASO 3: Guardar APK localmente
                        guardarAPKLocalmente(apkBytes, apkFileName);

                    } else {
                        mostrarMensaje("ERROR: El hash no coincide. La APK podría estar corrupta.");
                        Log.e("AppDownload", "Hash no coincide para APK ID: " + apkId);
                    }

                } else {
                    mostrarMensaje("Error en verificación: " + response.code());
                    Log.e("AppDownload", "Error verificación hash: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                mostrarMensaje("Error al verificar hash: " + t.getMessage());
                Log.e("AppDownload", "Error conexión verificación: " + t.getMessage());
            }
        });
    }

    private void guardarAPKLocalmente(byte[] apkBytes, String apkFileName) {
        try {
            // Crear carpeta "descargas" si no existe
            File downloadsDir = new File(getExternalFilesDir(null), "descargas");
            if (!downloadsDir.exists()) {
                boolean creado = downloadsDir.mkdirs();
                Log.d("AppDownload", "Directorio creado: " + creado);
            }

            // Crear archivo
            String nombreArchivo;
            if (apkFileName != null && !apkFileName.isEmpty()) {
                nombreArchivo = apkFileName;
            } else {
                nombreArchivo = "app_" + System.currentTimeMillis() + ".apk";
            }

            File apkFile = new File(downloadsDir, nombreArchivo);

            // Guardar bytes
            try (FileOutputStream fos = new FileOutputStream(apkFile)) {
                fos.write(apkBytes);

                String ruta = apkFile.getAbsolutePath();
                // CORREGIDO: Ahora el Toast muestra la ruta completa y correcta
                mostrarMensaje("APK guardada en: " + ruta);

                Log.d("AppDownload", "Guardado: " + ruta);
                Log.d("AppDownload", "Tamaño: " + apkBytes.length + " bytes");

                // El Toast de confirmación ya se muestra en mostrarMensaje(),
                // así que el bloque de abajo era redundante.
            }

        } catch (IOException ex) {
            mostrarMensaje("Error al guardar archivo");
            Log.e("AppDownload", "Error al guardar archivo: " + ex.getMessage());
        } catch (Exception ex) {
            mostrarMensaje("Error: " + ex.getMessage());
            Log.e("AppDownload", "Error inesperado: " + ex.getMessage());
        }
    }

    private void mostrarMensaje(final String mensaje) {
        runOnUiThread(() -> {
            // Toast simple y claro
            Toast.makeText(AppDownloadActivity.this, mensaje, Toast.LENGTH_LONG).show();

            // Log para debugging
            Log.d("AppDownload", mensaje);

            // Opcional: También mostrar en consola de Android Studio
            System.out.println("AppDownload: " + mensaje);
        });
    }

    private void mostrarDialogoConfirmacion() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.closeApp)
                .setMessage(R.string.really)
                .setPositiveButton(R.string.affirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                })
                .setNegativeButton(R.string.Negate, null)
                .show();
    }
}
