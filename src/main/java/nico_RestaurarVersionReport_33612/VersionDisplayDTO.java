package nico_RestaurarVersionReport_33612;


public class VersionDisplayDTO {
    private int id; // ID de la versión
    private String fecha_hora; 
    private String que_cambio;
    private String subtitulo;
    private String cuerpo;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getFecha_hora() { return fecha_hora; }
    public void setFecha_hora(String fecha_hora) { this.fecha_hora = fecha_hora; }
    
    public String getQue_cambio() { return que_cambio; }
    public void setQue_cambio(String que_cambio) { this.que_cambio = que_cambio; }
    
    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }
    
    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }
}
