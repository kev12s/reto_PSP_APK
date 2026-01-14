package eus.tartanga.psp.apk.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import eus.tartanga.psp.apk.model.Alumno;
import jakarta.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AlumnoService {
    
    private List<Alumno> alumnos = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @PostConstruct
    public void init() {
        cargarAlumnosDesdeJSON();
    }
    
    private void cargarAlumnosDesdeJSON() {
        try {
            File file = ResourceUtils.getFile(getClass().getResource("/alumnos.json"));
            alumnos = objectMapper.readValue(file, new TypeReference<List<Alumno>>() {});
            System.out.println("JSON cargado correctamente. Total alumnos: " + alumnos.size());
        } catch (IOException e) {
            System.err.println("Error al cargar el archivo JSON: " + e.getMessage());
            alumnos = new ArrayList<>();
        }
    }
    
    // Obtener todos los alumnos
    public List<Alumno> obtenerTodosLosAlumnos() {
        return new ArrayList<>(alumnos);
    }
    
    // Buscar alumno por ID
    public Alumno obtenerAlumnoPorId(Integer id) {
        for (Alumno alumno : alumnos) {
            if (alumno.getId().equals(id)) {
                return alumno;
            }
        }
        
        return null;
    }
    
    // Buscar alumnos por curso
    public List<Alumno> obtenerAlumnosPorCurso(String curso) {
        List<Alumno> resultado = new ArrayList<>();
        
        for (Alumno alumno : alumnos) {
            if (alumno.getCurso().equalsIgnoreCase(curso)) {
                resultado.add(alumno);
            }
        }
        
        return resultado;
    }
    
    // Contar total de alumnos
    public Integer contarAlumnos() {
        return alumnos.size();
    }
    
    // Buscar por nombre  
    public List<Alumno> buscarAlumnosPorNombre(String nombre) {
        List<Alumno> resultado = new ArrayList<>();
        String nombreBuscado = nombre.toLowerCase();
        
        for (Alumno alumno : alumnos) {
            String nombreAlumno = alumno.getNombre().toLowerCase();
            if (nombreAlumno.contains(nombreBuscado)) {
                resultado.add(alumno);
            }
        }
        
        return resultado;
    }
    
    public Alumno crearAlumno(Alumno alumno) {
        if (alumno == null) {
            return null;
        }
        
        int nuevoId = obtenerSiguienteId();
        alumno.setId(nuevoId);
        alumnos.add(alumno);
        return alumno;
    }

    private int obtenerSiguienteId() {
        int maxId = 0;
        for (Alumno alumno : alumnos) {
            if (alumno.getId() > maxId) {
                maxId = alumno.getId();
            }
        }
        return maxId + 1;
    }

    public Alumno actualizarAlumno(Integer id, Alumno alumnoActualizado) {
        for (int i = 0; i < alumnos.size(); i++) {
            Alumno alumno = alumnos.get(i);
            if (alumno.getId().equals(id)) {
                alumnoActualizado.setId(id);
                alumnos.set(i, alumnoActualizado);
                return alumnoActualizado;
            }
        }
        return null;
    }

    public boolean eliminarAlumno(Integer id) {
        for (int i = 0; i < alumnos.size(); i++) {
            Alumno alumno = alumnos.get(i);
            if (alumno.getId().equals(id)) {
                alumnos.remove(i);
                return true;
            }
        }
        return false;
    }
}