package adrian_distribuirReportajes_33606;

public class EventoEntregadoDTO {
	private String id;
	private String nombreEvento;
	private String estado; // Para cumplir el prototipo

	public EventoEntregadoDTO() {}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombreEvento() { return nombreEvento; }
	public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }
}