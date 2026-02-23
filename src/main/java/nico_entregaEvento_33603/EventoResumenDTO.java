package nico_entregaEvento_33603;

//DTO para mostrar los eventos pendientes en la tabla de la vista.

public class EventoResumenDTO {
	private int id;
    private String nombre;
    private String fecha;

    // Constructor vacío necesario para el mapeo de la base de datos
    public EventoResumenDTO() {
    }

    public EventoResumenDTO(int id, String nombre, String fecha) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return nombre + " (" + fecha + ")";
    }
}
