package nico_RestaurarVersionReport_33612;

import java.util.List;
import giis.demo.util.Database;
import nico_EntregarReportEvento.VersionReportajeEntity;
import nico_EntregarReportEvento.ReporteroDisplayDTO;

public class RestaurarVersionModel {
    private Database db = new Database();

    public List<ReporteroDisplayDTO> getListaReporteros() {
        return db.executeQueryPojo(ReporteroDisplayDTO.class, "SELECT id, nombre FROM Reportero");
    }
    
    // 1. Obtener la información general del reportaje y del evento
    public List<Object[]> getDatosCabeceraReportaje(int idReportaje) {
        String sql = "SELECT e.nombre, r.titulo, r.reportero_entrega_id " +
                     "FROM Reportaje r " +
                     "INNER JOIN Evento e ON r.evento_id = e.id " +
                     "WHERE r.id = ?";
        return db.executeQueryArray(sql, idReportaje);
    }

    // 2. Obtener TODAS las versiones de un reportaje, ordenadas de la más nueva a la más antigua
    public List<VersionDisplayDTO> getVersionesReportaje(int idReportaje) {
        String sql = "SELECT id, fecha_hora, que_cambio, subtitulo, cuerpo " +
                     "FROM VersionReportaje " +
                     "WHERE reportaje_id = ? " +
                     "ORDER BY fecha_hora DESC";
        return db.executeQueryPojo(VersionDisplayDTO.class, sql, idReportaje);
    }

    // 3. Insertar la nueva versión generada al restaurar
    public void insertarVersionRestaurada(VersionReportajeEntity nuevaVersion) {
        String sql = "INSERT INTO VersionReportaje (id, reportaje_id, subtitulo, cuerpo, fecha_hora, que_cambio) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        db.executeUpdate(sql, nuevaVersion.getId(), nuevaVersion.getReportaje_id(), 
                         nuevaVersion.getSubtitulo(), nuevaVersion.getCuerpo(), 
                         nuevaVersion.getFecha_hora(), nuevaVersion.getQue_cambio());
    }

    // 4. Utilidad para saber el próximo ID de la versión
    public int getUltimoId(String tabla) {
        String sql = "SELECT max(id) FROM " + tabla;
        List<Object[]> result = db.executeQueryArray(sql);
        if (result != null && !result.isEmpty() && result.get(0)[0] != null) {
            return Integer.parseInt(result.get(0)[0].toString());
        }
        return 0;
    }
}