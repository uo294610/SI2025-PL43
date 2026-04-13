package adrian_modificarOfrecimiento_33609;

import java.util.List;
import giis.demo.util.Database;

public class OfrecimientoModModel {
	private Database db = new Database();

	public List<EventoModDTO> getEventosConReportero() {
		String sql = "SELECT e.id, e.nombre, e.fecha, e.asignacion_finalizada AS asignacionFinalizada, e.agencia_id AS agenciaId, " +
					 "GROUP_CONCAT(DISTINCT r.nombre) as reportero, " +
					 "GROUP_CONCAT(DISTINCT t.nombre) as tematica, " +
					 "COALESCE(CASE " +
					 "  WHEN rep.fecha_fin_embargo IS NOT NULL AND rep.fecha_fin_embargo > CURRENT_TIMESTAMP THEN 'SÍ (ACTIVO)' " +
					 "  WHEN rep.fecha_fin_embargo IS NOT NULL AND rep.fecha_fin_embargo <= CURRENT_TIMESTAMP THEN 'CADUCADO' " +
					 "  ELSE 'NO' END, 'NO') as embargo " +
					 "FROM Evento e JOIN Asignacion a ON e.id = a.evento_id " +
					 "JOIN Reportero r ON a.reportero_id = r.id " +
					 "LEFT JOIN EventoTematica evt ON e.id = evt.evento_id " +
					 "LEFT JOIN Tematica t ON evt.tematica_id = t.id " +
					 "LEFT JOIN EvaluacionReportaje er ON e.id = er.evento_id " +
					 "LEFT JOIN Reportaje rep ON er.reportaje_id = rep.id " +
					 "GROUP BY e.id, e.nombre, e.fecha, rep.fecha_fin_embargo, e.asignacion_finalizada, e.agencia_id " +
					 "ORDER BY e.fecha ASC";
		return db.executeQueryPojo(EventoModDTO.class, sql);
	}

	// === SIN OFRECIMIENTO ===
	public List<EmpresaModDTO> getEmpresasSinOfrecimiento(String eventoId, boolean embargoActivo, String agenciaId, boolean soloTarifaPlana) {
		String sql = "SELECT ec.id, ec.nombre, GROUP_CONCAT(DISTINCT t.nombre) as especialidad, " +
					 "CASE WHEN ec.acepta_embargos = 1 THEN 'SÍ' ELSE 'NO' END as aceptaEmbargos, " +
					 "CASE WHEN tp.empresa_id IS NOT NULL THEN 'SÍ' ELSE 'NO' END as tarifaPlana, " +
					 "COALESCE(tp.al_corriente_pago, 1) as alCorrientePago " +
					 "FROM EmpresaComunicacion ec " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "LEFT JOIN TarifaPlana tp ON ec.id = tp.empresa_id AND tp.agencia_id = ? " +
					 "WHERE ec.id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?) ";
		
		if (embargoActivo) { sql += " AND ec.acepta_embargos = 1 "; }
		if (soloTarifaPlana) { sql += " AND tp.empresa_id IS NOT NULL "; }
		
		sql += "GROUP BY ec.id, ec.nombre, ec.acepta_embargos, tp.empresa_id, tp.al_corriente_pago";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, agenciaId, eventoId);
	}

	public List<EmpresaModDTO> getEmpresasSinOfrecimientoPorTematica(String eventoId, boolean embargoActivo, String agenciaId, boolean soloTarifaPlana) {
		String sql = "SELECT ec.id, ec.nombre, GROUP_CONCAT(DISTINCT t.nombre) as especialidad, " +
					 "CASE WHEN ec.acepta_embargos = 1 THEN 'SÍ' ELSE 'NO' END as aceptaEmbargos, " +
					 "CASE WHEN tp.empresa_id IS NOT NULL THEN 'SÍ' ELSE 'NO' END as tarifaPlana, " +
					 "COALESCE(tp.al_corriente_pago, 1) as alCorrientePago " +
					 "FROM EmpresaComunicacion ec " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "LEFT JOIN TarifaPlana tp ON ec.id = tp.empresa_id AND tp.agencia_id = ? " +
					 "WHERE ec.id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?) " +
					 "AND ec.id IN (SELECT empresa_id FROM EmpresaTematica WHERE tematica_id IN (SELECT tematica_id FROM EventoTematica WHERE evento_id = ?)) ";
		
		if (embargoActivo) { sql += " AND ec.acepta_embargos = 1 "; }
		if (soloTarifaPlana) { sql += " AND tp.empresa_id IS NOT NULL "; }
		
		sql += "GROUP BY ec.id, ec.nombre, ec.acepta_embargos, tp.empresa_id, tp.al_corriente_pago";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, agenciaId, eventoId, eventoId);
	}

	// === CON OFRECIMIENTO ===
	public List<EmpresaModDTO> getEmpresasConOfrecimiento(String eventoId, boolean embargoActivo, String agenciaId, boolean soloTarifaPlana) {
		String sql = "SELECT ec.id, ec.nombre, o.decision as estado, o.acceso, GROUP_CONCAT(DISTINCT t.nombre) as especialidad, " +
					 "CASE WHEN ec.acepta_embargos = 1 THEN 'SÍ' ELSE 'NO' END as aceptaEmbargos, " +
					 "CASE WHEN tp.empresa_id IS NOT NULL THEN 'SÍ' ELSE 'NO' END as tarifaPlana, " +
					 "COALESCE(tp.al_corriente_pago, 1) as alCorrientePago " +
					 "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "LEFT JOIN TarifaPlana tp ON ec.id = tp.empresa_id AND tp.agencia_id = ? " +
					 "WHERE o.evento_id = ? ";
		
		if (embargoActivo) { sql += " AND ec.acepta_embargos = 1 "; }
		if (soloTarifaPlana) { sql += " AND tp.empresa_id IS NOT NULL "; }
		
		sql += "GROUP BY ec.id, ec.nombre, o.decision, o.acceso, ec.acepta_embargos, tp.empresa_id, tp.al_corriente_pago";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, agenciaId, eventoId);
	}

	public List<EmpresaModDTO> getEmpresasConOfrecimientoPorTematica(String eventoId, boolean embargoActivo, String agenciaId, boolean soloTarifaPlana) {
		String sql = "SELECT ec.id, ec.nombre, o.decision as estado, o.acceso, GROUP_CONCAT(DISTINCT t.nombre) as especialidad, " +
					 "CASE WHEN ec.acepta_embargos = 1 THEN 'SÍ' ELSE 'NO' END as aceptaEmbargos, " +
					 "CASE WHEN tp.empresa_id IS NOT NULL THEN 'SÍ' ELSE 'NO' END as tarifaPlana, " +
					 "COALESCE(tp.al_corriente_pago, 1) as alCorrientePago " +
					 "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "LEFT JOIN TarifaPlana tp ON ec.id = tp.empresa_id AND tp.agencia_id = ? " +
					 "WHERE o.evento_id = ? " +
					 "AND ec.id IN (SELECT empresa_id FROM EmpresaTematica WHERE tematica_id IN (SELECT tematica_id FROM EventoTematica WHERE evento_id = ?)) ";
		
		if (embargoActivo) { sql += " AND ec.acepta_embargos = 1 "; }
		if (soloTarifaPlana) { sql += " AND tp.empresa_id IS NOT NULL "; }
		
		sql += "GROUP BY ec.id, ec.nombre, o.decision, o.acceso, ec.acepta_embargos, tp.empresa_id, tp.al_corriente_pago";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, agenciaId, eventoId, eventoId);
	}

	public void insertarOfrecimiento(String eventoId, String empresaId) {
		String sql = "INSERT INTO Ofrecimiento (id, evento_id, empresa_id, acceso) VALUES (?,?,?,0)";
		db.executeUpdate(sql, (int)(Math.random()*10000), eventoId, empresaId);
	}

	public void eliminarOfrecimiento(String eventoId, String empresaId) {
		String sql = "DELETE FROM Ofrecimiento WHERE evento_id = ? AND empresa_id = ?";
		db.executeUpdate(sql, eventoId, empresaId);
	}
}