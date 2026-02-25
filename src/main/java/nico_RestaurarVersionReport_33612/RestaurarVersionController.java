package nico_RestaurarVersionReport_33612;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import giis.demo.util.SwingUtil;
import nico_EntregarReportEvento.ReporteroDisplayDTO;

public class RestaurarVersionController {
    private RestaurarVersionModel model;
    private RestaurarVersionView view;
    
    private int idReportaje;
    private int idAutorReportaje = -1; 

    // Constructor: Ya no le pasamos el idReporteroLogueado porque lo cogemos del ComboBox
    public RestaurarVersionController(RestaurarVersionModel m, RestaurarVersionView v, int idReportaje) {
        this.model = m;
        this.view = v;
        this.idReportaje = idReportaje;
    }

    public void initController() {
        cargarReporteros();
        cargarDatosCabecera();
        cargarTablaVersiones();
        configurarListeners();
        
        evaluarPermisos(); // Evaluamos la primera vez con el reportero por defecto
        view.getFrame().setVisible(true);
    }

    private void configurarListeners() {
        // Al cambiar de reportero simulado, reevaluamos los permisos
        view.getCbReporteros().addActionListener(e -> evaluarPermisos());

        // Al hacer clic en la tabla
        view.getTabVersiones().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) mostrarVersionSeleccionada();
        });

        // Botones
        view.getBtnGuardar().addActionListener(e -> guardarRestauracion());
        view.getBtnRestaurar().addActionListener(e -> guardarRestauracion());
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
    }

    private void cargarReporteros() {
        List<ReporteroDisplayDTO> lista = model.getListaReporteros();
        view.getCbReporteros().removeAllItems();
        for (ReporteroDisplayDTO rep : lista) {
            view.getCbReporteros().addItem(rep);
        }
    }

    private void cargarDatosCabecera() {
        List<Object[]> datos = model.getDatosCabeceraReportaje(idReportaje);
        if (datos != null && !datos.isEmpty()) {
            Object[] fila = datos.get(0);
            view.getTxtEvento().setText(fila[0].toString()); 
            view.getTxtTituloGeneral().setText(fila[1].toString()); 
            idAutorReportaje = Integer.parseInt(fila[2].toString()); 
        } else {
            JOptionPane.showMessageDialog(view.getFrame(), "AVISO: No existe el reportaje ID " + idReportaje + " en la base de datos de pruebas.");
        }
    }

    // --- NUEVO MÉTODO: Activa o desactiva botones según quién esté seleccionado ---
    private void evaluarPermisos() {
        ReporteroDisplayDTO repSel = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        if (repSel == null || idAutorReportaje == -1) return;
        
        int idLogueado = Integer.parseInt(repSel.getId());

        if (idLogueado == idAutorReportaje) {
            view.getBtnGuardar().setEnabled(true);
            view.getBtnRestaurar().setEnabled(true);
            view.getFrame().setTitle("Restaurar Versión (#33612) - MODO AUTOR (✓ Permitido)");
        } else {
            view.getBtnGuardar().setEnabled(false);
            view.getBtnRestaurar().setEnabled(false);
            view.getFrame().setTitle("Restaurar Versión (#33612) - MODO LECTURA (✗ Bloqueado)");
        }
    }

    private void cargarTablaVersiones() {
        List<VersionDisplayDTO> versiones = model.getVersionesReportaje(idReportaje);
        view.getTabVersiones().setModel(SwingUtil.getTableModelFromPojos(versiones, new String[] {"fecha_hora", "que_cambio"}));
        SwingUtil.autoAdjustColumns(view.getTabVersiones());
        
        if (!versiones.isEmpty()) {
            VersionDisplayDTO actual = versiones.get(0);
            view.getTxtTituloActual().setText(view.getTxtTituloGeneral().getText());
            view.getTxtSubtituloActual().setText(actual.getSubtitulo());
            view.getAreaCuerpoActual().setText(actual.getCuerpo());
        }
    }

    private void mostrarVersionSeleccionada() {
        int fila = view.getTabVersiones().getSelectedRow();
        if (fila < 0) return;
        List<VersionDisplayDTO> versiones = model.getVersionesReportaje(idReportaje);
        VersionDisplayDTO seleccionada = versiones.get(fila);
        view.getTxtTituloSeleccionada().setText(view.getTxtTituloGeneral().getText());
        view.getTxtSubtituloSeleccionada().setText(seleccionada.getSubtitulo());
        view.getAreaCuerpoSeleccionada().setText(seleccionada.getCuerpo());
    }

    private void guardarRestauracion() {
        int fila = view.getTabVersiones().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Selecciona una versión antigua de la tabla.");
            return;
        }
        if (fila == 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Esa ya es la versión actual. Selecciona otra.");
            return;
        }

        String subtituloRestaurado = view.getTxtSubtituloSeleccionada().getText();
        String cuerpoRestaurado = view.getAreaCuerpoSeleccionada().getText();
        String fechaAntigua = view.getTabVersiones().getValueAt(fila, 0).toString();

        nico_EntregarReportEvento.VersionReportajeEntity nuevaVersion = new nico_EntregarReportEvento.VersionReportajeEntity();
        nuevaVersion.setId(model.getUltimoId("VersionReportaje") + 1);
        nuevaVersion.setReportaje_id(idReportaje);
        nuevaVersion.setSubtitulo(subtituloRestaurado);
        nuevaVersion.setCuerpo(cuerpoRestaurado);
        
        java.sql.Timestamp fechaHoraCambio = new java.sql.Timestamp(System.currentTimeMillis());
        nuevaVersion.setFecha_hora(fechaHoraCambio);
        nuevaVersion.setQue_cambio("Restauración a la versión del: " + fechaAntigua);

        try {
            model.insertarVersionRestaurada(nuevaVersion);
            JOptionPane.showMessageDialog(view.getFrame(), "¡Versión restaurada con éxito!");
            cargarTablaVersiones();
            view.getTxtSubtituloSeleccionada().setText("");
            view.getAreaCuerpoSeleccionada().setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error al restaurar: " + ex.getMessage());
        }
    }
}