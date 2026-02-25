package nico_RestaurarVersionReport_33612;

public class VersionDisplayDTO {
    private int id; 
    private String fecha_hora; 
    private String que_cambio;
    private String subtitulo;
    private String cuerpo;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getFecha_hora() { return fecha_hora; }
    
    // --- ESTA ES LA MAGIA QUE ARREGLA EL PROBLEMA VISUAL ---
    public void setFecha_hora(String fecha_hora) { 
        if (fecha_hora == null) {
            this.fecha_hora = "";
            return;
        }
        
        try {
            // Intentamos ver si el texto es un número gigante (los milisegundos de SQLite)
            long milisegundos = Long.parseLong(fecha_hora);
            
            // Si no da error, es un número. Lo convertimos a fecha bonita:
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.fecha_hora = sdf.format(new java.util.Date(milisegundos));
            
        } catch (NumberFormatException e) {
            // Si salta a este catch, significa que NO era un número gigante,
            // sino que ya venía con formato de fecha desde la base de datos.
            
            // (A veces Java le añade un ".0" a los milisegundos, lo limpiamos)
            if (fecha_hora.endsWith(".0")) {
                this.fecha_hora = fecha_hora.substring(0, fecha_hora.length() - 2);
            } else {
                this.fecha_hora = fecha_hora;
            }
        }
    }
    // ---------------------------------------------------------

    public String getQue_cambio() { return que_cambio; }
    public void setQue_cambio(String que_cambio) { this.que_cambio = que_cambio; }
    
    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String subtitulo) { this.subtitulo = subtitulo; }
    
    public String getCuerpo() { return cuerpo; }
    public void setCuerpo(String cuerpo) { this.cuerpo = cuerpo; }
}
