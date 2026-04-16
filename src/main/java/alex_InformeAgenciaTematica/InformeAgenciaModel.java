package alex_InformeAgenciaTematica;

import java.util.List;
import java.util.ArrayList;
import giis.demo.util.Database;

public class InformeAgenciaModel {
    private Database db = new Database();

    public List<Object[]> getAgencias() {
        return db.executeQueryArray("SELECT id, nombre FROM AgenciaPrensa ORDER BY nombre");
    }

    public List<Object[]> getTematicas() {
        return db.executeQueryArray("SELECT id, nombre FROM Tematica ORDER BY nombre");
    }

    public double getCuotasMensualesAgencia(int idAgencia) {
        String sql = "SELECT SUM(cuota_mensual) FROM TarifaPlana WHERE agencia_id = ?";
        List<Object[]> res = db.executeQueryArray(sql, idAgencia);
        if (res != null && !res.isEmpty() && res.get(0)[0] != null) {
            return Double.parseDouble(res.get(0)[0].toString());
        }
        return 0.0;
    }

    public List<InformeAgenciaDTO.EventoReporte> getInformeData(int idAgencia, int idTematica) {
        List<InformeAgenciaDTO.EventoReporte> listaEventos = new ArrayList<>();

        String sqlEventos = "SELECT DISTINCT e.id, e.nombre, e.precio " +
                            "FROM Evento e " +
                            "JOIN EventoTematica et ON e.id = et.evento_id " +
                            "WHERE e.agencia_id = ? AND et.tematica_id = ? " +
                            "ORDER BY e.fecha ASC";
        
        List<Object[]> rawEventos = db.executeQueryArray(sqlEventos, idAgencia, idTematica);

        String sqlAccesos = "SELECT c.nombre AS empresa, o.tipo_acceso, o.estado_pago, " +
                            "CASE WHEN tp.cuota_mensual IS NOT NULL THEN 1 ELSE 0 END AS tiene_tarifa, " +
                            "tp.cuota_mensual " + 
                            "FROM Ofrecimiento o " +
                            "JOIN EmpresaComunicacion c ON o.empresa_id = c.id " +
                            "LEFT JOIN TarifaPlana tp ON tp.empresa_id = c.id AND tp.agencia_id = ? " +
                            "WHERE o.evento_id = ? " +
                            "AND o.acceso = 1 " + 
                            "AND (tp.cuota_mensual IS NOT NULL OR o.estado_pago = 'PAGADO')";

        for (Object[] rowEv : rawEventos) {
            InformeAgenciaDTO.EventoReporte evento = new InformeAgenciaDTO.EventoReporte();
            evento.id = (int) rowEv[0];
            evento.nombre = (String) rowEv[1];
            evento.precio = Double.parseDouble(rowEv[2].toString());

            List<Object[]> rawAccesos = db.executeQueryArray(sqlAccesos, idAgencia, evento.id);
            for (Object[] rowAcc : rawAccesos) {
                InformeAgenciaDTO.AccesoEmpresa acceso = new InformeAgenciaDTO.AccesoEmpresa();
                acceso.nombreEmpresa = (String) rowAcc[0];
                acceso.tipoAcceso = (String) rowAcc[1];
                acceso.estadoPago = (String) rowAcc[2];
                acceso.tieneTarifaPlana = ((int) rowAcc[3]) == 1;
                acceso.cuotaMensual = rowAcc[4] != null ? Double.parseDouble(rowAcc[4].toString()) : 0.0;
                
                evento.accesos.add(acceso);
            }
            listaEventos.add(evento);
        }
        return listaEventos;
    }
}