package alex_GestionarOfrecimientos_33605;

import java.util.*;
import giis.demo.util.Database;

public class OfrecimientosModel {
    private Database db = new Database();

    public List<Object[]> getListaEmpresas() {
        // Obtenemos ID y Nombre para el combo
        return db.executeQueryArray("SELECT id, nombre FROM EmpresaComunicacion ORDER BY nombre");
    }

    public List<OfrecimientosDTO> getOfrecimientosPendientes(int empresaId) {
        String sql = "SELECT o.id, e.nombre AS evento, a.nombre AS agencia, e.fecha " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento e ON o.evento_id = e.id " +
                     "JOIN AgenciaPrensa a ON e.agencia_id = a.id " +
                     "WHERE o.decision IS NULL AND o.empresa_id = ? ORDER BY e.fecha ASC";
        return db.executeQueryPojo(OfrecimientosDTO.class, sql, empresaId);
    }

    public OfrecimientoEntity getDetalleOfrecimiento(int id) {
        String sql = "SELECT o.id, e.nombre AS evento, a.nombre AS agencia, e.fecha " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento e ON o.evento_id = e.id " +
                     "JOIN AgenciaPrensa a ON e.agencia_id = a.id " +
                     "WHERE o.id = ?";
        List<OfrecimientoEntity> lista = db.executeQueryPojo(OfrecimientoEntity.class, sql, id);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void updateDecision(int id, String decision) {
        int acceso = decision.equals("ACEPTADO") ? 1 : 0;
        db.executeUpdate("UPDATE Ofrecimiento SET decision = ?, acceso = ? WHERE id = ?", decision, acceso, id);
    }
}


