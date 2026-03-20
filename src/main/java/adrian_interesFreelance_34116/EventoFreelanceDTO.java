package adrian_interesFreelance_34116;

public class EventoFreelanceDTO {
	private String id;
	private String nombreEvento;
	private String fecha;
	private String tematica;
	private String miInteres;

	public EventoFreelanceDTO() {}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombreEvento() { return nombreEvento; }
	public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
	public String getFecha() { return fecha; }
	public void setFecha(String fecha) { this.fecha = fecha; }
	public String getTematica() { return tematica; }
	public void setTematica(String tematica) { this.tematica = tematica; }
	public String getMiInteres() { return miInteres; }
	public void setMiInteres(String miInteres) { this.miInteres = miInteres; }
}