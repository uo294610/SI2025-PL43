package diego_asignarReporteros;

public class EventoDisplayDTO {
    private String id;
    private String nombre;
    private String fecha;

    public EventoDisplayDTO() {}

    public String getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getFecha() { return this.fecha; }

    public void setId(String value) { this.id = value; }
    public void setNombre(String value) { this.nombre = value; }
    public void setFecha(String value) { this.fecha = value; }
}
