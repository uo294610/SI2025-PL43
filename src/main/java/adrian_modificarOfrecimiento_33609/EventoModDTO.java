package adrian_modificarOfrecimiento_33609;

public class EventoModDTO {
	private String id;
	private String nombre;
	private String fecha;
	private String reportero;
	private String tematica;   
	private String embargo; // NUEVO SPRINT 3

	public EventoModDTO() {}
	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public String getFecha() { return fecha; }
	public void setFecha(String fecha) { this.fecha = fecha; }
	public String getReportero() { return reportero; }
	public void setReportero(String reportero) { this.reportero = reportero; }
	public String getTematica() { return tematica; }
	public void setTematica(String tematica) { this.tematica = tematica; }
	public String getEmbargo() { return embargo; }
	public void setEmbargo(String embargo) { this.embargo = embargo; }
}