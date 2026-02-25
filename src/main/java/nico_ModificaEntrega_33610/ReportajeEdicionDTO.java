package nico_ModificaEntrega_33610;

public class ReportajeEdicionDTO {
    private int reportaje_id;
    private int reportero_entrega_id;
    private String titulo;
    private String subtitulo;
    private String cuerpo;

    // Getters y Setters
    public int getReportaje_id() { return reportaje_id; }
    public void setReportaje_id(int reportaje_id) { this.reportaje_id = reportaje_id; }
    public int getReportero_entrega_id() { return reportero_entrega_id; }
    public void setReportero_entrega_id(int reportero_entrega_id) { this.reportero_entrega_id = reportero_entrega_id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }
    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }
}
