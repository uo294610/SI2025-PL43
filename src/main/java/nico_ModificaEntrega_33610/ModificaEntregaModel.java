package nico_ModificaEntrega_33610;

import java.util.List;
import giis.demo.util.Database;
import nico_EntregarReportEvento.EventoResumenDTO;
import nico_EntregarReportEvento.ReporteroDisplayDTO;
import nico_EntregarReportEvento.VersionReportajeEntity;

public class ModificaEntregaModel {
    private Database db = new Database();

    public List<ReporteroDisplayDTO> getListaReporteros() {
        return db.executeQueryPojo(ReporteroDisplayDTO.class, "SELECT id, nombre FROM Reportero");
    }

    public List<EventoResumenDTO> getEventosPendientes(int idReportero) {
        String sql = "SELECT e.id, e.nombre, e.fecha FROM Evento e " +
                     "INNER JOIN Asignacion a ON e.id = a.evento_id " +
                     "WHERE a.reportero_id = ? AND NOT EXISTS (SELECT 1 FROM Reportaje r WHERE r.reportero_entrega_id = ?)";
        return db.executeQueryPojo(EventoResumenDTO.class, sql, idReportero, idReportero);
    }

    public List<EventoResumenDTO> getEventosEntregados(int idReportero) {
        String sql = "SELECT e.id, e.nombre, e.fecha FROM Evento e " +
                     "INNER JOIN Asignacion a ON e.id = a.evento_id " +
                     "WHERE a.reportero_id = ? AND EXISTS (SELECT 1 FROM Reportaje r WHERE r.reportero_entrega_id = ?)";
        return db.executeQueryPojo(EventoResumenDTO.class, sql, idReportero, idReportero);
    }

    public ReportajeEdicionDTO getUltimaVersion(int idReportero) {
        String sql = "SELECT r.id as reportaje_id, r.reportero_entrega_id, r.revision_solicitada, r.titulo, v.subtitulo, v.cuerpo " +
                     "FROM Reportaje r " +
                     "INNER JOIN VersionReportaje v ON r.id = v.reportaje_id " +
                     "WHERE r.reportero_entrega_id = ? " +
                     "ORDER BY v.fecha_hora DESC LIMIT 1"; 
        List<ReportajeEdicionDTO> lista = db.executeQueryPojo(ReportajeEdicionDTO.class, sql, idReportero);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public void insertarNuevaVersion(VersionReportajeEntity version) {
        String sql = "INSERT INTO VersionReportaje (id, reportaje_id, subtitulo, cuerpo, fecha_hora, que_cambio) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        db.executeUpdate(sql, version.getId(), version.getReportaje_id(), version.getSubtitulo(), 
                         version.getCuerpo(), version.getFecha_hora(), version.getQue_cambio());
    }
    
    public void insertarReportaje(int idReportaje, String titulo, int idReportero) {
        String sql = "INSERT INTO Reportaje (id, titulo, reportero_entrega_id, estado, revision_solicitada) VALUES (?, ?, ?, 'NODESCARGADO', FALSE)";
        db.executeUpdate(sql, idReportaje, titulo, idReportero);
    }

    public int getUltimoId(String tabla) {
        String sql = "SELECT max(id) FROM " + tabla;
        List<Object[]> result = db.executeQueryArray(sql);
        if (result != null && !result.isEmpty() && result.get(0)[0] != null) {
            return Integer.parseInt(result.get(0)[0].toString());
        }
        return 0;
    }

    public List<ArchivoMultimediaDTO> getMultimedia(int idReportero, String tipo) {
        String sql = "SELECT i.ruta_archivo, i.estado, r.nombre as autor_nombre " +
                     "FROM Imagen i " +
                     "INNER JOIN Reportero r ON i.reportero_id = r.id " +
                     "WHERE i.reportero_id = ? AND i.tipo = ?";
        return db.executeQueryPojo(ArchivoMultimediaDTO.class, sql, idReportero, tipo);
    }

    public void insertarMultimedia(int idReportero, String ruta, String tipo) {
        int nuevoId = getUltimoId("Imagen") + 1;
        String sql = "INSERT INTO Imagen (id, reportero_id, ruta_archivo, estado, tipo) " +
                     "VALUES (?, ?, ?, 'BORRADOR', ?)";
        db.executeUpdate(sql, nuevoId, idReportero, ruta, tipo);
    }

    public void eliminarMultimedia(String ruta) {
        String sql = "DELETE FROM Imagen WHERE ruta_archivo = ?";
        db.executeUpdate(sql, ruta);
    }

    public void fijarMultimediaDefinitiva(String ruta) {
        String sql = "UPDATE Imagen SET estado = 'DEFINITIVA' WHERE ruta_archivo = ?";
        db.executeUpdate(sql, ruta);
    }

    // --- MÉTODOS REVISIÓN AUTOMÁTICA ---
    public void solicitarRevisionAutomatica(int idReportaje, int idEvento, int idAutor) {
        // 1. Bloqueamos el reportaje en la base de datos
        String sqlUpdate = "UPDATE Reportaje SET revision_solicitada = TRUE WHERE id = ?";
        db.executeUpdate(sqlUpdate, idReportaje);

        // 2. Buscamos todos los reporteros asignados a este evento
        String sqlReporteros = "SELECT reportero_id FROM Asignacion WHERE evento_id = ?";
        List<Object[]> asignados = db.executeQueryArray(sqlReporteros, idEvento);
        
        boolean estaSolo = (asignados.size() == 1);
        int nuevoIdRevision = getUltimoId("Revision") + 1;
        String sqlInsert = "INSERT INTO Revision (id, reportaje_id, revisor_id, estado) VALUES (?, ?, ?, 'PENDIENTE')";

        // 3. Repartimos las revisiones a todos menos al autor (salvo que esté solo)
        for (Object[] row : asignados) {
            int idRevisor = Integer.parseInt(row[0].toString());
            
            if (estaSolo || idRevisor != idAutor) {
                db.executeUpdate(sqlInsert, nuevoIdRevision, idReportaje, idRevisor);
                nuevoIdRevision++;
            }
        }
    }
}