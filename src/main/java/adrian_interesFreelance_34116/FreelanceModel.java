package adrian_interesFreelance_34116;

import java.util.List;
import giis.demo.util.Database;

public class FreelanceModel {
	private Database db = new Database();

	public List<Object[]> getInfoFreelance(int reporteroId) {
		String sql = "SELECT r.nombre, GROUP_CONCAT(DISTINCT t.nombre) " +
		             "FROM Reportero r JOIN ReporteroTematica rt ON r.id = rt.reportero_id " +
		             "JOIN Tematica t ON rt.tematica_id = t.id " +
		             "WHERE r.id = ? GROUP BY r.id, r.nombre";
		return db.executeQueryArray(sql, reporteroId);
	}

	public List<EventoFreelanceDTO> getEventosDisponibles(int reporteroId) {
		// Magia SQL: Cruza el evento, su temática, la del freelance y el estado guardado
		String sql = "SELECT e.id, e.nombre as nombreEvento, e.fecha, " +
		             "GROUP_CONCAT(DISTINCT t.nombre) as tematica, " +
		             "COALESCE(i.estado, 'PENDIENTE') as miInteres " +
		             "FROM Evento e " +
		             "JOIN EventoTematica et ON e.id = et.evento_id " +
		             "JOIN Tematica t ON et.tematica_id = t.id " +
		             "LEFT JOIN InteresFreelance i ON e.id = i.evento_id AND i.reportero_id = ? " +
		             "WHERE e.id IN (SELECT evento_id FROM EventoTematica WHERE tematica_id IN (SELECT tematica_id FROM ReporteroTematica WHERE reportero_id = ?)) " +
		             "GROUP BY e.id, e.nombre, e.fecha, i.estado";
		return db.executeQueryPojo(EventoFreelanceDTO.class, sql, reporteroId, reporteroId);
	}
	
	public List<Object[]> getListaFreelances() {
	    String sql = "SELECT id, nombre FROM Reportero WHERE agencia_id IS NULL ORDER BY nombre";
	    return db.executeQueryArray(sql);
	}

	public void guardarInteres(int reporteroId, String eventoId, String estado) {
		// Borramos si ya existía para no tener duplicados, y luego insertamos la nueva decisión
		String sqlDelete = "DELETE FROM InteresFreelance WHERE reportero_id = ? AND evento_id = ?";
		db.executeUpdate(sqlDelete, reporteroId, eventoId);
		
		String sqlInsert = "INSERT INTO InteresFreelance (reportero_id, evento_id, estado) VALUES (?, ?, ?)";
		db.executeUpdate(sqlInsert, reporteroId, eventoId, estado);
	}
}