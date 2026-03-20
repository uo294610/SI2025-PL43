package revisionOtrosReportajes;

public class ReportajeRevisionDTO {
    private int id_revision;
    private int id_reportaje;
    private String titulo_reportaje;
    private String nombre_evento; 
    private String autor_nombre;
    private String estado_revision;

    public int getId_revision() { return id_revision; }
    public void setId_revision(int id_revision) { this.id_revision = id_revision; }
    
    public int getId_reportaje() { return id_reportaje; }
    public void setId_reportaje(int id_reportaje) { this.id_reportaje = id_reportaje; }
    
    public String getTitulo_reportaje() { return titulo_reportaje; }
    public void setTitulo_reportaje(String titulo_reportaje) { this.titulo_reportaje = titulo_reportaje; }
    
    public String getNombre_evento() { return nombre_evento; }
    public void setNombre_evento(String nombre_evento) { this.nombre_evento = nombre_evento; }
    
    public String getAutor_nombre() { return autor_nombre; }
    public void setAutor_nombre(String autor_nombre) { this.autor_nombre = autor_nombre; }
    
    public String getEstado_revision() { return estado_revision; }
    public void setEstado_revision(String estado_revision) { this.estado_revision = estado_revision; }
}