package diego_asignarReporteros_33602;

public class ReporteroDTO {
    private int id;
    private String nombre;
    private String tematica; 
    private String tipo; // NUEVO

    public ReporteroDTO() {}

    public int getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getTematica() { return this.tematica; } 
    public String getTipo() { return this.tipo; } // NUEVO

    public void setId(int value) { this.id = value; }
    public void setNombre(String value) { this.nombre = value; }
    public void setTematica(String value) { this.tematica = value; } 
    public void setTipo(String value) { this.tipo = value; } // NUEVO
}