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
        view.getCbTematicas().addActionListener(e -> cargarTabla());
        view.getRdPendientes().addActionListener(e -> cargarTabla());
        view.getRdDecididos().addActionListener(e -> cargarTabla());

        view.getBtnAceptar().addActionListener(e -> procesarDecision("ACEPTADO"));
        view.getBtnRechazar().addActionListener(e -> procesarDecision("RECHAZADO"));
        view.getBtnEliminar().addActionListener(e -> procesarDecision(null));

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
        for (Object[] emp : empresas) {
            m.addElement(emp);
        }
        view.getCbEmpresas().setModel(m);
        
        view.getCbEmpresas().setRenderer(new ListaRenderer());
        view.getCbTematicas().setRenderer(new ListaRenderer());
        
        recargarComboTematicas();
    }

    // --- AQUÍ ESTÁ EL CAMBIO PRINCIPAL ---
    private void recargarComboTematicas() {
        Object[] empresa = (Object[]) view.getCbEmpresas().getSelectedItem();
        if (empresa == null) return;
        
        boolean filtroActivo = view.getChkFiltroEmpresa().isSelected();
        
        // 1. Activar o desactivar el desplegable según el Checkbox
        view.getCbTematicas().setEnabled(filtroActivo);

        List<Object[]> tematicas;
        if (filtroActivo) {
            tematicas = model.getTematicasEmpresa((int) empresa[0]);
        } else {
            tematicas = model.getTodasTematicas();
        }

        DefaultComboBoxModel<Object> m = new DefaultComboBoxModel<>();
        m.addElement(new Object[]{0, "--- Todas ---"});
        for (Object[] t : tematicas) {
            m.addElement(t);
        }
        
        // Quitamos los listeners temporalmente para no recargar la tabla varias veces
        java.awt.event.ActionListener[] listeners = view.getCbTematicas().getActionListeners();
        for (java.awt.event.ActionListener l : listeners) view.getCbTematicas().removeActionListener(l);
        
        view.getCbTematicas().setModel(m);
        
        // 2. Si el filtro está desactivado, forzamos la selección a "Todas"
        if (!filtroActivo) {
            view.getCbTematicas().setSelectedIndex(0); 
        }
        
        // Devolvemos los listeners
        for (java.awt.event.ActionListener l : listeners) view.getCbTematicas().addActionListener(l);
        
        cargarTabla();
    }
    // --------------------------------------

    private void cargarTabla() {
        Object[] emp = (Object[]) view.getCbEmpresas().getSelectedItem();
        Object[] tem = (Object[]) view.getCbTematicas().getSelectedItem();
        if (emp == null || tem == null) return;

        listaActual = model.getOfrecimientos(
            (int) emp[0], 
            view.getChkFiltroEmpresa().isSelected(), 
            (int) tem[0], 
            view.getRdPendientes().isSelected()
        );

        String[] columnasDTO = {"id", "nombreEvento", "nombreAgencia", "fechaEvento", "nombreTematica", "decision"};
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
            return;
        }

        OfrecimientosDTO seleccionado = listaActual.get(fila);
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