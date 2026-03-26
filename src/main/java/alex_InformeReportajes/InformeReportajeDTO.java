package alex_InformeReportajes;

public class InformeReportajeDTO {
    private int id;
    private String nombreEvento, fechaEvento, nombreTematica, tituloReportaje;
    private double precio;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getNombreTematica() { return nombreTematica; }
    public void setNombreTematica(String nombreTematica) { this.nombreTematica = nombreTematica; }
    public String getTituloReportaje() { return tituloReportaje; }
    public void setTituloReportaje(String tituloReportaje) { this.tituloReportaje = tituloReportaje; }
}
