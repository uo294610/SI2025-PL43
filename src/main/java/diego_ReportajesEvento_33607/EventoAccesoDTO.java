package diego_ReportajesEvento_33607;

//DTO para listar los eventos en la tabla
public class EventoAccesoDTO {
 private int id;
 private String nombre;
 private String fecha;

 public EventoAccesoDTO() {}
 public int getId() { return id; }
 public void setId(int id) { this.id = id; }
 public String getNombre() { return nombre; }
 public void setNombre(String nombre) { this.nombre = nombre; }
 public String getFecha() { return fecha; }
 public void setFecha(String fecha) { this.fecha = fecha; }
}

