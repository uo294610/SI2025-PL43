package adrian_distribuirReportajes_33606;

import java.util.List;
import giis.demo.util.Database;

public class DistribucionModel {
	private Database db = new Database();

	public List<EventoEntregadoDTO> getEventosConReportaje() {
		String sql = "SELECT e.id, e.nombre as nombreEvento, er.estado " +
		             "FROM Evento e JOIN EvaluacionReportaje er ON e.id = er.evento_id " +
		             "WHERE er.estado = 'ACEPTADO'";
		return db.executeQueryPojo(EventoEntregadoDTO.class, sql);
	}

	public List<EmpresaAceptadaDTO> getEmpresasPorAcceso(String idEvento, boolean conAcceso) {
		int accesoFiltro = conAcceso ? 1 : 0;
		// Buscamos si el reportaje de este evento está descargado
		String sql = "SELECT ec.id, ec.nombre as nombreEmpresa, " +
		             "CASE WHEN r.estado = 'DESCARGADO' THEN 1 ELSE 0 END as descargado " +
		             "FROM EmpresaComunicacion ec " +
		             "JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
		             "JOIN EvaluacionReportaje er ON o.evento_id = er.evento_id " +
		             "JOIN Reportaje r ON er.reportaje_id = r.id " +
		             "WHERE o.evento_id = ? AND o.decision = 'ACEPTADO' AND o.acceso = ?";
		return db.executeQueryPojo(EmpresaAceptadaDTO.class, sql, idEvento, accesoFiltro);
	}

	public void darAcceso(String idEvento, String idEmpresa) {
		String sql = "UPDATE Ofrecimiento SET acceso = 1 WHERE evento_id = ? AND empresa_id = ?";
		db.executeUpdate(sql, idEvento, idEmpresa);
	}

	public void revocarAcceso(String idEvento, String idEmpresa) {
		String sql = "UPDATE Ofrecimiento SET acceso = 0 WHERE evento_id = ? AND empresa_id = ?";
		db.executeUpdate(sql, idEvento, idEmpresa);
	}
}