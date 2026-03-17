package alex_GestionarOfrecimientos;

import java.util.ArrayList;
import java.util.List;
import giis.demo.util.Database;

public class OfrecimientosModel {
    private Database db = new Database();

    public List<Object[]> getListaEmpresas() {
        return db.executeQueryArray("SELECT id, nombre FROM EmpresaComunicacion ORDER BY nombre");
    }

    public List<Object[]> getTodasTematicas() {
        return db.executeQueryArray("SELECT id, nombre FROM Tematica ORDER BY nombre");
    }

    public List<Object[]> getTematicasEmpresa(int idEmpresa) {
        String sql = "SELECT t.id, t.nombre FROM Tematica t " +
                     "JOIN EmpresaTematica et ON et.tematica_id = t.id " +
                     "WHERE et.empresa_id = ? ORDER BY t.nombre";
        return db.executeQueryArray(sql, idEmpresa);
    }

    public List<OfrecimientosDTO> getOfrecimientos(int idEmpresa, boolean soloEmpresa, int idTematica, boolean soloPendientes) {
        StringBuilder sql = new StringBuilder(	
        	"SELECT o.id, ev.nombre AS nombreEvento, a.nombre AS nombreAgencia, " +
  		    "ev.fecha AS fechaEvento, t.nombre AS nombreTematica, o.decision, o.acceso " + 
            "FROM Ofrecimiento o " +
            "JOIN Evento ev ON o.evento_id = ev.id " +
            "JOIN AgenciaPrensa a ON ev.agencia_id = a.id " +
            "JOIN Tematica t ON ev.tematica_id = t.id " +
            "WHERE o.empresa_id = ? "
        );

        List<Object> params = new ArrayList<>();
        params.add(idEmpresa);

        if (soloPendientes) sql.append("AND o.decision IS NULL ");
        else sql.append("AND o.decision IS NOT NULL ");

        if (soloEmpresa) {
            if (idTematica > 0) {
                sql.append("AND t.id = ? ");
                params.add(idTematica);
            } else {
                sql.append("AND t.id IN (SELECT tematica_id FROM EmpresaTematica WHERE empresa_id = ?) ");
                params.add(idEmpresa);
            }
        } else if (idTematica > 0) {
            sql.append("AND t.id = ? ");
            params.add(idTematica);
        }

        sql.append("ORDER BY ev.fecha ASC");
        return db.executeQueryPojo(OfrecimientosDTO.class, sql.toString(), params.toArray());
    }

    public void actualizarDecision(int idOfrecimiento, String decision) {
        String sql = "UPDATE Ofrecimiento SET decision = ? WHERE id = ?";
        db.executeUpdate(sql, decision, idOfrecimiento);
    }
}