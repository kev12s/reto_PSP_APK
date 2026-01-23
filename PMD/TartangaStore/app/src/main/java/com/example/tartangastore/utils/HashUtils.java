package com.example.tartangastore.utils;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class HashUtils {

    public static String calcularSHA256(File archivo) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(archivo);
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            fis.close();

            byte[] hashBytes = digest.digest();
            StringBuilder hex = new StringBuilder();
            for (byte b : hashBytes) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean verificarHash(File archivo, String hashServidor) {
        String hashLocal = calcularSHA256(archivo);
        return hashLocal != null && hashLocal.equalsIgnoreCase(hashServidor);
    }
}
