package diego_asignarReporteros;

import java.util.List;
import giis.demo.util.Database;

public class ReporteroModel {
    private Database db = new Database();

    /**
     * Obtiene eventos de una agencia que NO tienen asignaciones.
     */
    public List<EventoDTO> getEventosSinAsignar(int idAgencia) {
 
        String sql = "SELECT e.id, e.nombre, e.fecha "
                   + "FROM Evento e "
                   + "LEFT JOIN Asignacion a ON e.id = a.evento_id "
                   + "WHERE e.agencia_id = ? "
                   + "AND a.evento_id IS NULL "
                   + "ORDER BY e.fecha";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene reporteros de la agencia que NO están asignados a ningún evento en esa fecha concreta.
     */
    public List<ReporteroDTO> getReporterosDisponibles(int idAgencia, String fechaEvento) {
        String sql = "SELECT id, nombre FROM Reportero "
                   + "WHERE agencia_id = ? "
                   + "AND id NOT IN ("
                   + "    SELECT a.reportero_id FROM Asignacion a "
                   + "    JOIN Evento e ON a.evento_id = e.id "
                   + "    WHERE e.fecha = ?"
                   + ") ORDER BY nombre";
        return db.executeQueryPojo(ReporteroDTO.class, sql, idAgencia, fechaEvento);
    }

    /**
     * Crea la asignación en la base de datos.
     */
    public void asignarReportero(int idEvento, int idReportero) {
        String sql = "INSERT INTO Asignacion (evento_id, reportero_id) VALUES (?, ?)";
        db.executeUpdate(sql, idEvento, idReportero);
    }
}
