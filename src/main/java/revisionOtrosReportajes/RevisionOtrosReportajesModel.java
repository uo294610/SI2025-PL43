package revisionOtrosReportajes;

import java.util.List;
import giis.demo.util.Database;
import nico_EntregarReportEvento.ReporteroDisplayDTO;
import nico_ModificaEntrega_33610.ArchivoMultimediaDTO;
import nico_ModificaEntrega_33610.ReportajeEdicionDTO;

public class RevisionOtrosReportajesModel {
    private Database db = new Database();

    public List<ReporteroDisplayDTO> getListaReporteros() {
        return db.executeQueryPojo(ReporteroDisplayDTO.class, "SELECT id, nombre FROM Reportero");
    }

    // CORRECCIÓN: Busca todas las revisiones pendientes y en curso del revisor
    public List<ReportajeRevisionDTO> getAllPendingRevisiones(int idRevisor) {
        String sql = "SELECT r.id as id_revision, rep.id as id_reportaje, rep.titulo as titulo_reportaje, " +
                     "autor.nombre as autor_nombre, r.estado as estado_revision " +
                     "FROM Revision r " +
                     "INNER JOIN Reportaje rep ON r.reportaje_id = rep.id " +
                     "INNER JOIN Reportero autor ON rep.reportero_entrega_id = autor.id " +
                     "WHERE r.revisor_id = ? AND r.estado IN ('PENDIENTE', 'EN_CURSO')";
        return db.executeQueryPojo(ReportajeRevisionDTO.class, sql, idRevisor);
    }

    public ReportajeEdicionDTO getTextosReportaje(int idReportaje) {
        String sql = "SELECT r.id as reportaje_id, r.reportero_entrega_id, r.titulo, v.subtitulo, v.cuerpo " +
                     "FROM Reportaje r " +
                     "INNER JOIN VersionReportaje v ON r.id = v.reportaje_id " +
                     "WHERE r.id = ? " +
                     "ORDER BY v.fecha_hora DESC LIMIT 1"; 
        List<ReportajeEdicionDTO> lista = db.executeQueryPojo(ReportajeEdicionDTO.class, sql, idReportaje);
        return lista.isEmpty() ? null : lista.get(0);
    }

    public List<ArchivoMultimediaDTO> getMultimedia(int idReportaje) {
        // CORRECCIÓN SQL: Mapea i.tipo a 'autor_nombre' para la columna "Tipo" de la tabla
        String sql = "SELECT i.ruta_archivo, i.estado, i.tipo as autor_nombre " + 
                     "FROM Imagen i WHERE i.reportaje_id = ?";
        return db.executeQueryPojo(ArchivoMultimediaDTO.class, sql, idReportaje);
    }

    public List<ComentarioDTO> getComentarios(int idRevision) {
        // CORRECCIÓN SQL: Mapea la fecha_hora para la columna "Fecha" de la tabla
        String sql = "SELECT fecha_hora, texto FROM Comentario WHERE revision_id = ? ORDER BY fecha_hora ASC";
        return db.executeQueryPojo(ComentarioDTO.class, sql, idRevision);
    }

    public void añadirComentario(int idRevision, String texto) {
        int nuevoId = getUltimoId("Comentario") + 1;
        String sql = "INSERT INTO Comentario (id, revision_id, texto, fecha_hora) VALUES (?, ?, ?, ?)";
        db.executeUpdate(sql, nuevoId, idRevision, texto, new java.sql.Timestamp(System.currentTimeMillis()));
        
        // Si estaba en PENDIENTE, al comentar pasa a estar EN_CURSO automáticamente
        db.executeUpdate("UPDATE Revision SET estado = 'EN_CURSO' WHERE id = ? AND estado = 'PENDIENTE'", idRevision);
    }

    public void finalizarRevision(int idRevision) {
        String sql = "UPDATE Revision SET estado = 'FINALIZADA' WHERE id = ?";
        db.executeUpdate(sql, idRevision);
    }

    private int getUltimoId(String tabla) {
        String sql = "SELECT max(id) FROM " + tabla;
        List<Object[]> result = db.executeQueryArray(sql);
        if (result != null && !result.isEmpty() && result.get(0)[0] != null) {
            return Integer.parseInt(result.get(0)[0].toString());
        }
        return 0;
    }
}