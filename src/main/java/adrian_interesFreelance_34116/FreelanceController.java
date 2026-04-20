package adrian_interesFreelance_34116;

import java.util.List;
import javax.swing.*;
import giis.demo.util.SwingUtil;

/**
 * Controlador para la gestión de interés de reporteros freelance.
 * Permite seleccionar un reportero, ver eventos que coincidan con sus temáticas
 * y guardar su estado de interés (Interesado, No interesado, En duda).
 */
public class FreelanceController {
    private FreelanceModel model;
    private FreelanceView view;
    private int idFreelanceActual; 

    public FreelanceController(FreelanceModel m, FreelanceView v) {
        this.model = m;
        this.view = v;
    }

    /**
     * 
     */
    public void initController() {
        // 1. Manejador para el cambio de reportero en el desplegable
        view.getCbFreelances().addActionListener(e -> {
            Object[] seleccionado = (Object[]) view.getCbFreelances().getSelectedItem();
            if (seleccionado != null) {
                this.idFreelanceActual = (int) seleccionado[0];
                actualizarPerfilYTabla();
            }
        });

        // 2. Manejador para el botón Guardar
        view.getBtnGuardar().addActionListener(e -> ejecutarGuardar());

        // 3. Manejador para actualizar los RadioButtons al seleccionar una fila de la tabla
        view.getTabEventos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int f = view.getTabEventos().getSelectedRow();
                if (f >= 0) {
                    actualizarSeleccionRadios(f);
                }
            }
        });
    }

    /**
     * Configuración inicial de la vista
     */
    public void initView() {
        // Cargar la lista de freelances en el ComboBox
        List<Object[]> freelances = model.getListaFreelances();
        DefaultComboBoxModel<Object> cbModel = new DefaultComboBoxModel<>();
        for (Object[] f : freelances) {
            cbModel.addElement(f);
        }
        view.getCbFreelances().setModel(cbModel);
        
        // Renderizador personalizado para mostrar el nombre del reportero en el combo
        view.getCbFreelances().setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Object[]) {
                    setText(((Object[]) value)[1].toString());
                }
                return this;
            }
        });

        // Seleccionamos el primer reportero por defecto para disparar la carga inicial
        if (cbModel.getSize() > 0) {
            view.getCbFreelances().setSelectedIndex(0);
        }
        
        view.getFrame().setLocationRelativeTo(null);
        view.getFrame().setVisible(true);
    }

    /**
     * Refresca la información del perfil (temáticas) y la tabla de eventos
     */
    private void actualizarPerfilYTabla() {
        // Obtener info del reportero (Nombre y Temáticas)
        List<Object[]> info = model.getInfoFreelance(idFreelanceActual);
        if (!info.isEmpty()) {
            String nombre = info.get(0)[0].toString();
            String especialidades = info.get(0)[1].toString();
            view.getLblCabecera().setText("Perfil: " + nombre + " | Especialidades: [" + especialidades + "]");
        } else {
            view.getLblCabecera().setText("El reportero seleccionado no tiene temáticas asignadas.");
        }
        
        cargarTabla();
    }

    /**
     * Carga los eventos que coinciden con las temáticas del freelance actual
     */
    private void cargarTabla() {
        List<EventoFreelanceDTO> eventos = model.getEventosDisponibles(idFreelanceActual);
        view.getTabEventos().setModel(SwingUtil.getTableModelFromPojos(eventos, 
                new String[]{"id", "nombreEvento", "fecha", "tematica", "miInteres"}));
        SwingUtil.autoAdjustColumns(view.getTabEventos());
    }

    /**
     * Sincroniza los RadioButtons con el estado de interés de la fila seleccionada
     */
    private void actualizarSeleccionRadios(int fila) {
        String estadoActual = view.getTabEventos().getValueAt(fila, 4).toString();
        switch (estadoActual) {
            case "Interesado": view.getRbInteresado().setSelected(true); break;
            case "No interesado": view.getRbNoInteresado().setSelected(true); break;
            case "En duda": view.getRbEnDuda().setSelected(true); break;
            default: view.getBgInteres().clearSelection(); break;
        }
    }

    /**
     * Recoge la selección del usuario y la persiste en la base de datos
     */
    private void ejecutarGuardar() {
        int f = view.getTabEventos().getSelectedRow();
        if (f < 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Por favor, selecciona un evento de la lista.");
            return;
        }

        String idEv = view.getTabEventos().getValueAt(f, 0).toString();
        String estadoElegido = "";
        
        if (view.getRbInteresado().isSelected()) estadoElegido = "Interesado";
        else if (view.getRbNoInteresado().isSelected()) estadoElegido = "No interesado";
        else if (view.getRbEnDuda().isSelected()) estadoElegido = "En duda";
        else {
            JOptionPane.showMessageDialog(view.getFrame(), "Debes marcar una de las 3 opciones de interés.");
            return;
        }

        // Persistencia a través del modelo
        model.guardarInteres(idFreelanceActual, idEv, estadoElegido);
        
        JOptionPane.showMessageDialog(view.getFrame(), "Interés actualizado correctamente.");
        cargarTabla(); // Refrescamos para ver el cambio en la columna "miInteres"
    }
}