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

	// Filtramos empresas según tengan acceso o no (hu #34114)
	public List<EmpresaAceptadaDTO> getEmpresasPorAcceso(String idEvento, boolean conAcceso) {
		int accesoFiltro = conAcceso ? 1 : 0;
		// Incluimos el campo 'descargado' para el criterio de revocación
		String sql = "SELECT ec.id, ec.nombre as nombreEmpresa, o.descargado " +
		             "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
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