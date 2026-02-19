package diego_asignarReporteros;

import java.util.List;
import giis.demo.util.Database;

public class ReporteroModel {
    private Database db = new Database();

    /**
     * Obtiene la lista de eventos de una agencia que NO tienen ningún reportero asignado.
     */
    public List<EventoDisplayDTO> getEventosSinAsignar(int idAgencia) {
        String sql = "SELECT e.id, e.nombre, e.fecha "
                   + "FROM Evento e "
                   + "LEFT JOIN Asignacion a ON e.id = a.evento_id "
                   + "WHERE e.agencia_id = ? AND a.reportero_id IS NULL";
        return db.executeQueryPojo(EventoDisplayDTO.class, sql, idAgencia);
    }

    /**
     * Obtiene los reporteros de una agencia que están disponibles para una fecha concreta.
     * (No están asignados a ningún otro evento en esa misma fecha).
     */
    public List<ReporteroDisplayDTO> getReporterosDisponibles(int idAgencia, String fechaEvento) {
        String sql = "SELECT r.id, r.nombre "
                   + "FROM Reportero r "
                   + "WHERE r.agencia_id = ? AND r.id NOT IN ("
                   + "    SELECT a.reportero_id "
                   + "    FROM Asignacion a "
                   + "    INNER JOIN Evento ev ON a.evento_id = ev.id "
                   + "    WHERE ev.fecha = ?"
                   + ")";
        return db.executeQueryPojo(ReporteroDisplayDTO.class, sql, idAgencia, fechaEvento);
    }

    /**
     * Asigna un reportero a un evento en la base de datos.
     */
    public void asignarReportero(int idEvento, int idReportero) {
        String sql = "INSERT INTO Asignacion (evento_id, reportero_id) VALUES (?, ?)";
        db.executeUpdate(sql, idEvento, idReportero);
    }
}
