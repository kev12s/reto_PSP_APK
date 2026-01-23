package com.example.tartangastore.model;

import com.google.gson.annotations.SerializedName;

public class Apk {
    @SerializedName("id")
    private Integer id;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("descripcion")
    private String descripcion;


    @SerializedName("nombreApk")
    private String nombreApk;

    @SerializedName("icono")
    private String icono;

    // Constructor vacío
    public Apk() {}

    // Getters y setters (usa Generate en Android Studio)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getNombreApk() { return nombreApk; }
    public void setNombreApk(String nombreApk) { this.nombreApk = nombreApk; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }
}
