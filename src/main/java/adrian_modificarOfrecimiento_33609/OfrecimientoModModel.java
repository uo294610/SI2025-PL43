package adrian_modificarOfrecimiento_33609;

import java.util.List;
import giis.demo.util.Database;

public class OfrecimientoModModel {
	private Database db = new Database();

	public List<EventoModDTO> getEventosConReportero() {
		// Usamos GROUP_CONCAT para que cada evento salga una sola vez con sus reporteros
		String sql = "SELECT e.id, e.nombre, e.fecha, GROUP_CONCAT(r.nombre, ', ') as reportero " +
					 "FROM Evento e JOIN Asignacion a ON e.id = a.evento_id " +
					 "JOIN Reportero r ON a.reportero_id = r.id " +
					 "GROUP BY e.id, e.nombre, e.fecha " +
					 "ORDER BY e.fecha ASC";
		return db.executeQueryPojo(EventoModDTO.class, sql);
	}

	public List<EmpresaModDTO> getEmpresasConOfrecimiento(String eventoId) {
		String sql = "SELECT ec.id, ec.nombre, o.decision as estado, o.acceso " +
					 "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
					 "WHERE o.evento_id = ?";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId);
	}

	public List<EmpresaModDTO> getEmpresasSinOfrecimiento(String eventoId) {
		String sql = "SELECT id, nombre FROM EmpresaComunicacion " +
					 "WHERE id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?)";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, eventoId);
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