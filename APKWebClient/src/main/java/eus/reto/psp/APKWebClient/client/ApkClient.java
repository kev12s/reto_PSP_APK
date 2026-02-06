package eus.reto.psp.APKWebClient.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import eus.reto.psp.APKWebClient.model.Apk;

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
                .codecs(configurer-> configurer 
                		.defaultCodecs()
                		.maxInMemorySize(10*1024*1024))
                .defaultHeader("Accept", MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
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
        	byte[] bytes = webClient.get()
                    .uri("apks/descargarAPK/{id}", id)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
            
            return bytes;
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }
    }
    
    //descarga binaria imagen
    public byte[] descargarImagen(int id) {
        try {
        	byte[] bytes = webClient.get()
                    .uri("apks/imagenAPK/{id}", id)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
            
            return bytes;
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }
    }
    
    // devuelve hash
    public String obtenerHash(int id) {
    	try {
    		String hash = webClient.get()
    				.uri("/hash/{id}", id)
    				.retrieve()
    				.bodyToMono(String.class)
    				.block();
    		
    		return hash;
    	} catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }
    }
    
    //DESCRIPCION
    //modificar descripcion
    public Apk modificarDescripcion(int id, String descripcion) {
        try {
        	Apk apk = webClient.put()
                    .uri("apks/descripcion/{id}", id)
                    .header("Content-Type", MediaType.TEXT_PLAIN_VALUE)
                    .bodyValue(descripcion)
                    .retrieve()
                    .bodyToMono(Apk.class)
                    .block();
             return apk;
        } catch (WebClientResponseException.NotFound e) {
            System.err.println("Apk con ID " + id + " no encontrada");
            return null;
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }
    }
    
    //añadir descripcion
    public Apk añadirDescripcion(int id, String descripcion) {
        try {
        	Apk apk = webClient.post()
                    .uri("apks/descripcion/{id}", id)
                    .bodyValue(descripcion)
                    .retrieve()
                    .bodyToMono(Apk.class)
                    .block();
             return apk;
        } catch (WebClientResponseException.NotFound e) {
            System.err.println("Apk con ID " + id + " no encontrada");
            return null;
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return null;
        }
    }
       
    //eliminar descripcion
    public HttpStatusCode eliminarDescripcion(int id) {
        try {
        	Apk apk = webClient.delete()
                    .uri("apks/descripcion/{id}", id)
                    .retrieve()
                    .bodyToMono(Apk.class)
                    .block();
             return HttpStatusCode.valueOf(200); // no se si se puede
        } catch (WebClientResponseException.NotFound e) {
            System.err.println("Apk con ID " + id + " no encontrada");
            return e.getStatusCode();
        } catch (WebClientResponseException e) {
            System.err.println(e.getStatusCode());
            return e.getStatusCode();
        }
    }

}