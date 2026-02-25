package adrian_modificarOfrecimiento_33609;

public class EmpresaModDTO {
	private String id;
	private String nombre;
	private String estado; // Campo 'decision' en la BD
	private int acceso;    // 0 o 1

	public EmpresaModDTO() {}

	// SwingUtil usará esto para la columna "estado"
	public String getEstado() {
		String textoEstado = (estado == null) ? "PENDIENTE" : estado;
		
		if (acceso == 1) {
			return textoEstado + " (ACCESO CONCEDIDO)";
		}
		return textoEstado;
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public void setEstado(String estado) { this.estado = estado; }
	public int getAcceso() { return acceso; }
	public void setAcceso(int acceso) { this.acceso = acceso; }
}