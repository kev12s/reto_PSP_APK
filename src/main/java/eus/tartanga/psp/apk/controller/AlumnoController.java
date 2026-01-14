package eus.tartanga.psp.apk.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import eus.tartanga.psp.apk.model.Alumno;
import eus.tartanga.psp.apk.model.Apk;
import eus.tartanga.psp.apk.service.ApkService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/alumnos")
public class AlumnoController {
    
    private final ApkService apkService;
    
    public AlumnoController(ApkService alumnoService) {
        this.alumnoService = alumnoService;
    }
    
    //Obetener todas las APKs
    @GetMapping
    public ResponseEntity<List<Apk>> obtenerTOdasLasApks(){
    	List<Apk> apkList = 
    }
    
    // Obtener todos los alumnos
    @GetMapping
    public ResponseEntity<List<Alumno>> obtenerTodosLosAlumnos() {
        List<Alumno> alumnos = alumnoService.obtenerTodosLosAlumnos();
        return ResponseEntity.ok(alumnos);
    }
    
    // Buscar alumno por ID
    @GetMapping("/{id}")
    public ResponseEntity<Alumno> obtenerAlumnoPorId(@PathVariable Integer id) {
        Alumno alumno = alumnoService.obtenerAlumnoPorId(id);
        
        if (alumno!=null) {
            return ResponseEntity.ok(alumno);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    //Buscar alumnos por curso
    @GetMapping("/curso/{curso}")
    public ResponseEntity<List<Alumno>> obtenerAlumnosPorCurso(@PathVariable String curso) {
        List<Alumno> alumnos = alumnoService.obtenerAlumnosPorCurso(curso);
        
        if (alumnos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(alumnos);
        }
    }
    
    //  Devolver cuántos alumnos hay
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> contarAlumnos() {
        Integer total = alumnoService.contarAlumnos();
        Map<String, Integer> response = new HashMap<>();
        response.put("total", total);
        return ResponseEntity.ok(response);
    }
    
    //  Buscar por nombre parcial
    @GetMapping("/buscar")
    public ResponseEntity<List<Alumno>> buscarAlumnosPorNombre(@RequestParam String nombre) {
        List<Alumno> alumnos = alumnoService.buscarAlumnosPorNombre(nombre);
        
        if (alumnos.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(alumnos);
        }
    }
    
    @PostMapping
    public ResponseEntity<Alumno> crearAlumno(@RequestBody Alumno alumno) {
        Alumno alumnoCreado = alumnoService.crearAlumno(alumno);
        if (alumnoCreado != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(alumnoCreado);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Alumno> actualizarAlumno(@PathVariable Integer id, @RequestBody Alumno alumno) {
        Alumno alumnoActualizado = alumnoService.actualizarAlumno(id, alumno);
        if (alumnoActualizado != null) {
            return ResponseEntity.ok(alumnoActualizado);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAlumno(@PathVariable Integer id) {
        boolean eliminado = alumnoService.eliminarAlumno(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}