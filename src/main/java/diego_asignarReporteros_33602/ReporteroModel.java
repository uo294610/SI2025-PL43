package diego_asignarReporteros_33602;

import java.util.List;
import giis.demo.util.Database;

public class ReporteroModel {
    private Database db = new Database();

    public List<EventoDTO> getEventosSinAsignar(int idAgencia) {
        String sql = "SELECT e.id, e.nombre, e.fecha, t.nombre AS tematica "
                   + "FROM Evento e "
                   + "LEFT JOIN Asignacion a ON e.id = a.evento_id "
                   + "JOIN Tematica t ON e.tematica_id = t.id "
                   + "WHERE e.agencia_id = ? "
                   + "AND a.evento_id IS NULL "
                   + "ORDER BY e.fecha";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    public List<EventoDTO> getEventosConAsignacion(int idAgencia) {
        String sql = "SELECT DISTINCT e.id, e.nombre, e.fecha, t.nombre AS tematica "
                   + "FROM Evento e "
                   + "JOIN Asignacion a ON e.id = a.evento_id "
                   + "JOIN Tematica t ON e.tematica_id = t.id "
                   + "WHERE e.agencia_id = ? "
                   + "ORDER BY e.fecha";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    public List<ReporteroDTO> getReporterosDisponibles(int idAgencia, String fechaEvento, int idEvento, boolean filtrarTematica) {
        String sql = "SELECT r.id, r.nombre, t.nombre AS tematica FROM Reportero r "
                   + "JOIN Tematica t ON r.tematica_id = t.id "
                   + "WHERE r.agencia_id = ? "
                   + "AND r.id NOT IN ("
                   + "    SELECT a.reportero_id FROM Asignacion a "
                   + "    JOIN Evento e ON a.evento_id = e.id "
                   + "    WHERE e.fecha = ?"
                   + ") ";

        if (filtrarTematica) {
            sql += "AND r.tematica_id = (SELECT tematica_id FROM Evento WHERE id = ?) ";
            sql += "ORDER BY r.nombre";
            return db.executeQueryPojo(ReporteroDTO.class, sql, idAgencia, fechaEvento, idEvento);
        } else {
            sql += "ORDER BY r.nombre";
            return db.executeQueryPojo(ReporteroDTO.class, sql, idAgencia, fechaEvento);
        }
    }

    public List<ReporteroDTO> getReporterosAsignados(int idEvento) {
        String sql = "SELECT r.id, r.nombre, t.nombre AS tematica FROM Reportero r "
                   + "JOIN Asignacion a ON r.id = a.reportero_id "
                   + "JOIN Tematica t ON r.tematica_id = t.id "
                   + "WHERE a.evento_id = ? "
                   + "ORDER BY r.nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idEvento);
    }

    public void asignarReportero(int idEvento, int idReportero) {
        String sql = "INSERT INTO Asignacion (evento_id, reportero_id) VALUES (?, ?)";
        db.executeUpdate(sql, idEvento, idReportero);
    }

    public void eliminarAsignacion(int idEvento, int idReportero) {
        String sql = "DELETE FROM Asignacion WHERE evento_id = ? AND reportero_id = ?";
        db.executeUpdate(sql, idEvento, idReportero);
    }
}