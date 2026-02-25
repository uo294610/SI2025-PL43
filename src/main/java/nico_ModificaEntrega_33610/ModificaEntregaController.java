package nico_ModificaEntrega_33610;

import java.awt.Color;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import giis.demo.util.SwingUtil;
import nico_EntregarReportEvento.EventoResumenDTO;
import nico_EntregarReportEvento.ReporteroDisplayDTO;
import nico_EntregarReportEvento.VersionReportajeEntity;

public class ModificaEntregaController {
    private ModificaEntregaModel model;
    private ModificaEntregaView view;
    private ReportajeEdicionDTO reportajeActual; // Guarda el reportaje seleccionado

    public ModificaEntregaController(ModificaEntregaModel m, ModificaEntregaView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        cargarReporteros();
        configurarListeners();
        cambiarModo(true); // Arrancamos en modo "Pendientes"
        view.getFrame().setVisible(true);
    }

    private void configurarListeners() {
        // Cuando cambias de reportero en el desplegable
        view.getCbReporteros().addActionListener(e -> recargarTabla());

        // Cuando haces clic en los botones de radio (Filtros)
        view.getRdbtnPendientes().addActionListener(e -> cambiarModo(true));
        view.getRdbtnEntregados().addActionListener(e -> cambiarModo(false));

        // Cuando haces clic en una fila de la tabla
        view.getTabEventos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) seleccionarEvento();
        });

        // Guardar Cambio
        view.getBtnGuardarCambio().addActionListener(e -> guardarModificacion());
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
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
            view.getTxtTitulo().setEditable(false); // El título no se puede modificar
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
        if (fila < 0 || view.getRdbtnPendientes().isSelected()) return;

        int idEvento = (int) view.getTabEventos().getValueAt(fila, 0);
        reportajeActual = model.getUltimaVersion(idEvento);

        if (reportajeActual != null) {
            // Rellenamos el formulario
            view.getTxtTitulo().setText(reportajeActual.getTitulo());
            view.getTxtSubtitulo().setText(reportajeActual.getSubtitulo());
            view.getAreaCuerpo().setText(reportajeActual.getCuerpo());

            // Comprobamos permisos: ¿El reportero logueado es el autor original?
            ReporteroDisplayDTO repCombo = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
            int idRepActual = Integer.parseInt(repCombo.getId());

            if (idRepActual == reportajeActual.getReportero_entrega_id()) {
                view.getLblPermisoModificar().setText("✓ Eres el autor. Puedes modificar.");
                view.getLblPermisoModificar().setForeground(Color.GREEN.darker());
                view.getBtnGuardarCambio().setVisible(true);
            } else {
                view.getLblPermisoModificar().setText("✗ Sólo el autor original puede modificar.");
                view.getLblPermisoModificar().setForeground(Color.RED);
                view.getBtnGuardarCambio().setVisible(false);
            }
        }
    }

    private void guardarModificacion() {
        if (reportajeActual == null) return;

        String nuevoSub = view.getTxtSubtitulo().getText().trim();
        String nuevoCuerpo = view.getAreaCuerpo().getText().trim();

        // 1. Detectar QUÉ ha cambiado exactamente
        String cambios = "";
        if (!nuevoSub.equals(reportajeActual.getSubtitulo())) {
            cambios += "subtítulo";
        }
        if (!nuevoCuerpo.equals(reportajeActual.getCuerpo())) {
            if (!cambios.isEmpty()) cambios += " y "; // Si ya cambió el subtítulo, añadimos "y"
            cambios += "cuerpo";
        }
        
        // Si le dan al botón sin cambiar nada, avisamos y no hacemos nada
        if (cambios.isEmpty()) {
            JOptionPane.showMessageDialog(view.getFrame(), "No has realizado ningún cambio en el subtítulo ni en el cuerpo.");
            return;
        }

        // 2. Preparar el objeto con la fecha/hora exacta y el registro de cambios
        nico_EntregarReportEvento.VersionReportajeEntity nuevaVersion = new nico_EntregarReportEvento.VersionReportajeEntity();
        nuevaVersion.setId(model.getUltimoId("VersionReportaje") + 1);
        nuevaVersion.setReportaje_id(reportajeActual.getReportaje_id());
        nuevaVersion.setSubtitulo(nuevoSub);
        nuevaVersion.setCuerpo(nuevoCuerpo);
        
        // Capturamos el instante exacto del cambio
        java.sql.Timestamp fechaHoraCambio = new java.sql.Timestamp(System.currentTimeMillis());
        nuevaVersion.setFecha_hora(fechaHoraCambio);
        
        String registroCambio = "Se modificó: " + cambios;
        nuevaVersion.setQue_cambio(registroCambio);

        // Formateamos la fecha para que se lea bien en la pantalla
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String fechaFormateada = sdf.format(fechaHoraCambio);

        // 3. Enviar a base de datos y mostrar mensaje al usuario
        try {
            model.insertarNuevaVersion(nuevaVersion);
            
            // Mensaje actualizado incluyendo la fecha y la hora
            JOptionPane.showMessageDialog(view.getFrame(), 
                "Cambios guardados. Se ha generado una nueva versión.\n" +
                "Registro: " + registroCambio + "\n" +
                "Fecha y hora: " + fechaFormateada);
                
            limpiarFormulario();
            view.getTabEventos().clearSelection();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error al guardar: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        view.getTxtTitulo().setText("");
        view.getTxtSubtitulo().setText("");
        view.getAreaCuerpo().setText("");
        reportajeActual = null;
    }
}