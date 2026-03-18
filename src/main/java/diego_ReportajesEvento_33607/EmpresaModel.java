package diego_ReportajesEvento_33607;

import java.util.List;
import giis.demo.util.Database;

public class EmpresaModel {
    private Database db = new Database();

    /**
     * Obtiene eventos que tienen un reportaje ACEPTADO y a los que la empresa tiene acceso.
     */
    public List<EventoAccesoDTO> getEventosConAcceso(int idEmpresa) {
        String sql = "SELECT e.id, e.nombre, e.fecha "
                   + "FROM Evento e "
                   + "JOIN EvaluacionReportaje er ON e.id = er.evento_id "
                   + "JOIN Ofrecimiento o ON e.id = o.evento_id "
                   + "WHERE o.empresa_id = ? AND o.acceso = 1 AND er.estado = 'ACEPTADO' " 
                   + "ORDER BY e.fecha";
        return db.executeQueryPojo(EventoAccesoDTO.class, sql, idEmpresa);
    }

    /**
     * Obtiene SOLO la última versión del reportaje de un evento concreto.
     */
    public ReportajeDetalleDTO getUltimaVersionReportaje(int idEvento) {
        String sql = "SELECT r.titulo, v.subtitulo, v.cuerpo "
                   + "FROM Reportaje r "
                   + "JOIN VersionReportaje v ON r.id = v.reportaje_id "
                   + "JOIN EvaluacionReportaje er ON r.id = er.reportaje_id "
                   + "WHERE er.evento_id = ? "
                   + "ORDER BY v.fecha_hora DESC " 
                   + "LIMIT 1";                    
        
        List<ReportajeDetalleDTO> resultado = db.executeQueryPojo(ReportajeDetalleDTO.class, sql, idEvento);
        if (resultado.isEmpty()) {
            return null; 
        }
        return resultado.get(0);
    }

    /**
     * Obtiene los archivos multimedia en estado DEFINITIVA asociados al reportaje del evento.
     */
    public List<MultimediaDTO> getMultimediaDefinitiva(int idEvento) {
        // CORREGIDO: Ahora cruzamos Imagen directamente con el Reportaje usando EvaluacionReportaje,
        // aprovechando la nueva columna reportaje_id en la tabla Imagen.
        String sql = "SELECT i.ruta_archivo AS ruta, i.tipo "
                   + "FROM Imagen i "
                   + "JOIN EvaluacionReportaje er ON i.reportaje_id = er.reportaje_id "
                   + "WHERE er.evento_id = ? AND i.estado = 'DEFINITIVA'";
        return db.executeQueryPojo(MultimediaDTO.class, sql, idEvento);
    }
    
    /**
     * Marca el reportaje como DESCARGADO
     */
    public void marcarComoDescargado(int idEvento) {
        String sql = "UPDATE Reportaje SET estado = 'DESCARGADO' "
                   + "WHERE id IN (SELECT reportaje_id FROM EvaluacionReportaje WHERE evento_id = ?)";
        db.executeUpdate(sql, idEvento);
    }
}