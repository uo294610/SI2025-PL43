package alex_GestionarOfrecimientos;

/**
 * Objeto para mostrar en la tabla de ofrecimientos (pendientes o decididos).
 */
public class OfrecimientosDTO {
    private int id;
    private String nombreEvento, nombreAgencia, fechaEvento, nombreTematica, decision;
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
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public boolean isAcceso() { return acceso; }
    public void setAcceso(boolean acceso) { this.acceso = acceso; }
}