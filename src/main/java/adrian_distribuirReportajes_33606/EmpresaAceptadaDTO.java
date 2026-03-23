package adrian_distribuirReportajes_33606;

public class EmpresaAceptadaDTO {
	private String id;
	private String nombreEmpresa;
	private int descargadoValor; // Renombramos la variable interna

	public EmpresaAceptadaDTO() {}

	// Get y Set para la Base de Datos (Numérico)
	public int getDescargadoValor() { return descargadoValor; }
	public void setDescargadoValor(int descargadoValor) { this.descargadoValor = descargadoValor; }

	// Get especial solo para mostrar en la tabla visualmente
	public String getDescargadoTexto() {
		return (descargadoValor == 1) ? "SÍ" : "NO";
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombreEmpresa() { return nombreEmpresa; }
	public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
}