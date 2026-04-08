package nico_ModificaEntrega_33610;

import java.awt.Color;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import giis.demo.util.SwingUtil;
import nico_EntregarReportEvento.EventoResumenDTO;
import nico_EntregarReportEvento.ReporteroDisplayDTO;
import nico_EntregarReportEvento.VersionReportajeEntity;

public class ModificaEntregaController {
    private ModificaEntregaModel model;
    private ModificaEntregaView view;
    private ReportajeEdicionDTO reportajeActual;
    private int idEventoSeleccionado = -1;

    public ModificaEntregaController(ModificaEntregaModel m, ModificaEntregaView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        cargarReporteros();
        configurarListeners();
        cambiarModo(true); 
        view.getFrame().setVisible(true);
    }

    private void configurarListeners() {
        view.getCbReporteros().addItemListener(e -> {
            if (e.getStateChange() == java.awt.event.ItemEvent.SELECTED) {
                recargarTabla();
            }
        });
        
        view.getRdbtnPendientes().addActionListener(e -> cambiarModo(true));
        view.getRdbtnEntregados().addActionListener(e -> cambiarModo(false));

        view.getTabEventos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) seleccionarEvento();
        });

        view.getBtnGuardarCambio().addActionListener(e -> guardarModificacion());
        view.getBtnEntregar().addActionListener(e -> realizarNuevaEntrega());
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());

        view.getBtnAnadirImagen().addActionListener(e -> anadirMultimedia("imagen"));
        view.getBtnEliminarImagen().addActionListener(e -> eliminarMultimedia(view.getTabImagenes()));
        view.getBtnFijarImgDefinitiva().addActionListener(e -> fijarDefinitivo(view.getTabImagenes()));

        view.getBtnAnadirVideo().addActionListener(e -> anadirMultimedia("video"));
        view.getBtnEliminarVideo().addActionListener(e -> eliminarMultimedia(view.getTabVideos()));
        view.getBtnFijarVidDefinitivo().addActionListener(e -> fijarDefinitivo(view.getTabVideos()));

        view.getBtnSolicitarRevision().addActionListener(e -> procesarSolicitudRevision());
        view.getBtnFinalizarReportaje().addActionListener(e -> finalizarReportaje());
    }

    private void cargarReporteros() {
        List<ReporteroDisplayDTO> lista = model.getListaReporteros();
        view.getCbReporteros().removeAllItems();
        for (ReporteroDisplayDTO rep : lista) {
            view.getCbReporteros().addItem(rep);
        }
    }

    private void cambiarModo(boolean isPendiente) {
        limpiarFormulario();
        if (isPendiente) {
            view.getBtnEntregar().setVisible(true);
            view.getBtnGuardarCambio().setVisible(false);
            view.getBtnSolicitarRevision().setVisible(false);
            view.getLblPermisoModificar().setText("Modo: Nueva Entrega");
            view.getTxtTitulo().setEditable(true);
        } else {
            view.getBtnEntregar().setVisible(false);
            view.getTxtTitulo().setEditable(false); 
        }
        recargarTabla();
    }

    private void recargarTabla() {
        ReporteroDisplayDTO rep = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        if (rep == null) return;
        
        int idRep = Integer.parseInt(rep.getId());
        List<EventoResumenDTO> eventos = view.getRdbtnPendientes().isSelected() ? 
                                         model.getEventosPendientes(idRep) : 
                                         model.getEventosEntregados(idRep);
                                         
        view.getTabEventos().setModel(SwingUtil.getTableModelFromPojos(eventos, new String[] {"id", "nombre", "fecha"}));
        SwingUtil.autoAdjustColumns(view.getTabEventos());
        limpiarFormulario();
    }

    private void seleccionarEvento() {
        int fila = view.getTabEventos().getSelectedRow();
        if (fila < 0 || view.getRdbtnPendientes().isSelected()) {
            limpiarTablasMultimedia();
            view.getBtnSolicitarRevision().setVisible(false);
            limpiarTablaRevisiones();
            return;
        }

        idEventoSeleccionado = (int) view.getTabEventos().getValueAt(fila, 0);
        ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        int idRepActual = Integer.parseInt(repCombo.getId());
        reportajeActual = model.getUltimaVersion(idRepActual); 

        if (reportajeActual != null) {
            view.getTxtTitulo().setText(reportajeActual.getTitulo());
            view.getTxtSubtitulo().setText(reportajeActual.getSubtitulo());
            view.getAreaCuerpo().setText(reportajeActual.getCuerpo());

            recargarTablasMultimedia();
            cargarRevisionesCompaneros(idRepActual, idEventoSeleccionado);
            
            boolean isResponsable = model.isReporteroResponsable(idRepActual, idEventoSeleccionado);
            boolean isFinalizado = model.isReportajeFinalizado(reportajeActual.getReportaje_id());

            if (isFinalizado) {
                view.getLblPermisoModificar().setText("🔒 Reportaje FINALIZADO. Edición bloqueada.");
                view.getLblPermisoModificar().setForeground(Color.RED);
                congelarEdicion(true);
            } else if (isResponsable) {
                view.getLblPermisoModificar().setText("👑 Responsable: Tienes permisos totales (incluye Título).");
                view.getLblPermisoModificar().setForeground(new Color(0, 102, 204));
                congelarEdicion(false);
                
                if (reportajeActual.isRevision_solicitada()) {
                    view.getBtnSolicitarRevision().setVisible(false);
                }
            } else if (reportajeActual.isRevision_solicitada()) {
                view.getLblPermisoModificar().setText("🔒 Solicitaste Revisión: No puedes modificar.");
                view.getLblPermisoModificar().setForeground(Color.RED);
                congelarEdicion(true);
            } else {
                if (idRepActual == reportajeActual.getReportero_entrega_id()) {
                    view.getLblPermisoModificar().setText("✓ Eres el autor. Puedes modificar y pedir revisión.");
                    view.getLblPermisoModificar().setForeground(Color.GREEN.darker());
                    congelarEdicion(false);
                } else {
                    view.getLblPermisoModificar().setText("✗ Sólo el autor original puede modificar.");
                    view.getLblPermisoModificar().setForeground(Color.RED);
                    congelarEdicion(true);
                }
            }
        }
    }

    private void cargarRevisionesCompaneros(int idReportero, int idEvento) {
        boolean isResponsable = model.isReporteroResponsable(idReportero, idEvento);
        
        if (!isResponsable) {
            limpiarTablaRevisiones();
            view.getLblEstadoFinalizacion().setText("Estado: No eres el Responsable de este evento.");
            view.getLblEstadoFinalizacion().setForeground(Color.GRAY);
            view.getBtnFinalizarReportaje().setEnabled(false);
            return;
        }

        List<Object[]> revisiones = model.getRevisionesReportaje(reportajeActual.getReportaje_id());
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Revisor", "Estado", "Último Comentario"}, 0);
        boolean todosFinalizados = true;
        
        for (Object[] rev : revisiones) {
            tableModel.addRow(new Object[]{rev[0], rev[1], rev[2] != null ? rev[2] : ""});
            if (!"FINALIZADA".equals(rev[1])) {
                todosFinalizados = false;
            }
        }
        
        view.getTabRevisionesCompaneros().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTabRevisionesCompaneros());

        if (model.isReportajeFinalizado(reportajeActual.getReportaje_id())) {
            view.getLblEstadoFinalizacion().setText("Estado: 🏆 FINALIZADO");
            view.getLblEstadoFinalizacion().setForeground(new Color(0, 153, 51));
            view.getBtnFinalizarReportaje().setEnabled(false);
        } else if (revisiones.isEmpty()) {
            view.getLblEstadoFinalizacion().setText("Estado: Aún no se ha solicitado revisión.");
            view.getLblEstadoFinalizacion().setForeground(Color.BLACK);
            view.getBtnFinalizarReportaje().setEnabled(false);
        } else if (todosFinalizados) {
            view.getLblEstadoFinalizacion().setText("Estado: ✅ Listo para finalizar");
            view.getLblEstadoFinalizacion().setForeground(Color.GREEN.darker());
            view.getBtnFinalizarReportaje().setEnabled(true);
        } else {
            view.getLblEstadoFinalizacion().setText("Estado: ⚠️ Esperando revisiones finalizadas");
            view.getLblEstadoFinalizacion().setForeground(Color.ORANGE.darker());
            view.getBtnFinalizarReportaje().setEnabled(false);
        }
    }

    private void congelarEdicion(boolean congelar) {
        view.getTxtTitulo().setEditable(!congelar);
        view.getTxtSubtitulo().setEditable(!congelar);
        view.getAreaCuerpo().setEditable(!congelar);
        view.getBtnGuardarCambio().setVisible(!congelar);
        
        view.getBtnAnadirImagen().setEnabled(!congelar);
        view.getBtnEliminarImagen().setEnabled(!congelar);
        view.getBtnFijarImgDefinitiva().setEnabled(!congelar);
        view.getBtnAnadirVideo().setEnabled(!congelar);
        view.getBtnEliminarVideo().setEnabled(!congelar);
        view.getBtnFijarVidDefinitivo().setEnabled(!congelar);
        
        view.getBtnSolicitarRevision().setVisible(!congelar);
    }

    private void limpiarTablaRevisiones() {
        view.getTabRevisionesCompaneros().setModel(new DefaultTableModel(new Object[]{"Revisor", "Estado", "Último Comentario"}, 0));
    }

    private void limpiarTablasMultimedia() {
        view.getTabImagenes().setModel(new DefaultTableModel());
        view.getTabVideos().setModel(new DefaultTableModel());
    }

    private void recargarTablasMultimedia() {
        ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        if (repCombo == null) return;
        int idReportero = Integer.parseInt(repCombo.getId());

        List<ArchivoMultimediaDTO> imagenes = model.getMultimedia(idReportero, "imagen");
        List<ArchivoMultimediaDTO> videos = model.getMultimedia(idReportero, "video");

        String[] columnas = {"ruta_archivo", "estado", "autor_nombre"};
        
        view.getTabImagenes().setModel(SwingUtil.getTableModelFromPojos(imagenes, columnas));
        SwingUtil.autoAdjustColumns(view.getTabImagenes());

        view.getTabVideos().setModel(SwingUtil.getTableModelFromPojos(videos, columnas));
        SwingUtil.autoAdjustColumns(view.getTabVideos());
    }

    private void anadirMultimedia(String tipo) {
        if (reportajeActual == null) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar " + tipo);
        if (tipo.equals("imagen")) chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg"));
        else chooser.setFileFilter(new FileNameExtensionFilter("Vídeos (MP4, AVI)", "mp4", "avi"));

        if (chooser.showOpenDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            String ruta = archivo.getAbsolutePath();
            String nombreNuevoArchivo = archivo.getName();

            JTable tablaDestino = tipo.equals("imagen") ? view.getTabImagenes() : view.getTabVideos();
            for (int i = 0; i < tablaDestino.getRowCount(); i++) {
                String rutaExistente = (String) tablaDestino.getValueAt(i, 0); 
                File archivoExistente = new File(rutaExistente);
                if (rutaExistente.equals(ruta) || archivoExistente.getName().equals(nombreNuevoArchivo)) {
                    JOptionPane.showMessageDialog(view.getFrame(), "Error: Ya existe un(a) " + tipo + " con el nombre '" + nombreNuevoArchivo + "' en la lista.", "Archivo Duplicado", JOptionPane.ERROR_MESSAGE);
                    return; 
                }
            }

            ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
            int idReportero = Integer.parseInt(repCombo.getId());

            model.insertarMultimedia(idReportero, ruta, tipo);
            recargarTablasMultimedia();
            JOptionPane.showMessageDialog(view.getFrame(), tipo.toUpperCase() + " añadida como BORRADOR.");
        }
    }

    private void eliminarMultimedia(javax.swing.JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Selecciona un archivo de la tabla primero.");
            return;
        }
        String ruta = (String) tabla.getValueAt(fila, 0);
        String estado = (String) tabla.getValueAt(fila, 1);
        
        if (estado.equals("DEFINITIVA")) {
            JOptionPane.showMessageDialog(view.getFrame(), "No puedes eliminar un archivo fijado como DEFINITIVO.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), "¿Seguro que quieres borrar este archivo?", "Borrar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            model.eliminarMultimedia(ruta);
            recargarTablasMultimedia();
        }
    }

    private void fijarDefinitivo(javax.swing.JTable tabla) {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        String ruta = (String) tabla.getValueAt(fila, 0);
        model.fijarMultimediaDefinitiva(ruta);
        recargarTablasMultimedia();
    }

    private void guardarModificacion() {
        if (reportajeActual == null) return;
        
        String nuevoTit = view.getTxtTitulo().getText().trim();
        String nuevoSub = view.getTxtSubtitulo().getText().trim();
        String nuevoCuerpo = view.getAreaCuerpo().getText().trim();
        
        boolean tituloCambiado = !nuevoTit.equals(reportajeActual.getTitulo());
        String cambios = "";
        
        if (tituloCambiado) cambios += "título";
        if (!nuevoSub.equals(reportajeActual.getSubtitulo())) {
            if (!cambios.isEmpty()) cambios += ", ";
            cambios += "subtítulo";
        }
        if (!nuevoCuerpo.equals(reportajeActual.getCuerpo())) {
            if (!cambios.isEmpty()) cambios += " y "; 
            cambios += "cuerpo";
        }
        
        if (cambios.isEmpty()) {
            JOptionPane.showMessageDialog(view.getFrame(), "No has modificado nada.");
            return;
        }

        try {
            if (tituloCambiado) {
                model.actualizarTituloReportaje(reportajeActual.getReportaje_id(), nuevoTit);
            }

            VersionReportajeEntity nuevaVersion = new VersionReportajeEntity();
            nuevaVersion.setId(model.getUltimoId("VersionReportaje") + 1);
            nuevaVersion.setReportaje_id(reportajeActual.getReportaje_id());
            nuevaVersion.setSubtitulo(nuevoSub);
            nuevaVersion.setCuerpo(nuevoCuerpo);
            nuevaVersion.setFecha_hora(new java.sql.Timestamp(System.currentTimeMillis()));
            nuevaVersion.setQue_cambio("Se modificó: " + cambios);

            model.insertarNuevaVersion(nuevaVersion);
            JOptionPane.showMessageDialog(view.getFrame(), "Cambios guardados.\n" + "Registro: " + nuevaVersion.getQue_cambio());
            
            seleccionarEvento();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error al guardar: " + e.getMessage());
        }
    }

    private void realizarNuevaEntrega() {
        int fila = view.getTabEventos().getSelectedRow();
        if (fila < 0) return;
        String titulo = view.getTxtTitulo().getText().trim();
        if (titulo.isEmpty()) {
            JOptionPane.showMessageDialog(view.getFrame(), "El Título es obligatorio.");
            return;
        }

        ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        int idReportero = Integer.parseInt(repCombo.getId());
        String subtitulo = view.getTxtSubtitulo().getText().trim();
        String cuerpo = view.getAreaCuerpo().getText().trim();

        try {
            int nuevoIdReportaje = model.getUltimoId("Reportaje") + 1;
            model.insertarReportaje(nuevoIdReportaje, titulo, idReportero);

            VersionReportajeEntity nuevaVersion = new VersionReportajeEntity();
            nuevaVersion.setId(model.getUltimoId("VersionReportaje") + 1);
            nuevaVersion.setReportaje_id(nuevoIdReportaje);
            nuevaVersion.setSubtitulo(subtitulo);
            nuevaVersion.setCuerpo(cuerpo);
            nuevaVersion.setFecha_hora(new java.sql.Timestamp(System.currentTimeMillis()));
            nuevaVersion.setQue_cambio("Versión inicial");

            model.insertarNuevaVersion(nuevaVersion);
            JOptionPane.showMessageDialog(view.getFrame(), "¡Reportaje entregado!");
            recargarTabla(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error al entregar: " + ex.getMessage());
        }
    }
    
    private void procesarSolicitudRevision() {
        if (idEventoSeleccionado == -1) return;
        ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        int idAutor = Integer.parseInt(repCombo.getId());
        int idReportaje = reportajeActual.getReportaje_id();

        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), 
            "Al solicitar revisión, el reportaje quedará BLOQUEADO y se notificará a los compañeros.\n¿Deseas continuar?", 
            "Aviso", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.solicitarRevisionAutomatica(idReportaje, idEventoSeleccionado, idAutor);
            JOptionPane.showMessageDialog(view.getFrame(), "Revisión solicitada.");
            seleccionarEvento(); 
        }
    }

    private void finalizarReportaje() {
        if (reportajeActual == null) return;
        
        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), 
            "¿Seguro que quieres finalizar definitivamente este reportaje? Ya no se podrán hacer más modificaciones.", 
            "Finalizar Reportaje", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.finalizarReportaje(reportajeActual.getReportaje_id());
            JOptionPane.showMessageDialog(view.getFrame(), "¡Reportaje finalizado con éxito!");
            
            seleccionarEvento();
        }
    }

    private void limpiarFormulario() {
        view.getTxtTitulo().setText("");
        view.getTxtSubtitulo().setText("");
        view.getAreaCuerpo().setText("");
        reportajeActual = null;
        idEventoSeleccionado = -1;
        limpiarTablasMultimedia();
        limpiarTablaRevisiones();
        view.getBtnSolicitarRevision().setVisible(false);
        view.getBtnFinalizarReportaje().setEnabled(false);
        view.getLblEstadoFinalizacion().setText("Estado: -");
    }
}