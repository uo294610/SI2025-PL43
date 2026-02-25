package nico_ModificaEntrega_33610;

import java.util.List;
import giis.demo.util.Database;
import nico_EntregarReportEvento.EventoResumenDTO;
import nico_EntregarReportEvento.ReporteroDisplayDTO;
import nico_EntregarReportEvento.VersionReportajeEntity;

public class ModificaEntregaModel {
    private Database db = new Database();

    // 1. Reutilizamos la lista de reporteros
    public List<ReporteroDisplayDTO> getListaReporteros() {
        return db.executeQueryPojo(ReporteroDisplayDTO.class, "SELECT id, nombre FROM Reportero");
    }

    // 2. Eventos PENDIENTES (Sin reportaje)
    public List<EventoResumenDTO> getEventosPendientes(int idReportero) {
        String sql = "SELECT e.id, e.nombre, e.fecha FROM Evento e " +
                     "INNER JOIN Asignacion a ON e.id = a.evento_id " +
                     "WHERE a.reportero_id = ? AND e.id NOT IN (SELECT evento_id FROM Reportaje)";
        return db.executeQueryPojo(EventoResumenDTO.class, sql, idReportero);
    }

    // 3. NUEVO: Eventos ENTREGADOS (Con reportaje)
    public List<EventoResumenDTO> getEventosEntregados(int idReportero) {
        String sql = "SELECT e.id, e.nombre, e.fecha FROM Evento e " +
                     "INNER JOIN Asignacion a ON e.id = a.evento_id " +
                     "INNER JOIN Reportaje r ON e.id = r.evento_id " +
                     "WHERE a.reportero_id = ?";
        return db.executeQueryPojo(EventoResumenDTO.class, sql, idReportero);
    }

    // 4. NUEVO: Obtener los datos de la última versión de un reportaje
    public ReportajeEdicionDTO getUltimaVersion(int idEvento) {
        String sql = "SELECT r.id as reportaje_id, r.reportero_entrega_id, r.titulo, v.subtitulo, v.cuerpo " +
                     "FROM Reportaje r " +
                     "INNER JOIN VersionReportaje v ON r.id = v.reportaje_id " +
                     "WHERE r.evento_id = ? " +
                     "ORDER BY v.fecha_hora DESC LIMIT 1"; // Cogemos la versión más reciente
        List<ReportajeEdicionDTO> lista = db.executeQueryPojo(ReportajeEdicionDTO.class, sql, idEvento);
        return lista.isEmpty() ? null : lista.get(0);
    }

    // 5. NUEVO: Insertar una nueva versión (Historial)
    public void insertarNuevaVersion(VersionReportajeEntity version) {
        String sql = "INSERT INTO VersionReportaje (id, reportaje_id, subtitulo, cuerpo, fecha_hora, que_cambio) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        db.executeUpdate(sql, version.getId(), version.getReportaje_id(), version.getSubtitulo(), 
                         version.getCuerpo(), version.getFecha_hora(), version.getQue_cambio());
    }

    public int getUltimoId(String tabla) {
        String sql = "SELECT max(id) FROM " + tabla;
        List<Object[]> result = db.executeQueryArray(sql);
        if (result != null && !result.isEmpty() && result.get(0)[0] != null) {
            return Integer.parseInt(result.get(0)[0].toString());
        }
        return 0;
    }
}