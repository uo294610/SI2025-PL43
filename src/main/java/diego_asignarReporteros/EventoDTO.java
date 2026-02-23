package diego_asignarReporteros;

public class EventoDTO {
    private int id;
    private String nombre;
    private String fecha;

    public EventoDTO() {}

    public int getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getFecha() { return this.fecha; }
    
    public void setId(int value) { this.id = value; }
    public void setNombre(String value) { this.nombre = value; }
    public void setFecha(String value) { this.fecha = value; }
}
