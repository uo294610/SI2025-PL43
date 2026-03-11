package alex_ModificarDecision_33611;

import java.util.List;
import giis.demo.util.Database;

public class OfrecimientosModel {
    private Database db = new Database();

    public List<Object[]> getListaEmpresas() {
        return db.executeQueryArray("SELECT id, nombre FROM EmpresaComunicacion ORDER BY nombre");
    }

    public List<OfrecimientosDTO> getOfrecimientosFiltrados(int idEmpresa, boolean verDecididos) {
        String condicion = verDecididos ? "o.decision IS NOT NULL" : "o.decision IS NULL";
        
        String sql = "SELECT o.id, ev.nombre as evento, ag.nombre as agencia, ev.fecha, o.decision " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento ev ON o.evento_id = ev.id " +
                     "JOIN AgenciaPrensa ag ON ev.agencia_id = ag.id " +
                     "WHERE o.empresa_id = ? AND " + condicion + " " +
                     "ORDER BY ev.fecha DESC";
                     
        return db.executeQueryPojo(OfrecimientosDTO.class, sql, idEmpresa);
    }

    public OfrecimientoEntity getDetalleOfrecimiento(int idOfrecimiento) {
        String sql = "SELECT o.id, ev.nombre as evento, ag.nombre as agencia, ev.fecha, o.decision, o.acceso " +
                     "FROM Ofrecimiento o " +
                     "JOIN Evento ev ON o.evento_id = ev.id " +
                     "JOIN AgenciaPrensa ag ON ev.agencia_id = ag.id " +
                     "WHERE o.id = ?";
                     
        List<OfrecimientoEntity> lista = db.executeQueryPojo(OfrecimientoEntity.class, sql, idOfrecimiento);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void updateDecision(int idOfrecimiento, String nuevaDecision) {
        String sql = "UPDATE Ofrecimiento SET decision = ? WHERE id = ?";
        db.executeUpdate(sql, nuevaDecision, idOfrecimiento);
    }
}