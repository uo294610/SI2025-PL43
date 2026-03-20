package revisionOtrosReportajes;

import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import giis.demo.util.SwingUtil;
import nico_EntregarReportEvento.ReporteroDisplayDTO;
import nico_ModificaEntrega_33610.ArchivoMultimediaDTO;
import nico_ModificaEntrega_33610.ReportajeEdicionDTO;

public class RevisionOtrosReportajesController {
    private RevisionOtrosReportajesModel model;
    private RevisionOtrosReportajesView view;
    
    private ReportajeRevisionDTO revisionActual;

    public RevisionOtrosReportajesController(RevisionOtrosReportajesModel m, RevisionOtrosReportajesView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        cargarReporteros();
        configurarListeners();
        view.getFrame().setVisible(true);
        recargarTablaRevisiones();
    }

    private void configurarListeners() {
        view.getCbRevisores().addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) recargarTablaRevisiones();
        });

        view.getTabRevisiones().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarDetalleRevision();
        });

        view.getBtnAñadirComentario().addActionListener(e -> guardarComentario());
        view.getBtnFinalizarRevision().addActionListener(e -> finalizarRevision());
    }

    private void cargarReporteros() {
        List<ReporteroDisplayDTO> lista = model.getListaReporteros();
        for (ReporteroDisplayDTO rep : lista) {
            view.getCbRevisores().addItem(rep);
        }
    }

    private void recargarTablaRevisiones() {
        ReporteroDisplayDTO revisor = (ReporteroDisplayDTO) view.getCbRevisores().getSelectedItem();
        if (revisor == null) return;
        
        int idRevisor = Integer.parseInt(revisor.getId());
        
        List<ReportajeRevisionDTO> revisiones = model.getAllPendingRevisiones(idRevisor);
        
        // --- AQUÍ ESTÁ EL CAMBIO: Añadida la columna nombre_evento ---
        view.getTabRevisiones().setModel(SwingUtil.getTableModelFromPojos(revisiones, 
            new String[] {"id_revision", "titulo_reportaje", "nombre_evento", "autor_nombre", "estado_revision"})); 
            
        // Como hemos metido una columna nueva, "estado_revision" ahora es la número 4 (empezando desde 0)
        view.getTabRevisiones().getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                String originalValue = (String) value;
                String mappedValue = originalValue;
                if ("PENDIENTE".equals(originalValue)) {
                    mappedValue = "Nueva";
                } else if ("EN_CURSO".equals(originalValue)) {
                    mappedValue = "Sin Finalizar";
                }
                return super.getTableCellRendererComponent(table, mappedValue, isSelected, hasFocus, row, column);
            }
        });

        // Renombrar todas las cabeceras bonitas
        view.getTabRevisiones().getColumnModel().getColumn(0).setHeaderValue("ID");
        view.getTabRevisiones().getColumnModel().getColumn(1).setHeaderValue("Título");
        view.getTabRevisiones().getColumnModel().getColumn(2).setHeaderValue("Evento"); // NUEVA
        view.getTabRevisiones().getColumnModel().getColumn(3).setHeaderValue("Autor");
        view.getTabRevisiones().getColumnModel().getColumn(4).setHeaderValue("Revisión");
        view.getTabRevisiones().getTableHeader().repaint();

        SwingUtil.autoAdjustColumns(view.getTabRevisiones());
        limpiarVisor();
    }

    private void cargarDetalleRevision() {
        int fila = view.getTabRevisiones().getSelectedRow();
        if (fila < 0) return;

        int idRevision = (int) view.getTabRevisiones().getValueAt(fila, 0);
        
        ReporteroDisplayDTO revisor = (ReporteroDisplayDTO) view.getCbRevisores().getSelectedItem();
        List<ReportajeRevisionDTO> revs = model.getAllPendingRevisiones(Integer.parseInt(revisor.getId()));
        for(ReportajeRevisionDTO r : revs) {
            if(r.getId_revision() == idRevision) {
                revisionActual = r;
                break;
            }
        }

        if (revisionActual != null) {
            ReportajeEdicionDTO textos = model.getTextosReportaje(revisionActual.getId_reportaje());
            if (textos != null) {
                view.getTxtTitulo().setText(textos.getTitulo());
                view.getTxtSubtitulo().setText(textos.getSubtitulo());
                view.getAreaCuerpo().setText(textos.getCuerpo());
            }

            List<ArchivoMultimediaDTO> multimedia = model.getMultimedia(revisionActual.getId_reportaje());
            view.getTabMultimedia().setModel(SwingUtil.getTableModelFromPojos(multimedia, 
                new String[] {"autor_nombre", "ruta_archivo", "estado"})); 
            
            view.getTabMultimedia().getColumnModel().getColumn(0).setHeaderValue("Tipo");
            view.getTabMultimedia().getColumnModel().getColumn(1).setHeaderValue("Ruta");
            view.getTabMultimedia().getColumnModel().getColumn(2).setHeaderValue("Estado");
            view.getTabMultimedia().getTableHeader().repaint();
            
            SwingUtil.autoAdjustColumns(view.getTabMultimedia());
            
            recargarComentarios();
        }
    }

    private void recargarComentarios() {
        if (revisionActual == null) return;
        List<ComentarioDTO> comentarios = model.getComentarios(revisionActual.getId_revision());
        view.getTabComentarios().setModel(SwingUtil.getTableModelFromPojos(comentarios, 
            new String[] {"fecha_hora", "texto"}));
            
        view.getTabComentarios().getColumnModel().getColumn(0).setHeaderValue("Fecha");
        view.getTabComentarios().getColumnModel().getColumn(1).setHeaderValue("Comentario");
        view.getTabComentarios().getTableHeader().repaint();
            
        SwingUtil.autoAdjustColumns(view.getTabComentarios());
    }

    private void guardarComentario() {
        if (revisionActual == null) {
            JOptionPane.showMessageDialog(view.getFrame(), "Selecciona una revisión primero.");
            return;
        }
        
        String texto = view.getAreaNuevoComentario().getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(view.getFrame(), "El comentario no puede estar vacío.");
            return;
        }

        model.añadirComentario(revisionActual.getId_revision(), texto);
        view.getAreaNuevoComentario().setText(""); 
        JOptionPane.showMessageDialog(view.getFrame(), "Comentario añadido.");
        
        recargarComentarios();
        
        if ("PENDIENTE".equals(revisionActual.getEstado_revision())) {
             recargarTablaRevisiones();
        }
    }

    private void finalizarRevision() {
        if (revisionActual == null) {
            JOptionPane.showMessageDialog(view.getFrame(), "Selecciona una revisión primero.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), 
            "¿Seguro que quieres marcar esta revisión como Finalizada? Desaparecerá de tu lista.", 
            "Finalizar", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.finalizarRevision(revisionActual.getId_revision());
            JOptionPane.showMessageDialog(view.getFrame(), "Revisión Finalizada.");
            recargarTablaRevisiones();
        }
    }

    private void limpiarVisor() {
        view.getTxtTitulo().setText("");
        view.getTxtSubtitulo().setText("");
        view.getAreaCuerpo().setText("");
        view.getTabMultimedia().setModel(new javax.swing.table.DefaultTableModel());
        view.getTabComentarios().setModel(new javax.swing.table.DefaultTableModel());
        revisionActual = null;
    }
}