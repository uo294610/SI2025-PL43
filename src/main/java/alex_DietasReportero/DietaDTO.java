package alex_DietasReportero;

public class DietaDTO {
    private int eventoId;
    private String fecha;
    private String eventoNombre;
    private String ubicacion;
    private int dias;
    
    // Tarifas base (por día)
    private double tarifaManutencion;
    private double tarifaAlojamiento;
    private boolean aplicaAlojamiento;
    
    // Totales calculados
    private double totalManutencion;
    private double totalAlojamiento;
    private double totalApercibir;

    // Getters y Setters
    public int getEventoId() { return eventoId; }
    public void setEventoId(int eventoId) { this.eventoId = eventoId; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getEventoNombre() { return eventoNombre; }
    public void setEventoNombre(String eventoNombre) { this.eventoNombre = eventoNombre; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public int getDias() { return dias; }
    public void setDias(int dias) { this.dias = dias; }
    public double getTarifaManutencion() { return tarifaManutencion; }
    public void setTarifaManutencion(double tarifaManutencion) { this.tarifaManutencion = tarifaManutencion; }
    public double getTarifaAlojamiento() { return tarifaAlojamiento; }
    public void setTarifaAlojamiento(double tarifaAlojamiento) { this.tarifaAlojamiento = tarifaAlojamiento; }
    public boolean isAplicaAlojamiento() { return aplicaAlojamiento; }
    public void setAplicaAlojamiento(boolean aplicaAlojamiento) { this.aplicaAlojamiento = aplicaAlojamiento; }
    public double getTotalManutencion() { return totalManutencion; }
    public void setTotalManutencion(double totalManutencion) { this.totalManutencion = totalManutencion; }
    public double getTotalAlojamiento() { return totalAlojamiento; }
    public void setTotalAlojamiento(double totalAlojamiento) { this.totalAlojamiento = totalAlojamiento; }
    public double getTotalApercibir() { return totalApercibir; }
    public void setTotalApercibir(double totalApercibir) { this.totalApercibir = totalApercibir; }
}