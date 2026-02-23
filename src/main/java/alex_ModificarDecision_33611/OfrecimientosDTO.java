package alex_ModificarDecision_33611;

/**
 * Objeto para mostrar en la tabla de ofrecimientos (pendientes o decididos).
 */
public class OfrecimientosDTO {
	private String id, evento, agencia, fecha, decision;
    public OfrecimientosDTO() {}
    public String getId() { return id; } public void setId(String id) { this.id = id; }
    public String getEvento() { return evento; } public void setEvento(String evento) { this.evento = evento; }
    public String getAgencia() { return agencia; } public void setAgencia(String agencia) { this.agencia = agencia; }
    public String getFecha() { return fecha; } public void setFecha(String fecha) { this.fecha = fecha; }
    public String getDecision() { return decision; } public void setDecision(String decision) { this.decision = decision; }
}