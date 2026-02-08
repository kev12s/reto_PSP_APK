package eus.tartanga.psp.apk.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eus.tartanga.psp.apk.model.Apk;
import jakarta.annotation.PostConstruct;

@Service
public class ApkService {

	private List<Apk> listaApk = new ArrayList<>();
	private final ObjectMapper objectMapper = new ObjectMapper();

	@PostConstruct
	public void init() {
		cargarApks();
	}

	private void cargarApks () {
		try {
			File file = ResourceUtils.getFile(getClass().getResource("/apk.json"));
			System.out.println("primer json" + file);
			listaApk = objectMapper.readValue(file, new TypeReference<List<Apk>>() {});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//1º PARTE APLICACIONES
	//devuelve todas las apk
	public List<Apk> obtenerApks() {
		return listaApk;
	}


	//devuelve apk concreta por ID
	private Apk obtenerApkPorId(Integer id) {

		for(Apk apk:listaApk) {
			if(apk.getId()==id) {
				return apk;
			}
		}

		return null;
	}

	//devuelve los bytes para descargar la APK
	public byte[] obtenerApkBytes(int id) {
		try {
			Apk apk = obtenerApkPorId(id);
			if (apk == null) {
				return null;
			}

			//usa el nombre de la apk buscada para acceder a la carpeta y buscar la APK
			File apkFile = ResourceUtils.getFile(getClass().getResource("/apks/" + apk.getNombreApk())); 

			// Leer bytes para descargar
			FileInputStream fis = new FileInputStream(apkFile);
			byte[] bytes = new byte[(int) apkFile.length()];
			fis.read(bytes);
			fis.close();

			return bytes;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//lee los bytes para descargar la imagen
	public byte[] obtenerImagenApk(int id) {
		try {
			Apk apk = obtenerApkPorId(id);
			if (apk == null) {
				return null;
			}

			File imagenFile = ResourceUtils.getFile(getClass().getResource("/iconos/" + apk.getIcono())); //carga la imagen 

			FileInputStream fis = new FileInputStream(imagenFile);
			byte[] bytes = new byte[(int) imagenFile.length()];
			fis.read(bytes);
			fis.close();

			return bytes;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	//calcular el has para comparar con el cliente cuando lo pida
	public String obtenerHash(Integer id) {
		try {

			Apk apk = obtenerApkPorId(id);
			if (apk == null) {
				System.out.println("APK con ID " + id + " no encontrada en JSON");
				return null;
			}

			String nombreApk = apk.getNombreApk(); 

			// Buscar el archivo en resources/apks/
			File apkFile = ResourceUtils.getFile(getClass().getResource("/apks/" + nombreApk));

			if (!apkFile.exists()) {
				System.out.println("Archivo APK no encontrado: /apks/" + nombreApk);
				return null;
			}

			// Calcular hash SHA-256
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			FileInputStream fis = new FileInputStream(apkFile);

			byte[] buffer = new byte[8192];
			int bytesRead;

			while ((bytesRead = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, bytesRead);
			}
			fis.close();

			// Convertir a hexadecimal
			byte[] hashBytes = digest.digest();
			StringBuilder hexString = new StringBuilder();

			for (byte b : hashBytes) {
				String hex = Integer.toHexString(0xff & b);
				if (hex.length() == 1) {
					hexString.append('0');
				}
				hexString.append(hex);
			}

			String hash = hexString.toString();
			System.out.println("Hash para " + nombreApk + ": " + 
					hash.substring(0, 8) + "...");

			return hash;

		} catch (Exception e) {
			System.err.println("Error calculando hash: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	//calcular hash pero de bytes (para la aplicacion enviada por el cliente)
	public String calcularHash(byte[] bytes) throws NoSuchAlgorithmException {
	    if (bytes == null || bytes.length == 0) {
	        return null;
	    }
	    
	    MessageDigest digest = MessageDigest.getInstance("SHA-256");
	    byte[] hashBytes = digest.digest(bytes);
	    
	    StringBuilder hexString = new StringBuilder();
	    for (byte b : hashBytes) {
	        String hex = Integer.toHexString(0xff & b);
	        if (hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    
	    return hexString.toString();
	}

	//2º PARTE DESCRIPCION
	//Modificar descripcion, devuelvo la app entera para practicar
	public Apk modificarDescripcion(int id, String descripcion){
		try {
			Apk apk = obtenerApkPorId(id);

			if(apk == null) {
				System.out.println("Apk no encontrada");
				return null;
			}

			apk.setDescripcion(descripcion);
			actualizarJson();

			System.out.println("Descripcion actualizada");

			return apk;

		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	//Añadir descripcion 
	public Apk añadirDescripcion(int id, String descripcion) {
		try {
			Apk apk = obtenerApkPorId(id);

			if(apk == null) {
				System.out.println("Apk no encontrada");
				return null;
			}

			apk.setDescripcion(descripcion);
			actualizarJson();
			
			System.err.println("Descripcion añadida correctamente");

			return apk;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	//Eliminar descripcion
	public boolean eliminarDescripcion(int id){
		try {
			Apk apk = obtenerApkPorId(id);
			
			if(apk == null) {
				System.out.println("Apk no encontrada");
				return false;
			}

			apk.setDescripcion("");
			actualizarJson();

			System.out.println("Descripcion eliminada correctamente");

			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//escribir de nuevo en el json con la descripcion nueva
	private void actualizarJson() {
				
		try {
			//sacar el json
			/*File file = ResourceUtils.getFile(getClass().getResource("/apk.json"));
			System.out.println("original: " + file);*/
			
			//convertir la listaApk a formato json
			String jsonActualizado = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(listaApk);
			
			System.out.println("actualizado: " + jsonActualizado);
			
			FileWriter writer = new FileWriter("src/main/resources/apk.json");
			writer.write(jsonActualizado);
			writer.close();
			
			System.out.println("json actualizado");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


}
