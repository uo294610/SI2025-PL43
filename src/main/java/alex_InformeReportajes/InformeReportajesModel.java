package alex_InformeReportajes;

import java.util.List;
import giis.demo.util.Database;

public class InformeReportajesModel {
    private Database db = new Database();

    public List<Object[]> getListaEmpresas() {
        return db.executeQueryArray("SELECT id, nombre FROM EmpresaComunicacion ORDER BY nombre");
    }

    public List<Object[]> getListaAgencias() {
        return db.executeQueryArray("SELECT id, nombre FROM AgenciaPrensa ORDER BY nombre");
    }

    public List<InformeReportajeDTO> getReportajesAccesibles(int idEmpresa, int idAgencia, String fechaInicio, String fechaFin) {
        String sql = "SELECT r.id, ev.nombre AS nombreEvento, ev.fecha AS fechaEvento, " +
                     "ev.precio AS precio, GROUP_CONCAT(t.nombre, ', ') AS nombreTematica, " +
                     "r.titulo AS tituloReportaje " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento ev ON o.evento_id = ev.id " +
                     "JOIN EvaluacionReportaje er ON ev.id = er.evento_id " +
                     "JOIN Reportaje r ON er.reportaje_id = r.id " +
                     "JOIN EventoTematica etm ON ev.id = etm.evento_id " +
                     "JOIN Tematica t ON etm.tematica_id = t.id " +
                     "WHERE o.empresa_id = ? AND o.acceso = TRUE " + 
                     "AND ev.agencia_id = ? " +
                     "AND ev.fecha >= ? AND ev.fecha <= ? " +
                     "GROUP BY r.id, ev.nombre, ev.fecha, ev.precio, r.titulo " +
                     "ORDER BY ev.fecha ASC";
                     
        return db.executeQueryPojo(InformeReportajeDTO.class, sql, idEmpresa, idAgencia, fechaInicio, fechaFin);
    }
}