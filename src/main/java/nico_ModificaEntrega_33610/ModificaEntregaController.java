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
        // Corrección del Listener del JComboBox para evitar llamadas dobles o vacías
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
            return;
        }

        // --- LAS 3 LÍNEAS QUE CAMBIAN ---
        ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        int idRepActual = Integer.parseInt(repCombo.getId());
        reportajeActual = model.getUltimaVersion(idRepActual); 
        // --------------------------------

        if (reportajeActual != null) {
            view.getTxtTitulo().setText(reportajeActual.getTitulo());
            view.getTxtSubtitulo().setText(reportajeActual.getSubtitulo());
            view.getAreaCuerpo().setText(reportajeActual.getCuerpo());

            recargarTablasMultimedia();

            if (idRepActual == reportajeActual.getReportero_entrega_id()) {
                view.getLblPermisoModificar().setText("✓ Eres el autor. Puedes modificar.");
                view.getLblPermisoModificar().setForeground(Color.GREEN.darker());
                view.getBtnGuardarCambio().setVisible(true);
                activarBotonesMultimedia(true);
            } else {
                view.getLblPermisoModificar().setText("✗ Sólo el autor original puede modificar.");
                view.getLblPermisoModificar().setForeground(Color.RED);
                view.getBtnGuardarCambio().setVisible(false);
                activarBotonesMultimedia(false);
            }
        }
    }

    private void activarBotonesMultimedia(boolean activar) {
        view.getBtnAnadirImagen().setEnabled(activar);
        view.getBtnEliminarImagen().setEnabled(activar);
        view.getBtnFijarImgDefinitiva().setEnabled(activar);
        view.getBtnAnadirVideo().setEnabled(activar);
        view.getBtnEliminarVideo().setEnabled(activar);
        view.getBtnFijarVidDefinitivo().setEnabled(activar);
    }

    private void limpiarTablasMultimedia() {
        view.getTabImagenes().setModel(new javax.swing.table.DefaultTableModel());
        view.getTabVideos().setModel(new javax.swing.table.DefaultTableModel());
    }

    // --- LÓGICA MULTIMEDIA ---

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
        if (reportajeActual == null) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debes entregar primero el reportaje (textos) antes de añadir multimedia.");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar " + tipo);
        if (tipo.equals("imagen")) {
            chooser.setFileFilter(new FileNameExtensionFilter("Imágenes (JPG, PNG)", "jpg", "png", "jpeg"));
        } else {
            chooser.setFileFilter(new FileNameExtensionFilter("Vídeos (MP4, AVI)", "mp4", "avi"));
        }

        if (chooser.showOpenDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            String ruta = archivo.getAbsolutePath();
            String nombreNuevoArchivo = archivo.getName(); // Extraemos solo el nombre (ej: "foto.jpg")

            // --- NUEVO: CONTROL DE DUPLICADOS ---
            // 1. Elegimos en qué tabla vamos a buscar dependiendo del tipo
            JTable tablaDestino = tipo.equals("imagen") ? view.getTabImagenes() : view.getTabVideos();
            
            // 2. Recorremos todas las filas de esa tabla
            for (int i = 0; i < tablaDestino.getRowCount(); i++) {
                String rutaExistente = (String) tablaDestino.getValueAt(i, 0); // Columna 0 es la ruta
                File archivoExistente = new File(rutaExistente);
                
                // 3. Comprobamos si la ruta es idéntica o si el nombre del archivo es el mismo
                if (rutaExistente.equals(ruta) || archivoExistente.getName().equals(nombreNuevoArchivo)) {
                    JOptionPane.showMessageDialog(view.getFrame(), 
                        "Error: Ya existe un(a) " + tipo + " con el nombre '" + nombreNuevoArchivo + "' en la lista.",
                        "Archivo Duplicado", JOptionPane.ERROR_MESSAGE);
                    return; // CORTAMOS LA EJECUCIÓN: No se guarda nada en base de datos
                }
            }
            // ------------------------------------

            // Si pasa el control, guardamos normalmente
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
        if (fila < 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Selecciona un archivo de la tabla primero.");
            return;
        }
        String ruta = (String) tabla.getValueAt(fila, 0);
        model.fijarMultimediaDefinitiva(ruta);
        recargarTablasMultimedia();
    }

    // --- LÓGICA DE TEXTOS ---

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
        java.sql.Timestamp fechaHoraCambio = new java.sql.Timestamp(System.currentTimeMillis());
        nuevaVersion.setFecha_hora(fechaHoraCambio);
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
        if (fila < 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debe seleccionar un evento.");
            return;
        }
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
            JOptionPane.showMessageDialog(view.getFrame(), "¡Reportaje entregado! Ahora ve a 'Entregados' para añadirle fotos o vídeos.");
            recargarTabla(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error al entregar: " + ex.getMessage());
        }
    }
    
    private void limpiarFormulario() {
        view.getTxtTitulo().setText("");
        view.getTxtSubtitulo().setText("");
        view.getAreaCuerpo().setText("");
        reportajeActual = null;
        limpiarTablasMultimedia();
    }
}