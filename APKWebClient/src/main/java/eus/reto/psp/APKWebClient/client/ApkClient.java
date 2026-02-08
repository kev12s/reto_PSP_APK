package eus.reto.psp.APKWebClient.client;


import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import eus.reto.psp.APKWebClient.model.Apk;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class ApkClient {
    
    private final WebClient webClient;
    private final String baseUrl;
    
    // Constructor con URL base
    public ApkClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.webClient = WebClient.builder()
        		.baseUrl(baseUrl)
                .codecs(configurer -> configurer 
                    .defaultCodecs()
                    .maxInMemorySize(100 * 1024 * 1024)) // para descargar archivos grandes
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
           //   .defaultHeader("Accept", MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .build();
    }
    
    // Obtener todas APK
    public List<Apk> obetenerApks() {
        try {
        	Apk[] apksArray = webClient.get()
                    .uri("/apks")
                    .retrieve()
                    .bodyToMono(Apk[].class)
                    .block();
            
            return Arrays.asList(apksArray != null ? apksArray : new Apk[0]);
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return List.of();
        }
    }
    
    //descarga binaria apk
    public byte[] descargarApk(int id) {
        try {
            System.out.println("Iniciando descarga de APK ID: " + id);
            
            return webClient.get()
                .uri("/apks/descargarAPK/{id}", id)
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .retrieve()
                .bodyToMono(byte[].class)
                .timeout(Duration.ofSeconds(60)) // Aumentar timeout
                .doOnSubscribe(s -> System.out.println("Suscrito a descarga"))
                .doOnSuccess(data -> {
                    if (data != null) {
                        System.out.println("Descarga completada: " + data.length + " bytes");
                    }
                })
                .doOnError(e -> System.err.println("Error durante descarga: " + e.getMessage()))
                .block();
                
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            System.err.println("Error al descargar APK ID " + id + ": " + e.getMessage());
            return null;
        }catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return null;
        }
            
    }
    
    //descarga binaria imagen
    public byte[] descargarImagen(int id) {
        try {
        	byte[] bytes = webClient.get()
                    .uri("/apks/imagenAPK/{id}", id)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
            
            return bytes;
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }
    
    // devuelve hash
    public String obtenerHash(int id) {
    	try {
    		String hash = webClient.get()
    				.uri("/apks/hash/{id}", id)
    				.retrieve()
    				.bodyToMono(String.class)
    				.block();
    		
    		return hash;
    	} catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }
    
    //verificacion del hash
    public boolean verificarHash(int id, byte[] apkBytes) {
        try {
            System.out.println("Verificando hash para APK ID: " + id);
            
            Boolean resultado = webClient.post()
                .uri("/apks/verificarHash/{id}", id)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .bodyValue(apkBytes)
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();
            
            return resultado != null && resultado;
            
        } catch (WebClientResponseException e) {
            System.err.println("Error HTTP " + e.getStatusCode() + " verificando hash: " + 
                              e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado verificando hash: " + e.getMessage());
            return false;
        }
    }
    
    //DESCRIPCION
    //modificar descripcion
    public Apk modificarDescripcion(int id, String descripcion) {
        try {
        	Apk apk = webClient.put()
                    .uri("/apks/descripcion/{id}", id)
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValue(descripcion)
                    .retrieve()
                    .bodyToMono(Apk.class)
                    .block();
             return apk;
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }
    
    //añadir descripcion
    public Apk añadirDescripcion(int id, String descripcion) {
        try {
        	Apk apk = webClient.put()
                    .uri("/apks/descripcion/{id}", id)
                    .contentType(MediaType.TEXT_PLAIN)
                    .bodyValue(descripcion)
                    .retrieve()
                    .bodyToMono(Apk.class)
                    .block();
             return apk;
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return null;
        }
    }
       
    //eliminar descripcion
    public boolean eliminarDescripcion(int id) {
        try {
            Boolean eliminado = webClient.delete()
                .uri("/apks/descripcion/{id}", id)
                .retrieve()
                .bodyToMono(Boolean.class) 
                .block();
            
            return eliminado != null && eliminado;
        }catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return false;
        } catch (Exception e) {
            System.err.println("Error inesperado: " + e.getMessage());
            return false;
        }
    }

}