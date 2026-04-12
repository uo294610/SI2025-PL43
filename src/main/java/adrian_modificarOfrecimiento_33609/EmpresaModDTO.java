package adrian_modificarOfrecimiento_33609;

public class EmpresaModDTO {
	private String id;
	private String nombre;
	private String estado; 
	private int acceso;    
	private String especialidad; 
	private String aceptaEmbargos; // NUEVO SPRINT 3

	public EmpresaModDTO() {}

	public String getEstado() {
		String textoEstado = (estado == null) ? "PENDIENTE" : estado;
		if (acceso == 1) { return textoEstado + " (ACCESO CONCEDIDO)"; }
		return textoEstado;
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public void setEstado(String estado) { this.estado = estado; }
	public int getAcceso() { return acceso; }
	public void setAcceso(int acceso) { this.acceso = acceso; }
	public String getEspecialidad() { return especialidad; }
	public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
	public String getAceptaEmbargos() { return aceptaEmbargos; }
	public void setAceptaEmbargos(String aceptaEmbargos) { this.aceptaEmbargos = aceptaEmbargos; }
}