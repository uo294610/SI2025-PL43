package nico_EntregarReportEvento;


import java.util.List;
import giis.demo.util.Database;

/**
 * Modelo para la gestión de entrega de reportajes.
 * Implementa la lógica de persistencia para la HU #33603.
 */
public class EntregaReportajeModel {
    private Database db = new Database();

    /**
     * Obtiene los eventos asignados a un reportero que aún NO tienen reportaje.
     * SQL basado en la relación entre Evento, Asignacion y Reportaje.
     */
    public List<EventoResumenDTO> getEventosPendientes(int idReportero) {
        String sql = "SELECT e.id, e.nombre, e.fecha " +
                     "FROM Evento e " +
                     "INNER JOIN Asignacion a ON e.id = a.evento_id " +
                     "WHERE a.reportero_id = ? " +
                     "AND e.id NOT IN (SELECT evento_id FROM Reportaje)";
        return db.executeQueryPojo(EventoResumenDTO.class, sql, idReportero);
    }
    public List<ReporteroDisplayDTO> getListaReporteros() {
        String sql = "SELECT id, nombre FROM Reportero";
        return db.executeQueryPojo(ReporteroDisplayDTO.class, sql);
    }

    /**
     * Comprueba si un título ya existe en el sistema.
     * Utiliza executeQueryArray para evitar el error de método no definido.
     */
    public boolean existeTitulo(String titulo) {
        String sql = "SELECT count(*) FROM Reportaje WHERE titulo = ?";
        List<Object[]> result = db.executeQueryArray(sql, titulo);
        
        if (result != null && !result.isEmpty() && result.get(0)[0] != null) {
            long count = Long.parseLong(result.get(0)[0].toString());
            return count > 0;
        }
        return false;
    }

    /**
     * Inserta la cabecera del reportaje en la tabla 'Reportaje'.
     */
    public void insertarReportaje(ReportajeEntity reportaje) {
        String sql = "INSERT INTO Reportaje (id, titulo, evento_id, reportero_entrega_id) VALUES (?, ?, ?, ?)";
        db.executeUpdate(sql, reportaje.getId(), reportaje.getTitulo(), 
                         reportaje.getEvento_id(), reportaje.getReportero_entrega_id());
    }

    /**
     * Inserta el contenido inicial en la tabla 'VersionReportaje'.
     */
    public void insertarVersion(VersionReportajeEntity version) {
        String sql = "INSERT INTO VersionReportaje (id, reportaje_id, subtitulo, cuerpo, fecha_hora, que_cambio) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        db.executeUpdate(sql, version.getId(), version.getReportaje_id(), 
                         version.getSubtitulo(), version.getCuerpo(), 
                         version.getFecha_hora(), version.getQue_cambio());
    }
    
    /**
     * Obtiene el valor máximo de ID para gestionar el autoincrementado manual.
     */
    public int getUltimoId(String tabla) {
        String sql = "SELECT max(id) FROM " + tabla;
        List<Object[]> result = db.executeQueryArray(sql);
        if (result != null && !result.isEmpty() && result.get(0)[0] != null) {
            return Integer.parseInt(result.get(0)[0].toString());
        }
        return 0;
    }
}
