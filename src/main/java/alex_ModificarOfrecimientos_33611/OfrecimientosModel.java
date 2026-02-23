package alex_ModificarOfrecimientos_33611;

import java.util.*;
import giis.demo.util.Database;

public class OfrecimientosModel {
    private Database db = new Database();

    public List<Object[]> getListaEmpresas() {
        return db.executeQueryArray("SELECT id, nombre FROM EmpresaComunicacion ORDER BY nombre");
    }

    public List<OfrecimientosDTO> getOfrecimientosFiltrados(int empresaId, boolean verDecididos) {
        String clausula = verDecididos ? "IS NOT NULL" : "IS NULL";
        String sql = "SELECT o.id, e.nombre AS evento, a.nombre AS agencia, e.fecha, o.decision " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento e ON o.evento_id = e.id " +
                     "JOIN AgenciaPrensa a ON e.agencia_id = a.id " +
                     "WHERE o.empresa_id = ? AND o.decision " + clausula + " ORDER BY e.fecha ASC";
        return db.executeQueryPojo(OfrecimientosDTO.class, sql, empresaId);
    }

    public OfrecimientoEntity getDetalleOfrecimiento(int id) {
        String sql = "SELECT o.id, e.nombre AS evento, a.nombre AS agencia, e.fecha, o.acceso, o.decision " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento e ON o.evento_id = e.id " +
                     "JOIN AgenciaPrensa a ON e.agencia_id = a.id " +
                     "WHERE o.id = ?";
        List<OfrecimientoEntity> lista = db.executeQueryPojo(OfrecimientoEntity.class, sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void updateDecision(int id, String decision) {
        // Solo actualizamos la decisión. 
        // El acceso se queda como está (si es 0, permite seguir editando)
        db.executeUpdate("UPDATE Ofrecimiento SET decision = ? WHERE id = ?", decision, id);
    }
}