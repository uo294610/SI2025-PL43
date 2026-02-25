package nico_EntregarReportEvento;


/**
 * DTO para mostrar la lista de reporteros disponibles en la vista.
 */
public class ReporteroDisplayDTO {
    private String id;
    private String nombre;

    // Constructor vacío obligatorio para que la utilidad de Base de Datos lo rellene
    public ReporteroDisplayDTO() {
    }

    // Getters
    public String getId() { 
        return this.id; 
    }
    
    public String getNombre() { 
        return this.nombre; 
    }

    // Setters
    public void setId(String value) { 
        this.id = value; 
    }
    
    public void setNombre(String value) { 
        this.nombre = value; 
    }
 // Este método es clave para que el desplegable muestre el nombre
    @Override
    public String toString() {
        return this.nombre;
    }
}
