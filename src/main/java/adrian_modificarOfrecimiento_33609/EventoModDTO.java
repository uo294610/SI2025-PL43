package adrian_modificarOfrecimiento_33609;

public class EventoModDTO {
	private String id;
	private String nombre;
	private String fecha;
	private String reportero;
	private String tematica;   // Nuevo: Para la tabla de arriba
	private int tematicaId;    // Nuevo: Para poder filtrar abajo (oculto)

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
	public int getTematicaId() { return tematicaId; }
	public void setTematicaId(int tematicaId) { this.tematicaId = tematicaId; }
}