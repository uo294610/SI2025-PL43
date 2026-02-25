package adrian_modificarOfrecimiento_33609;

import java.util.List;
import giis.demo.util.Database;

public class OfrecimientoModModel {
	private Database db = new Database();

	public List<EventoModDTO> getEventosAsignados() {
		String sql = "SELECT e.id, e.nombre, e.fecha, r.nombre as reportero " +
		             "FROM Evento e JOIN Asignacion a ON e.id = a.evento_id " +
		             "JOIN Reportero r ON a.reportero_id = r.id";
		return db.executeQueryPojo(EventoModDTO.class, sql);
	}

	public List<EmpresaModDTO> getEmpresasSinOfrecimiento(String idEvento) {
		String sql = "SELECT id, nombre, 'SIN OFERTAR' as estado, 0 as acceso FROM EmpresaComunicacion " +
		             "WHERE id NOT IN (SELECT empresa_id FROM Ofrecimiento WHERE evento_id = ?)";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, idEvento);
	}

	public List<EmpresaModDTO> getEmpresasConOfrecimiento(String idEvento) {
		String sql = "SELECT ec.id, ec.nombre, o.decision as estado, o.acceso " +
		             "FROM EmpresaComunicacion ec JOIN Ofrecimiento o ON ec.id = o.empresa_id " +
		             "WHERE o.evento_id = ?";
		return db.executeQueryPojo(EmpresaModDTO.class, sql, idEvento);
	}

	public void eliminarOfrecimiento(String idEvento, String idEmpresa) {
		String sql = "DELETE FROM Ofrecimiento WHERE evento_id = ? AND empresa_id = ?";
		db.executeUpdate(sql, idEvento, idEmpresa);
	}
}