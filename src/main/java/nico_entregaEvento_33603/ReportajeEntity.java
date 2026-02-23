package nico_entregaEvento_33603;

// Entidad que mapea la tabla 'Reportaje'.

public class ReportajeEntity {
	private int id;
    private String titulo;
    private int evento_id;
    private int reportero_entrega_id;

    public ReportajeEntity() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getEvento_id() {
        return evento_id;
    }

    public void setEvento_id(int evento_id) {
        this.evento_id = evento_id;
    }

    public int getReportero_entrega_id() {
        return reportero_entrega_id;
    }

    public void setReportero_entrega_id(int reportero_entrega_id) {
        this.reportero_entrega_id = reportero_entrega_id;
    }
}
