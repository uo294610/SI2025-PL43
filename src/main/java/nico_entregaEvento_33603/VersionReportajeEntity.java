package nico_entregaEvento_33603;
import java.sql.Timestamp;

//Entidad que mapea la tabla 'VersionReportaje'.

public class VersionReportajeEntity {
	private int id;
    private int reportaje_id;
    private String subtitulo;
    private String cuerpo;
    private Timestamp fecha_hora;
    private String que_cambio; // Para la primera entrega será "Versión inicial"

    public VersionReportajeEntity() {
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReportaje_id() {
        return reportaje_id;
    }

    public void setReportaje_id(int reportaje_id) {
        this.reportaje_id = reportaje_id;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public Timestamp getFecha_hora() {
        return fecha_hora;
    }

    public void setFecha_hora(Timestamp fecha_hora) {
        this.fecha_hora = fecha_hora;
    }

    public String getQue_cambio() {
        return que_cambio;
    }

    public void setQue_cambio(String que_cambio) {
        this.que_cambio = que_cambio;
    }
}
