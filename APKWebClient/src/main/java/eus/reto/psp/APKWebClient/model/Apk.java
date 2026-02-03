package eus.reto.psp.APKWebClient.model;

public class Apk {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String icono;
    private String nombreApk;
    
    // Constructor vacío necesario para ObjectMapper
    public Apk() {}

	public Apk(Integer id, String nombre, String descripcion, String nombreImg, String nombreApk) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.icono = nombreImg;
		this.nombreApk = nombreApk;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public String getNombreApk() {
		return nombreApk;
	}

	public void setNombreApk(String nombreApk) {
		this.nombreApk = nombreApk;
	}

	@Override
	public String toString() {
		return "Apk [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", icono=" + icono
				+ ", nombreApk=" + nombreApk + "]";
	}
    
    
    

}