package diego_asignarReporteros_33602;

import java.util.List;
import java.util.ArrayList; // NUEVO IMPORT
import giis.demo.util.Database;

public class ReporteroModel {
    private Database db = new Database();

    public List<EventoDTO> getEventosSinAsignar(int idAgencia) {
        String sql = "SELECT e.id, e.nombre, e.fecha, GROUP_CONCAT(t.nombre, ', ') AS tematica "
                   + "FROM Evento e "
                   + "LEFT JOIN Asignacion a ON e.id = a.evento_id "
                   + "LEFT JOIN EventoTematica et ON e.id = et.evento_id "
                   + "LEFT JOIN Tematica t ON et.tematica_id = t.id "
                   + "WHERE e.agencia_id = ? AND a.evento_id IS NULL "
                   + "GROUP BY e.id, e.nombre, e.fecha "
                   + "ORDER BY e.fecha";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    public List<EventoDTO> getEventosConAsignacion(int idAgencia) {
        String sql = "SELECT e.id, e.nombre, e.fecha, GROUP_CONCAT(t.nombre, ', ') AS tematica "
                   + "FROM Evento e "
                   + "JOIN Asignacion a ON e.id = a.evento_id "
                   + "LEFT JOIN EventoTematica et ON e.id = et.evento_id "
                   + "LEFT JOIN Tematica t ON et.tematica_id = t.id "
                   + "WHERE e.agencia_id = ? "
                   + "GROUP BY e.id, e.nombre, e.fecha "
                   + "ORDER BY e.fecha";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    // ACTUALIZADO: Añadimos 'tipoFiltro' y construimos la consulta dinámicamente
    public List<ReporteroDTO> getReporterosDisponibles(int idAgencia, String fechaEvento, int idEvento, boolean filtrarTematica, String tipoFiltro) {
        String sql = "SELECT r.id, r.nombre, r.tipo, GROUP_CONCAT(t.nombre, ', ') AS tematica "
                   + "FROM Reportero r "
                   + "LEFT JOIN ReporteroTematica rt ON r.id = rt.reportero_id "
                   + "LEFT JOIN Tematica t ON rt.tematica_id = t.id "
                   + "WHERE r.agencia_id = ? "
                   + "AND r.id NOT IN ("
                   + "    SELECT a.reportero_id FROM Asignacion a "
                   + "    JOIN Evento e ON a.evento_id = e.id "
                   + "    WHERE e.fecha = ?"
                   + ") ";

        List<Object> params = new ArrayList<>();
        params.add(idAgencia);
        params.add(fechaEvento);

        if (filtrarTematica) {
            sql += "AND r.id IN ("
                 + "    SELECT rt_sub.reportero_id FROM ReporteroTematica rt_sub "
                 + "    JOIN EventoTematica et_sub ON rt_sub.tematica_id = et_sub.tematica_id "
                 + "    WHERE et_sub.evento_id = ?"
                 + ") ";
            params.add(idEvento);
        }

        if (!tipoFiltro.equals("Todos")) {
            sql += "AND r.tipo = ? ";
            params.add(tipoFiltro);
        }

        sql += "GROUP BY r.id, r.nombre, r.tipo ORDER BY r.nombre";
        
        return db.executeQueryPojo(ReporteroDTO.class, sql, params.toArray());
    }

    public List<ReporteroDTO> getReporterosAsignados(int idEvento) {
        // Añadimos r.tipo para que se vea también en la tabla de asignados
        String sql = "SELECT r.id, r.nombre, r.tipo, GROUP_CONCAT(t.nombre, ', ') AS tematica "
                   + "FROM Reportero r "
                   + "JOIN Asignacion a ON r.id = a.reportero_id "
                   + "LEFT JOIN ReporteroTematica rt ON r.id = rt.reportero_id "
                   + "LEFT JOIN Tematica t ON rt.tematica_id = t.id "
                   + "WHERE a.evento_id = ? "
                   + "GROUP BY r.id, r.nombre, r.tipo "
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