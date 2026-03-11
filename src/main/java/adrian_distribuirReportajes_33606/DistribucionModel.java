package adrian_distribuirReportajes_33606;

import java.util.List;
import giis.demo.util.Database;

public class DistribucionModel {
	private Database db = new Database();

	// Visualizar eventos que tienen un reportaje ya evaluado y aceptado
	public List<EventoEntregadoDTO> getEventosConReportaje() {
		String sql = "SELECT e.id, e.nombre as nombreEvento, er.estado " +
		             "FROM Evento e " +
		             "JOIN EvaluacionReportaje er ON e.id = er.evento_id " +
		             "WHERE er.estado = 'ACEPTADO'"; // Solo distribuimos lo aceptado
		return db.executeQueryPojo(EventoEntregadoDTO.class, sql);
	}

	// Empresas que aceptaron pero no tienen acceso (acceso = FALSE/0)
	public List<EmpresaAceptadaDTO> getEmpresasAceptadasSinAcceso(String idEvento) {
		String sql = "SELECT ec.id, ec.nombre as nombreEmpresa " +
		             "FROM EmpresaComunicacion ec " +
		             "JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
		             "WHERE o.evento_id = ? AND o.decision = 'ACEPTADO' AND o.acceso = 0";
		return db.executeQueryPojo(EmpresaAceptadaDTO.class, sql, idEvento);
	}

	// Dar acceso al reportaje (Update del campo acceso a TRUE/1)
	public void darAcceso(String idEvento, String idEmpresa) {
		String sql = "UPDATE Ofrecimiento SET acceso = 1 WHERE evento_id = ? AND empresa_id = ?";
		db.executeUpdate(sql, idEvento, idEmpresa);
	}
}