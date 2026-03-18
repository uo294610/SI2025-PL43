package alex_GestionarOfrecimientos;

/**
 * Objeto con el detalle completo del ofrecimiento.
 * Es vital que los nombres de los atributos coincidan con los que pide 
 * SwingUtil en el controlador.
 */

public class OfrecimientoEntity {
    private int id;
    private int evento_id;
    private int empresa_id;
    private String decision;
    private boolean acceso;
    
    // GETTERS O SETTERS
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getEvento_id() { return evento_id; }
    public void setEvento_id(int evento_id) { this.evento_id = evento_id; }
    public int getEmpresa_id() { return empresa_id; }
    public void setEmpresa_id(int empresa_id) { this.empresa_id = empresa_id; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public boolean isAcceso() { return acceso; }
    public void setAcceso(boolean acceso) { this.acceso = acceso; }
}