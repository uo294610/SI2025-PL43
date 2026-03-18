package adrian_distribuirReportajes_33606;

public class EmpresaAceptadaDTO {
	private String id;
	private String nombreEmpresa;
	private int descargado; 

	public EmpresaAceptadaDTO() {}

	public String getDescargado() {
		return (descargado == 1) ? "SÍ" : "NO";
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombreEmpresa() { return nombreEmpresa; }
	public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
	public void setDescargado(int descargado) { this.descargado = descargado; }
	public int getDescargadoInt() { return descargado; }
}