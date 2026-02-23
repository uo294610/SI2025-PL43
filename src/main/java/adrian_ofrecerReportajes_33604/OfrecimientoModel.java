package adrian_ofrecerReportajes_33604;


import java.util.List;
import giis.demo.util.Database;

public class OfrecimientoModel {
    private Database db = new Database();

    // Visualiza eventos con reportero asignado
    public List<EventoAsignadoDTO> getEventosConReportero() {
        String sql = "SELECT e.id, e.nombre as nombreEvento, e.fecha, r.nombre as nombreReportero " +
                     "FROM Evento e JOIN Asignacion a ON e.id = a.evento_id " +
                     "JOIN Reportero r ON a.reportero_id = r.id";
        return db.executeQueryPojo(EventoAsignadoDTO.class, sql);
    }

    // Empresas a las que todavía no se les ha ofrecido el reportaje
    public List<EmpresaDTO> getEmpresasDisponibles(String idEvento) {
        String sql = "SELECT id, nombre FROM EmpresaComunicacion " +
                     "WHERE id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?)";
        return db.executeQueryPojo(EmpresaDTO.class, sql, idEvento);
    }

    // Registra el ofrecimiento generando el ID para evitar errores de restricción
    public void registrarOfrecimiento(String idEvento, String idEmpresa) {
        String sql = "INSERT INTO Ofrecimiento (id, evento_id, empresa_id, decision, acceso) " +
                     "VALUES ((SELECT IFNULL(MAX(id), 0) + 1 FROM Ofrecimiento), ?, ?, NULL, 0)";
        db.executeUpdate(sql, idEvento, idEmpresa);
    }
}