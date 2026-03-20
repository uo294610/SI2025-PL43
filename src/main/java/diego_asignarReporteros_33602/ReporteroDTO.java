package diego_asignarReporteros_33602;

public class ReporteroDTO {
    private int id;
    private String nombre;
    private String tematica; // NUEVO

    public ReporteroDTO() {}

    public int getId() { return this.id; }
    public String getNombre() { return this.nombre; }
    public String getTematica() { return this.tematica; } // NUEVO

    public void setId(int value) { this.id = value; }
    public void setNombre(String value) { this.nombre = value; }
    public void setTematica(String value) { this.tematica = value; } // NUEVO
}