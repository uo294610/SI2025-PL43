package nico_ModificaEntrega_33610;

import java.awt.Color;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import giis.demo.util.SwingUtil;
import nico_EntregarReportEvento.EventoResumenDTO;
import nico_EntregarReportEvento.ReporteroDisplayDTO;
import nico_EntregarReportEvento.VersionReportajeEntity;

public class ModificaEntregaController {
    private ModificaEntregaModel model;
    private ModificaEntregaView view;
    private ReportajeEdicionDTO reportajeActual;

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
            view.getPanelRevision().setVisible(false);
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
            view.getPanelRevision().setVisible(false);
            return;
        }

        ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        int idRepActual = Integer.parseInt(repCombo.getId());
        reportajeActual = model.getUltimaVersion(idRepActual); 

        if (reportajeActual != null) {
            view.getTxtTitulo().setText(reportajeActual.getTitulo());
            view.getTxtSubtitulo().setText(reportajeActual.getSubtitulo());
            view.getAreaCuerpo().setText(reportajeActual.getCuerpo());

            recargarTablasMultimedia();

            if (reportajeActual.isRevision_solicitada()) {
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

    private void congelarEdicion(boolean congelar) {
        view.getTxtSubtitulo().setEditable(!congelar);
        view.getAreaCuerpo().setEditable(!congelar);
        view.getBtnGuardarCambio().setVisible(!congelar);
        
        view.getBtnAnadirImagen().setEnabled(!congelar);
        view.getBtnEliminarImagen().setEnabled(!congelar);
        view.getBtnFijarImgDefinitiva().setEnabled(!congelar);
        view.getBtnAnadirVideo().setEnabled(!congelar);
        view.getBtnEliminarVideo().setEnabled(!congelar);
        view.getBtnFijarVidDefinitivo().setEnabled(!congelar);
        
        view.getPanelRevision().setVisible(!congelar);
    }

    private void limpiarTablasMultimedia() {
        view.getTabImagenes().setModel(new javax.swing.table.DefaultTableModel());
        view.getTabVideos().setModel(new javax.swing.table.DefaultTableModel());
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
        String nuevoSub = view.getTxtSubtitulo().getText().trim();
        String nuevoCuerpo = view.getAreaCuerpo().getText().trim();
        String cambios = "";
        if (!nuevoSub.equals(reportajeActual.getSubtitulo())) cambios += "subtítulo";
        if (!nuevoCuerpo.equals(reportajeActual.getCuerpo())) {
            if (!cambios.isEmpty()) cambios += " y "; 
            cambios += "cuerpo";
        }
        
        if (cambios.isEmpty()) {
            JOptionPane.showMessageDialog(view.getFrame(), "No has modificado el texto.");
            return;
        }

        VersionReportajeEntity nuevaVersion = new VersionReportajeEntity();
        nuevaVersion.setId(model.getUltimoId("VersionReportaje") + 1);
        nuevaVersion.setReportaje_id(reportajeActual.getReportaje_id());
        nuevaVersion.setSubtitulo(nuevoSub);
        nuevaVersion.setCuerpo(nuevoCuerpo);
        nuevaVersion.setFecha_hora(new java.sql.Timestamp(System.currentTimeMillis()));
        nuevaVersion.setQue_cambio("Se modificó: " + cambios);

        try {
            model.insertarNuevaVersion(nuevaVersion);
            JOptionPane.showMessageDialog(view.getFrame(), "Cambios guardados.\n" + "Registro: " + nuevaVersion.getQue_cambio());
            limpiarFormulario();
            view.getTabEventos().clearSelection();
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
    
    // --- LÓGICA DE REVISIÓN AUTOMÁTICA ---
    private void procesarSolicitudRevision() {
        int fila = view.getTabEventos().getSelectedRow();
        if (fila < 0) return;
        
        int idEvento = (int) view.getTabEventos().getValueAt(fila, 0);
        ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        int idAutor = Integer.parseInt(repCombo.getId());
        int idReportaje = reportajeActual.getReportaje_id();

        int confirm = JOptionPane.showConfirmDialog(view.getFrame(), 
            "Al solicitar revisión, el reportaje quedará BLOQUEADO y se notificará a los compañeros del evento.\n¿Deseas continuar?", 
            "Aviso Importante", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            model.solicitarRevisionAutomatica(idReportaje, idEvento, idAutor);
            JOptionPane.showMessageDialog(view.getFrame(), "Revisión solicitada a los compañeros con éxito.");
            
            // Recargamos el evento para que aplique la congelación de botones
            seleccionarEvento(); 
        }
    }

    private void limpiarFormulario() {
        view.getTxtTitulo().setText("");
        view.getTxtSubtitulo().setText("");
        view.getAreaCuerpo().setText("");
        reportajeActual = null;
        limpiarTablasMultimedia();
        view.getPanelRevision().setVisible(false);
    }
}