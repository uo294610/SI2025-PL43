package adrian_modificarOfrecimiento_33609;

import java.util.List;
import giis.demo.util.Database;

public class OfrecimientoModModel {
	private Database db = new Database();

	public List<EventoModDTO> getEventosConReportero() {
		// Ahora unimos con EventoTematica y usamos DISTINCT para evitar duplicados
		String sql = "SELECT e.id, e.nombre, e.fecha, " +
					 "GROUP_CONCAT(DISTINCT r.nombre) as reportero, " +
					 "GROUP_CONCAT(DISTINCT t.nombre) as tematica " +
					 "FROM Evento e JOIN Asignacion a ON e.id = a.evento_id " +
					 "JOIN Reportero r ON a.reportero_id = r.id " +
					 "LEFT JOIN EventoTematica evt ON e.id = evt.evento_id " +
					 "LEFT JOIN Tematica t ON evt.tematica_id = t.id " +
					 "GROUP BY e.id, e.nombre, e.fecha " +
					 "ORDER BY e.fecha ASC";
		return db.executeQueryPojo(EventoModDTO.class, sql);
	}

	// === SIN OFRECIMIENTO ===
	public List<EmpresaModDTO> getEmpresasSinOfrecimiento(String eventoId) {
		String sql = "SELECT ec.id, ec.nombre, GROUP_CONCAT(DISTINCT t.nombre) as especialidad " +
					 "FROM EmpresaComunicacion ec " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "WHERE ec.id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?) " +
					 "GROUP BY ec.id, ec.nombre";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId);
	}

	public List<EmpresaModDTO> getEmpresasSinOfrecimientoPorTematica(String eventoId) {
		// Magia SQL: Busca empresas que tengan ALGUNA temática en común con el evento
		String sql = "SELECT ec.id, ec.nombre, GROUP_CONCAT(DISTINCT t.nombre) as especialidad " +
					 "FROM EmpresaComunicacion ec " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "WHERE ec.id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?) " +
					 "AND ec.id IN (SELECT empresa_id FROM EmpresaTematica WHERE tematica_id IN (SELECT tematica_id FROM EventoTematica WHERE evento_id = ?)) " +
					 "GROUP BY ec.id, ec.nombre";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId, eventoId);
	}

	// === CON OFRECIMIENTO ===
	public List<EmpresaModDTO> getEmpresasConOfrecimiento(String eventoId) {
		String sql = "SELECT ec.id, ec.nombre, o.decision as estado, o.acceso, GROUP_CONCAT(DISTINCT t.nombre) as especialidad " +
					 "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "WHERE o.evento_id = ? GROUP BY ec.id, ec.nombre, o.decision, o.acceso";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId);
	}

	public List<EmpresaModDTO> getEmpresasConOfrecimientoPorTematica(String eventoId) {
		String sql = "SELECT ec.id, ec.nombre, o.decision as estado, o.acceso, GROUP_CONCAT(DISTINCT t.nombre) as especialidad " +
					 "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "WHERE o.evento_id = ? " +
					 "AND ec.id IN (SELECT empresa_id FROM EmpresaTematica WHERE tematica_id IN (SELECT tematica_id FROM EventoTematica WHERE evento_id = ?)) " +
					 "GROUP BY ec.id, ec.nombre, o.decision, o.acceso";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId, eventoId);
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