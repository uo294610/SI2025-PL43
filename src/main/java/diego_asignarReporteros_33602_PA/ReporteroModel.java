package diego_asignarReporteros_33602_PA;

import java.util.List;
import java.util.ArrayList; 
import giis.demo.util.Database;
import giis.demo.util.ApplicationException;

public class ReporteroModel {
    private Database db = new Database();

    public List<EventoDTO> getEventosSinAsignar(int idAgencia) {
        String sql = "SELECT e.id, e.nombre, e.fecha, e.fecha_fin AS fechaFin, e.asignacion_finalizada AS asignacionFinalizada, GROUP_CONCAT(t.nombre, ', ') AS tematica "
                   + "FROM Evento e "
                   + "LEFT JOIN Asignacion a ON e.id = a.evento_id "
                   + "LEFT JOIN EventoTematica et ON e.id = et.evento_id "
                   + "LEFT JOIN Tematica t ON et.tematica_id = t.id "
                   + "WHERE e.agencia_id = ? AND a.evento_id IS NULL "
                   + "GROUP BY e.id, e.nombre, e.fecha, e.fecha_fin, e.asignacion_finalizada "
                   + "ORDER BY e.fecha";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    public List<EventoDTO> getEventosConAsignacion(int idAgencia) {
        String sql = "SELECT e.id, e.nombre, e.fecha, e.fecha_fin AS fechaFin, e.asignacion_finalizada AS asignacionFinalizada, GROUP_CONCAT(t.nombre, ', ') AS tematica "
                   + "FROM Evento e "
                   + "JOIN Asignacion a ON e.id = a.evento_id "
                   + "LEFT JOIN EventoTematica et ON e.id = et.evento_id "
                   + "LEFT JOIN Tematica t ON et.tematica_id = t.id "
                   + "WHERE e.agencia_id = ? "
                   + "GROUP BY e.id, e.nombre, e.fecha, e.fecha_fin, e.asignacion_finalizada "
                   + "ORDER BY e.fecha";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    public List<ReporteroDTO> getReporterosDisponibles(int idAgencia, String fechaInicio, String fechaFin, int idEvento, boolean filtrarTematica, String tipoFiltro) {
        String sql = "SELECT r.id, r.nombre, r.tipo, GROUP_CONCAT(t.nombre, ', ') AS tematica "
                   + "FROM Reportero r "
                   + "LEFT JOIN ReporteroTematica rt ON r.id = rt.reportero_id "
                   + "LEFT JOIN Tematica t ON rt.tematica_id = t.id "
                   + "WHERE r.agencia_id = ? "
                   + "AND r.id NOT IN ("
                   + "    SELECT a.reportero_id FROM Asignacion a "
                   + "    JOIN Evento e ON a.evento_id = e.id "
                   + "    WHERE e.fecha_fin >= ? AND e.fecha <= ?" 
                   + ") ";

        List<Object> params = new ArrayList<>();
        params.add(idAgencia);
        params.add(fechaInicio); 
        params.add(fechaFin);    

        if (filtrarTematica) {
            sql += "AND r.id IN (SELECT rt_sub.reportero_id FROM ReporteroTematica rt_sub JOIN EventoTematica et_sub ON rt_sub.tematica_id = et_sub.tematica_id WHERE et_sub.evento_id = ?) ";
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
        String sql = "SELECT r.id, r.nombre, r.tipo, a.rol, GROUP_CONCAT(t.nombre, ', ') AS tematica "
                   + "FROM Reportero r "
                   + "JOIN Asignacion a ON r.id = a.reportero_id "
                   + "LEFT JOIN ReporteroTematica rt ON r.id = rt.reportero_id "
                   + "LEFT JOIN Tematica t ON rt.tematica_id = t.id "
                   + "WHERE a.evento_id = ? "
                   + "GROUP BY r.id, r.nombre, r.tipo, a.rol "
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

    public void setResponsable(int idEvento, int idReportero) {
        db.executeUpdate("UPDATE Asignacion SET rol = 'Base' WHERE evento_id = ?", idEvento);
        db.executeUpdate("UPDATE Asignacion SET rol = 'Responsable' WHERE evento_id = ? AND reportero_id = ?", idEvento, idReportero);
    }

    public void finalizarAsignacion(int idEvento) {
        db.executeUpdate("UPDATE Evento SET asignacion_finalizada = 1 WHERE id = ?", idEvento);
    }
    
    // NUEVO MÉTODO CON LA LÓGICA DE NEGOCIO PARA PODER TESTEARLA
    public void validarYFinalizarAsignacion(int idEvento) {
        String sql = "SELECT rol FROM Asignacion WHERE evento_id = ?";
        List<Object[]> roles = db.executeQueryArray(sql, idEvento);
        
        int countResponsables = 0;
        int countBases = 0;
        
        for (Object[] row : roles) {
            String rol = (String) row[0];
            if ("Responsable".equals(rol)) countResponsables++;
            if ("Base".equals(rol)) countBases++;
        }
        
        if (countResponsables != 1 || countBases < 1) {
            // CAMBIO: Ahora lanza la excepción estándar de vuestro proyecto
            throw new giis.demo.util.ApplicationException("Debe haber exactamente 1 Responsable y al menos 1 Base.");
        }
        
        finalizarAsignacion(idEvento);
    }
}