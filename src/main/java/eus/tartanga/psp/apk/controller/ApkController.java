package eus.tartanga.psp.apk.controller;

import java.util.List;

// CORRECTOS imports de Spring
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eus.tartanga.psp.apk.model.Apk;
import eus.tartanga.psp.apk.service.ApkService;

@RestController
@RequestMapping("/apks")
public class ApkController {

    private final ApkService apkService;

    public ApkController(ApkService apkService) {
        this.apkService = apkService;
    }

    //devolver todas las apks
    @GetMapping
    public ResponseEntity<List<Apk>> obtenerTodasApks() {
        List<Apk> apks = apkService.obtenerApks();
        return ResponseEntity.ok(apks);
    }

    //Descargar una apk en concreto 
    @GetMapping("/descargarAPK/{id}")
    public ResponseEntity<byte[]> descargarApk(@PathVariable Integer id) {
        byte[] apkBytes = apkService.obtenerApkBytes(id);

        if (apkBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDisposition(
                ContentDisposition.attachment()
                    .filename("app_" + id + ".apk")
                    .build()
            );
            headers.setContentLength(apkBytes.length);

            return new ResponseEntity<>(apkBytes, headers, HttpStatus.OK);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    //devuelve la imagen
    @GetMapping("/imagenAPK/{id}")
    public ResponseEntity<byte[]> getImagen(@PathVariable int id) {
        byte[] imageBytes = apkService.obtenerImagenApk(id);
        
        if (imageBytes != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(imageBytes.length);
            
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    
    @GetMapping("/hash/{id}")
    public ResponseEntity<String> obtenerHash(@PathVariable Integer id) {
        String hash = apkService.obtenerHash(id);
        
        if (hash != null && !hash.isEmpty()) {
            return ResponseEntity.ok(hash);
        }
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("APK no encontrada");
    }
    

}