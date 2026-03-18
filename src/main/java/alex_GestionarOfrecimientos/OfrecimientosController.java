package alex_GestionarOfrecimientos;

import java.awt.Component;
import java.util.List;
import javax.swing.*;
import giis.demo.util.SwingUtil;

public class OfrecimientosController {
    private OfrecimientosModel model;
    private OfrecimientosView view;
    private List<OfrecimientosDTO> listaActual;

    public OfrecimientosController(OfrecimientosModel m, OfrecimientosView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        view.getCbEmpresas().addActionListener(e -> recargarComboTematicas());
        view.getChkFiltroEmpresa().addActionListener(e -> recargarComboTematicas());
        
        // Listener para los filtros
        view.getCbTematicas().addActionListener(e -> cargarTabla());
        view.getRdPendientes().addActionListener(e -> cargarTabla());
        view.getRdDecididos().addActionListener(e -> cargarTabla());
        view.getTxtPrecioMin().addActionListener(e -> cargarTabla());
        view.getTxtPrecioMax().addActionListener(e -> cargarTabla());

        // Listener para acciones
        view.getBtnAceptar().addActionListener(e -> procesarDecision("ACEPTADO"));
        view.getBtnRechazar().addActionListener(e -> procesarDecision("RECHAZADO"));
        view.getBtnEliminar().addActionListener(e -> procesarDecision(null));

        // Listener de la tabla
        view.getTablaOfrecimientos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                comprobarEstadoFila();
            }
        });
    }

    public void initView() {
        cargarEmpresas();
        view.getFrame().setLocationRelativeTo(null);
        view.getFrame().setVisible(true);
    }

    private void cargarEmpresas() {
        List<Object[]> empresas = model.getListaEmpresas();
        DefaultComboBoxModel<Object> m = new DefaultComboBoxModel<>();
        for (Object[] emp : empresas) m.addElement(emp);
        view.getCbEmpresas().setModel(m);
        
        view.getCbEmpresas().setRenderer(new ListaRenderer());
        view.getCbTematicas().setRenderer(new ListaRenderer());
        
        recargarComboTematicas();
    }

    private void recargarComboTematicas() {
        Object[] empresa = (Object[]) view.getCbEmpresas().getSelectedItem();
        if (empresa == null) return;
        
        boolean filtroActivo = view.getChkFiltroEmpresa().isSelected();
        view.getCbTematicas().setEnabled(filtroActivo);

        List<Object[]> tematicas;
        if (filtroActivo) {
            tematicas = model.getTematicasEmpresa((int) empresa[0]);
        } else {
            tematicas = model.getTodasTematicas();
        }

        DefaultComboBoxModel<Object> m = new DefaultComboBoxModel<>();
        m.addElement(new Object[]{0, "--- Todas ---"});
        for (Object[] t : tematicas) m.addElement(t);
        
        java.awt.event.ActionListener[] listeners = view.getCbTematicas().getActionListeners();
        for (java.awt.event.ActionListener l : listeners) view.getCbTematicas().removeActionListener(l);
        
        view.getCbTematicas().setModel(m);
        if (!filtroActivo) view.getCbTematicas().setSelectedIndex(0); 
        
        for (java.awt.event.ActionListener l : listeners) view.getCbTematicas().addActionListener(l);
        
        cargarTabla();
    }

    private void cargarTabla() {
        Object[] emp = (Object[]) view.getCbEmpresas().getSelectedItem();
        Object[] tem = (Object[]) view.getCbTematicas().getSelectedItem();
        if (emp == null || tem == null) return;

        Double minPrecio = null;
        Double maxPrecio = null;
        
        try {
            String txtMin = view.getTxtPrecioMin().getText().trim();
            if (!txtMin.isEmpty()) minPrecio = Double.parseDouble(txtMin);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Formato de Precio Mínimo incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String txtMax = view.getTxtPrecioMax().getText().trim();
            if (!txtMax.isEmpty()) maxPrecio = Double.parseDouble(txtMax);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view.getFrame(), "Formato de Precio Máximo incorrecto.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        listaActual = model.getOfrecimientos(
            (int) emp[0], view.getChkFiltroEmpresa().isSelected(), 
            (int) tem[0], view.getRdPendientes().isSelected(),
            minPrecio, maxPrecio
        );

        String[] columnasDTO = {"id", "nombreEvento", "nombreAgencia", "fechaEvento", "nombreTematica", "precio"};
        view.getTablaOfrecimientos().setModel(SwingUtil.getTableModelFromPojos(listaActual, columnasDTO));
        
        comprobarEstadoFila();
    }

    private void comprobarEstadoFila() {
        int fila = view.getTablaOfrecimientos().getSelectedRow();
        
        if (fila == -1 || listaActual == null || listaActual.isEmpty() || fila >= listaActual.size()) {
            view.getBtnAceptar().setEnabled(false);
            view.getBtnRechazar().setEnabled(false);
            view.getBtnEliminar().setEnabled(false);
            view.setMensajeDecision("Seleccione un ofrecimiento para ver su estado.", false);
            view.getDetalleEvento().setModel(new javax.swing.table.DefaultTableModel());
            return;
        }

        OfrecimientosDTO seleccionado = listaActual.get(fila);
        
        // Carga los detalles en la tabla inferior 
        String[] columnasDetalle = {"id", "nombreEvento", "nombreAgencia", "fechaEvento", "nombreTematica", "precio", "decision"};
        view.getDetalleEvento().setModel(SwingUtil.getRecordModelFromPojo(seleccionado, columnasDetalle));
        SwingUtil.autoAdjustColumns(view.getDetalleEvento());

        boolean estaBloqueado = seleccionado.isAcceso();

        view.getBtnAceptar().setEnabled(!estaBloqueado);
        view.getBtnRechazar().setEnabled(!estaBloqueado);
        view.getBtnEliminar().setEnabled(!estaBloqueado);

        if (estaBloqueado) {
            view.setMensajeDecision("BLOQUEADO: Acceso ya concedido por la agencia. No se puede modificar.", true);
        } else {
            view.setMensajeDecision("Editable: Puede modificar o eliminar la decisión libremente.", false);
        }
    }

    private void procesarDecision(String estado) {
        int fila = view.getTablaOfrecimientos().getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(view.getFrame(), "Por favor, selecciona un ofrecimiento de la tabla.");
            return;
        }

        int idOfrecimiento = (int) view.getTablaOfrecimientos().getValueAt(fila, 0);
        
        try {
            model.actualizarDecision(idOfrecimiento, estado);
            JOptionPane.showMessageDialog(view.getFrame(), "Decisión actualizada correctamente.");
            cargarTabla(); 
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    class ListaRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
            super.getListCellRendererComponent(l, v, i, s, f);
            if (v instanceof Object[]) {
                setText(((Object[]) v)[1].toString());
            }
            return this;
        }
    }
}