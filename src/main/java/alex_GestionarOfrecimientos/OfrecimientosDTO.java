package alex_GestionarOfrecimientos;

public class OfrecimientosDTO {
    private int id;
    private String nombreEvento, nombreAgencia, fechaEvento, nombreTematica, decision, tipoAcceso, estadoPago, fechaFinEmbargo, tituloReportaje;
    private double precio;
    private boolean acceso;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombreEvento() { return nombreEvento; }
    public void setNombreEvento(String nombreEvento) { this.nombreEvento = nombreEvento; }
    public String getNombreAgencia() { return nombreAgencia; }
    public void setNombreAgencia(String nombreAgencia) { this.nombreAgencia = nombreAgencia; }
    public String getFechaEvento() { return fechaEvento; }
    public void setFechaEvento(String fechaEvento) { this.fechaEvento = fechaEvento; }
    public String getNombreTematica() { return nombreTematica; }
    public void setNombreTematica(String nombreTematica) { this.nombreTematica = nombreTematica; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public boolean isAcceso() { return acceso; }
    public void setAcceso(boolean acceso) { this.acceso = acceso; }
    public String getTipoAcceso() { return tipoAcceso; }
    public void setTipoAcceso(String tipoAcceso) { this.tipoAcceso = tipoAcceso; }
    public String getEstadoPago() { return estadoPago; }
    public void setEstadoPago(String estadoPago) { this.estadoPago = estadoPago; }
    public String getFechaFinEmbargo() { return fechaFinEmbargo; }
    public void setFechaFinEmbargo(String fechaFinEmbargo) { this.fechaFinEmbargo = fechaFinEmbargo; }
    public String getTituloReportaje() { return tituloReportaje; }
    public void setTituloReportaje(String tituloReportaje) { this.tituloReportaje = tituloReportaje; }

}