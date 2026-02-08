package com.example.tartangastore.api;

import com.example.tartangastore.model.Apk;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import java.util.List;

public interface ApiService {

    // GET /apks
    @GET("apks")
    Call<List<Apk>> obtenerTodasApks();

    // GET /apks/descargarAPK/{id}
    @GET("apks/descargarAPK/{id}")
    @Streaming
    Call<ResponseBody> descargarApk(@Path("id") Integer id);

    // GET /apks/imagenAPK/{id}
    @GET("apks/imagenAPK/{id}")
    Call<ResponseBody> obtenerImagen(@Path("id") Integer id);

    // GET /apks/hash/{id}
    @GET("apks/hash/{id}")
    Call<String> obtenerHash(@Path("id") Integer id);

    // POST /apks/verificarHash/{id}
    @POST("apks/verificarHash/{id}")
    Call<Boolean> verificarHash(@Path("id") Integer id, @Body RequestBody apkBytes);
}
