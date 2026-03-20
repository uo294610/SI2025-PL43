package diego_asignarReporteros_33602;

public class EventoDTO {
    private int id;
    private String nombre;
    private String fecha;
    private String tematica; // NUEVO

    public EventoDTO() {}

    public int getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getFecha() { return this.fecha; }
    public String getTematica() { return this.tematica; } // NUEVO
    
    public void setId(int value) { this.id = value; }
    public void setNombre(String value) { this.nombre = value; }
    public void setFecha(String value) { this.fecha = value; }
    public void setTematica(String value) { this.tematica = value; } // NUEVO
}