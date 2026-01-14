package eus.tartanga.psp.apk.model;

public class Apk {
    private Integer id;
    private String nombre;
    private String descripcion;
    private String pathImg;
    private String pathApk;
    
    // Constructor vacío necesario para ObjectMapper
    public Apk() {}

	public Apk(Integer id, String nombre, String descripcion, String pathImg, String pathApk) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.pathImg = pathImg;
		this.pathApk = pathApk;
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

	public String getPathImg() {
		return pathImg;
	}

	public void setPathImg(String pathImg) {
		this.pathImg = pathImg;
	}

	public String getPathApk() {
		return pathApk;
	}

	public void setPathApk(String pathApk) {
		this.pathApk = pathApk;
	}

	@Override
	public String toString() {
		return "Apk [id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", pathImg=" + pathImg
				+ ", pathApk=" + pathApk + "]";
	}
    
    
    

}