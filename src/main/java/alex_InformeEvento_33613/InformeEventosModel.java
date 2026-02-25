package alex_InformeEvento_33613;

import java.util.*;
import giis.demo.util.Database;

public class InformeEventosModel {
    private Database db = new Database();

    public List<Object[]> getListaAgencias() {
        return db.executeQueryArray("SELECT id, nombre FROM AgenciaPrensa ORDER BY nombre");
    }

    public List<EventoDTO> getEventosPorAgencia(int idAgencia) {
        String sql = "SELECT id, nombre, fecha FROM Evento WHERE agencia_id = ? ORDER BY fecha DESC";
        return db.executeQueryPojo(EventoDTO.class, sql, idAgencia);
    }

    public InformeDTO generarInforme(int idEvento) {
        InformeDTO informe = new InformeDTO();

        // Reporteros asignados 
        String sqlRep = "SELECT r.nombre FROM Asignacion a " +
                        "JOIN Reportero r ON a.reportero_id = r.id " +
                        "WHERE a.evento_id = ?";
        List<Object[]> reps = db.executeQueryArray(sqlRep, idEvento);
        List<String> reporteros = new ArrayList<>();
        for (Object[] r : reps) reporteros.add(r[0].toString());
        informe.setReporterosAsignados(reporteros);

        // Entrega del reportaje
        String sqlEnt = "SELECT r.nombre FROM Reportaje rep " +
                        "JOIN Reportero r ON rep.reportero_entrega_id = r.id " + 
                        "WHERE rep.evento_id = ?";
        
        
        List<Object[]> ent = db.executeQueryArray(sqlEnt, idEvento);
        if (!ent.isEmpty()) {
            informe.setTieneReportaje(true);
            informe.setReporteroEntrega(ent.get(0)[0].toString());
        } else {
            informe.setTieneReportaje(false);
            informe.setReporteroEntrega("");
        }

        // Empresas con acceso
        String sqlAcc = "SELECT ec.nombre FROM Ofrecimiento o " +
                        "JOIN EmpresaComunicacion ec ON o.empresa_id = ec.id " +
                        "WHERE o.evento_id = ? AND o.acceso = TRUE";
        
        List<Object[]> acc = db.executeQueryArray(sqlAcc, idEvento);
        List<String> empresas = new ArrayList<>();
        for (Object[] a : acc) empresas.add(a[0].toString());
        informe.setEmpresasConAcceso(empresas);

        return informe;
    }
}