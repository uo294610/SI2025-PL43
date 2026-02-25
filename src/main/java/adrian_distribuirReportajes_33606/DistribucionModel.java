package adrian_distribuirReportajes_33606;

import java.util.List;
import giis.demo.util.Database;

public class DistribucionModel {
	private Database db = new Database();

	// Visualizar eventos con reportaje ya entregado
	public List<EventoEntregadoDTO> getEventosConReportaje() {
		String sql = "SELECT e.id, e.nombre as nombreEvento, 'ENTREGADO' as estado " +
		             "FROM Evento e JOIN Reportaje r ON e.id = r.evento_id";
		return db.executeQueryPojo(EventoEntregadoDTO.class, sql);
	}

	// Empresas que aceptaron el ofrecimiento pero no tienen acceso (acceso = 0)
	public List<EmpresaAceptadaDTO> getEmpresasAceptadasSinAcceso(String idEvento) {
		String sql = "SELECT ec.id, ec.nombre as nombreEmpresa " +
		             "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
		             "WHERE o.evento_id = ? AND o.decision = 'ACEPTADO' AND o.acceso = 0";
		return db.executeQueryPojo(EmpresaAceptadaDTO.class, sql, idEvento);
	}

	// Dar acceso al reportaje (Update del campo acceso)
	public void darAcceso(String idEvento, String idEmpresa) {
		String sql = "UPDATE Ofrecimiento SET acceso = 1 WHERE evento_id = ? AND empresa_id = ?";
		db.executeUpdate(sql, idEvento, idEmpresa);
	}
}