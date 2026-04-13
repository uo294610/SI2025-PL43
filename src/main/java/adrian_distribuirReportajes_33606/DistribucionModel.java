package adrian_distribuirReportajes_33606;

import java.util.List;
import giis.demo.util.Database;

public class DistribucionModel {
	private Database db = new Database();

	// Filtra los eventos finalizados y calcula si el embargo está activo
	public List<EventoEntregadoDTO> getEventosConReportaje(String filtroEmbargo) {
		String sql = "SELECT e.id, e.nombre as nombreEvento, r.estado, " +
		             "CASE WHEN r.fecha_fin_embargo IS NOT NULL AND r.fecha_fin_embargo > CURRENT_TIMESTAMP THEN 'SÍ (ACTIVO)' ELSE 'NO' END as embargo " +
		             "FROM Evento e " +
		             "JOIN EvaluacionReportaje er ON e.id = er.evento_id " +
		             "JOIN Reportaje r ON er.reportaje_id = r.id " +
		             "WHERE r.estado = 'FINALIZADO'";

		if ("Solo Embargo Activo".equals(filtroEmbargo)) {
			sql += " AND r.fecha_fin_embargo IS NOT NULL AND r.fecha_fin_embargo > CURRENT_TIMESTAMP";
		} else if ("Sin Embargo / Caducado".equals(filtroEmbargo)) {
			sql += " AND (r.fecha_fin_embargo IS NULL OR r.fecha_fin_embargo <= CURRENT_TIMESTAMP)";
		}
		
		return db.executeQueryPojo(EventoEntregadoDTO.class, sql);
	}

	// Cruza las empresas con la tabla de TarifaPlana para saber si están al corriente de pago
	public List<EmpresaAceptadaDTO> getEmpresasPorAcceso(String idEvento, boolean conAcceso) {
		int accesoFiltro = conAcceso ? 1 : 0;
		String sql = "SELECT ec.id, ec.nombre as nombreEmpresa, " +
		             "CASE WHEN r.estado = 'DESCARGADO' THEN 1 ELSE 0 END as descargadoValor, " +
		             "COALESCE(o.tipo_acceso, 'NINGUNO') as tipoAccesoActual, " +
		             "CASE WHEN tp.empresa_id IS NOT NULL THEN 1 ELSE 0 END as tieneTarifaPlana, " +
		             "COALESCE(tp.al_corriente_pago, 0) as tarifaPlanaPagada, " +
		             "o.estado_pago as estadoPagoIndividual " +
		             "FROM EmpresaComunicacion ec " +
		             "JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
		             "JOIN EvaluacionReportaje er ON o.evento_id = er.evento_id " +
		             "JOIN Reportaje r ON er.reportaje_id = r.id " +
		             "JOIN Evento e ON er.evento_id = e.id " +
		             "LEFT JOIN TarifaPlana tp ON ec.id = tp.empresa_id AND e.agencia_id = tp.agencia_id " +
		             "WHERE o.evento_id = ? AND o.decision = 'ACEPTADO' AND o.acceso = ?";
		return db.executeQueryPojo(EmpresaAceptadaDTO.class, sql, idEvento, accesoFiltro);
	}

	public void darAcceso(String idEvento, String idEmpresa, String tipoAcceso) {
		String sql = "UPDATE Ofrecimiento SET acceso = 1, tipo_acceso = ? WHERE evento_id = ? AND empresa_id = ?";
		db.executeUpdate(sql, tipoAcceso, idEvento, idEmpresa);
	}

	public void revocarAcceso(String idEvento, String idEmpresa) {
		String sql = "UPDATE Ofrecimiento SET acceso = 0, tipo_acceso = 'NINGUNO' WHERE evento_id = ? AND empresa_id = ?";
		db.executeUpdate(sql, idEvento, idEmpresa);
	}
}