package eus.tartanga.psp.apk.controller;

import java.util.List;

// CORRECTOS imports de Spring
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ContentDisposition;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

	//APLICACIONES
	//devolver todas las apks
	@GetMapping
	public ResponseEntity<List<Apk>> obtenerTodasApks() {
		
		try {
			List<Apk> apks = apkService.obtenerApks();
			return ResponseEntity.ok(apks);
		}catch(Exception e){
			System.err.println(e.getMessage());
			return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
		}
	}

	//Descargar una apk en concreto 
	@GetMapping("/descargarAPK/{id}")
	public ResponseEntity<byte[]> descargarApk(@PathVariable Integer id) {

		try {
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
		}catch(Exception e){
			System.err.println(e.getMessage());
			return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
		}
	}

	//devuelve la imagen
	@GetMapping("/imagenAPK/{id}")
	public ResponseEntity<byte[]> getImagen(@PathVariable int id) {

		try {
			byte[] imageBytes = apkService.obtenerImagenApk(id);

			if (imageBytes != null) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.IMAGE_PNG);
				headers.setContentLength(imageBytes.length);

				return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		}catch(Exception e){
			System.err.println(e.getMessage());
			return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
		}
	}

	@GetMapping("/hash/{id}")
	public ResponseEntity<String> obtenerHash(@PathVariable Integer id) {

		try{
			String hash = apkService.obtenerHash(id);

			if (hash != null && !hash.isEmpty()) {
				return ResponseEntity.ok(hash);
			}

			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("APK no encontrada");
		}catch(Exception e){
			System.err.println(e.getMessage());
			return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
		}
	}

	//DESCRIPCION
	//Modificar descripcion 
	@PutMapping("/descripcion/{id}")
	public ResponseEntity<Apk> actualizarDescripcion(@PathVariable int id, @RequestBody String descripcion){

		if(descripcion == null) {
			return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)); 
		}

		try {
			Apk apk = apkService.modificarDescripcion(id, descripcion);

			if(apk == null) {
				return (ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
			}

			return ResponseEntity.ok(apk);
		}catch(Exception e){
			System.err.println(e.getMessage());
			return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
		}
	}

	//Añadir descripcion 
	@PostMapping("/descripcion/{id}")
	public ResponseEntity<Apk> añadirDescripcion(@PathVariable int id, @RequestBody String descripcion){

		if(descripcion == null) {
			return (ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null)); 
		}

		try {
			Apk apk = apkService.añadirDescripcion(id, descripcion);

			if(apk == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
			}else {
				return ResponseEntity.status(HttpStatus.CREATED).body(apk);
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
			return (ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
		}
	}

	//Borrar descripcion 
	@DeleteMapping("/descripcion/{id}")
	public ResponseEntity<Boolean> eliminarDescripcion(@PathVariable int id){

		boolean eliminada = apkService.eliminarDescripcion(id);

		if(eliminada){
			return ResponseEntity.status(HttpStatus.OK).body(true);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
			// se podria devolver return ResponseEntity.noContent().build(); 204
		}
	}


}