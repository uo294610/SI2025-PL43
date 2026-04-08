package alex_DietasReportero;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import giis.demo.util.Database;

public class DietasModel {
    private Database db = new Database();

    // Obtener lista de reporteros para simular el "Login"
    public List<Object[]> getListaReporteros() {
        return db.executeQueryArray("SELECT id, nombre FROM Reportero ORDER BY nombre");
    }

    // Obtener la información base del reportero seleccionado
    public Object[] getInfoReportero(int idReportero) {
        String sql = "SELECT r.id, r.nombre, p.nombre AS provincia, pa.nombre AS pais, a.nombre AS agencia, p.id AS provincia_id " +
                     "FROM Reportero r " +
                     "JOIN Provincia p ON r.provincia_id = p.id " +
                     "JOIN Pais pa ON p.pais_id = pa.id " +
                     "LEFT JOIN AgenciaPrensa a ON r.agencia_id = a.id " + // LEFT JOIN por si es Freelance (no tiene agencia)
                     "WHERE r.id = ?";
        List<Object[]> res = db.executeQueryArray(sql, idReportero);
        return res.isEmpty() ? null : res.get(0);
    }

    // Obtener los eventos asignados y calcular las dietas
    public List<DietaDTO> getDietasEventos(int idReportero, int idProvinciaBase) {
        String sql = "SELECT ev.id, ev.fecha, ev.fecha_fin, ev.nombre, " +
                     "pe.nombre AS prov_evento, pae.nombre AS pais_evento, " +
                     "pae.coste_manutencion, pe.coste_alojamiento, pe.id AS prov_evento_id " +
                     "FROM Evento ev " +
                     "JOIN Asignacion asig ON ev.id = asig.evento_id " +
                     "JOIN Provincia pe ON ev.provincia_id = pe.id " +
                     "JOIN Pais pae ON pe.pais_id = pae.id " +
                     "WHERE asig.reportero_id = ? " +
                     "ORDER BY ev.fecha ASC";
                     
        List<Object[]> rawData = db.executeQueryArray(sql, idReportero);
        List<DietaDTO> lista = new ArrayList<>();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Object[] row : rawData) {
            DietaDTO dto = new DietaDTO();
            dto.setEventoId((int) row[0]);
            
            // Fechas y Días
            String fechaIniStr = row[1].toString();
            String fechaFinStr = row[2].toString();
            LocalDate inicio = LocalDate.parse(fechaIniStr, formatter);
            LocalDate fin = LocalDate.parse(fechaFinStr, formatter);
            
            // Los días se cuentan sumando 1 (ej: del día 1 al día 1 es 1 día trabajado)
            int dias = (int) ChronoUnit.DAYS.between(inicio, fin) + 1;
            dto.setFecha(fechaIniStr);
            dto.setDias(dias);
            
            dto.setEventoNombre((String) row[3]);
            dto.setUbicacion(row[4] + " (" + row[5] + ")");
            
            // Tarifas extraídas de la base de datos
            double manutencionDiaria = Double.parseDouble(row[6].toString());
            double alojamientoDiario = Double.parseDouble(row[7].toString());
            int idProvinciaEvento = (int) row[8];
            
            // REGLA DE NEGOCIO: ¿Aplica alojamiento?
            boolean aplicaAlojamiento = (idProvinciaEvento != idProvinciaBase);
            dto.setAplicaAlojamiento(aplicaAlojamiento);
            
            dto.setTarifaManutencion(manutencionDiaria);
            dto.setTarifaAlojamiento(aplicaAlojamiento ? alojamientoDiario : 0.0);
            
            // Cálculos Totales
            dto.setTotalManutencion(dias * manutencionDiaria);
            dto.setTotalAlojamiento(aplicaAlojamiento ? (dias * alojamientoDiario) : 0.0);
            dto.setTotalApercibir(dto.getTotalManutencion() + dto.getTotalAlojamiento());
            
            lista.add(dto);
        }
        return lista;
    }
}