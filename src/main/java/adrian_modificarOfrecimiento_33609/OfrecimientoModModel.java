package adrian_modificarOfrecimiento_33609;

import java.util.List;
import giis.demo.util.Database;

public class OfrecimientoModModel {
	private Database db = new Database();

	public List<EventoModDTO> getEventosConReportero() {
		// Añadimos el JOIN con Tematica para sacar el nombre de la temática
		String sql = "SELECT e.id, e.nombre, e.fecha, GROUP_CONCAT(DISTINCT r.nombre) as reportero, " +
					 "t.nombre as tematica, e.tematica_id as tematicaId " +
					 "FROM Evento e JOIN Asignacion a ON e.id = a.evento_id " +
					 "JOIN Reportero r ON a.reportero_id = r.id " +
					 "JOIN Tematica t ON e.tematica_id = t.id " +
					 "GROUP BY e.id, e.nombre, e.fecha, t.nombre, e.tematica_id " +
					 "ORDER BY e.fecha ASC";
		return db.executeQueryPojo(EventoModDTO.class, sql);
	}

	// === SIN OFRECIMIENTO ===
	public List<EmpresaModDTO> getEmpresasSinOfrecimiento(String eventoId) {
		String sql = "SELECT ec.id, ec.nombre, GROUP_CONCAT(t.nombre) as especialidad " +
					 "FROM EmpresaComunicacion ec " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "WHERE ec.id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?) " +
					 "GROUP BY ec.id, ec.nombre";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId);
	}

	public List<EmpresaModDTO> getEmpresasSinOfrecimientoPorTematica(String eventoId, int tematicaId) {
		String sql = "SELECT ec.id, ec.nombre, GROUP_CONCAT(t.nombre) as especialidad " +
					 "FROM EmpresaComunicacion ec " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "WHERE ec.id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?) " +
					 "AND ec.id IN (SELECT empresa_id FROM EmpresaTematica WHERE tematica_id = ?) " +
					 "GROUP BY ec.id, ec.nombre";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId, tematicaId);
	}

	// === CON OFRECIMIENTO ===
	public List<EmpresaModDTO> getEmpresasConOfrecimiento(String eventoId) {
		String sql = "SELECT ec.id, ec.nombre, o.decision as estado, o.acceso, GROUP_CONCAT(t.nombre) as especialidad " +
					 "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "WHERE o.evento_id = ? GROUP BY ec.id, ec.nombre, o.decision, o.acceso";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId);
	}

	public List<EmpresaModDTO> getEmpresasConOfrecimientoPorTematica(String eventoId, int tematicaId) {
		String sql = "SELECT ec.id, ec.nombre, o.decision as estado, o.acceso, GROUP_CONCAT(t.nombre) as especialidad " +
					 "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
					 "LEFT JOIN EmpresaTematica et ON ec.id = et.empresa_id " +
					 "LEFT JOIN Tematica t ON et.tematica_id = t.id " +
					 "WHERE o.evento_id = ? AND ec.id IN (SELECT empresa_id FROM EmpresaTematica WHERE tematica_id = ?) " +
					 "GROUP BY ec.id, ec.nombre, o.decision, o.acceso";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId, tematicaId);
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