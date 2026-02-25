package alex_ModificarDecision_33611;

/**
 * Objeto con el detalle completo del ofrecimiento.
 * Es vital que los nombres de los atributos coincidan con los que pide 
 * SwingUtil en el controlador.
 */
public class OfrecimientoEntity {
    private int id;
    private String evento;
    private String agencia;
    private String fecha;
    private String decision; // El campo que causaba el error
    private boolean acceso;

    public OfrecimientoEntity() {}

    // GETTERS Y SETTERS (Imprescindibles para SwingUtil)
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEvento() { return evento; }
    public void setEvento(String evento) { this.evento = evento; }

    public String getAgencia() { return agencia; }
    public void setAgencia(String agencia) { this.agencia = agencia; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }

    public boolean isAcceso() { return acceso; }
    public void setAcceso(boolean acceso) { this.acceso = acceso; }
}