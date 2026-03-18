package adrian_distribuirReportajes_33606;

public class EmpresaAceptadaDTO {
	private String id;
	private String nombreEmpresa;
	private int descargado; // 0 para NO, 1 para SÍ

	public EmpresaAceptadaDTO() {}

	// Formateamos para que en la tabla se vea "SÍ" o "NO"
	public String getDescargado() {
		return (descargado == 1) ? "SÍ" : "NO";
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombreEmpresa() { return nombreEmpresa; }
	public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }
	public void setDescargado(int d) { this.descargado = d; }
	public int getDescargadoInt() { return descargado; }
}