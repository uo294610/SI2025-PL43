package diego_ReportajesEvento_33607;

import java.util.List;
import giis.demo.util.Database;

public class EmpresaModel {
    private Database db = new Database();

    /**
     * Obtiene eventos que tienen un reportaje y a los que la empresa tiene acceso.
     */
    public List<EventoAccesoDTO> getEventosConAcceso(int idEmpresa) {
        // En SQLite TRUE se guarda como 1. 
        // Cruzamos Evento con Reportaje (para asegurar que existe) y con Ofrecimiento (para el acceso)
        String sql = "SELECT e.id, e.nombre, e.fecha "
                   + "FROM Evento e "
                   + "JOIN Reportaje r ON e.id = r.evento_id "
                   + "JOIN Ofrecimiento o ON e.id = o.evento_id "
                   + "WHERE o.empresa_id = ? AND o.acceso = 1 " 
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
                   + "WHERE r.evento_id = ? "
                   + "ORDER BY v.fecha_hora DESC " // Ordenamos de más reciente a más antigua
                   + "LIMIT 1";                    // Nos quedamos solo con la primera (la última versión)
        
        List<ReportajeDetalleDTO> resultado = db.executeQueryPojo(ReportajeDetalleDTO.class, sql, idEvento);
        if (resultado.isEmpty()) {
            return null; // Por si no hubiera versiones
        }
        return resultado.get(0);
    }
}
