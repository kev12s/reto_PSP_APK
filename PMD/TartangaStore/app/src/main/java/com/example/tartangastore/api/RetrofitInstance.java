package com.example.tartangastore.api;

import android.util.Log;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String BASE_URL = "http://192.168.1.95:8080/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            Log.d("RetrofitInstance", "🚀 Creando conexión a: " + BASE_URL);
            
            // Logging DETALLADO
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    if (message.startsWith("-->") || message.startsWith("<--") || 
                        message.contains("ERROR") || message.contains("Exception")) {
                        Log.d("Retrofit", message);
                    }
                }
            });
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            
            // Timeouts más realistas
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Log.d("Retrofit", "➡️ Enviando petición a: " + chain.request().url());
                        long startTime = System.currentTimeMillis();
                        try {
                            okhttp3.Response response = chain.proceed(chain.request());
                            long endTime = System.currentTimeMillis();
                            Log.d("Retrofit", "⬅️ Respuesta recibida en " + (endTime - startTime) + "ms");
                            Log.d("Retrofit", "   Código: " + response.code());
                            return response;
                        } catch (Exception e) {
                            Log.e("Retrofit", "❌ Error en petición: " + e.getMessage());
                            throw e;
                        }
                    })
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}