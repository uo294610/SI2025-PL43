package alex_ModificarDecision_33611;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import giis.demo.util.SwingUtil;

public class OfrecimientosController {
    private OfrecimientosModel model;
    private OfrecimientosView view;
    private String lastSelectedKey = "";

    public OfrecimientosController(OfrecimientosModel m, OfrecimientosView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        view.getCbEmpresas().addActionListener(e -> getListaOfrecimientos());
        view.getRdPendientes().addActionListener(e -> getListaOfrecimientos());
        view.getRdDecididos().addActionListener(e -> getListaOfrecimientos());

        view.getTablaOfrecimientos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                updateDetailSelection();
            }
        });

        view.getBtnAceptar().addActionListener(e -> decidir("ACEPTADO"));
        view.getBtnRechazar().addActionListener(e -> decidir("RECHAZADO"));
        view.getBtnEliminar().addActionListener(e -> decidir(null));
    }

    public void initView() {
        List<Object[]> empresas = model.getListaEmpresas();
        DefaultComboBoxModel<Object> comboModel = new DefaultComboBoxModel<>();
        for (Object[] emp : empresas) comboModel.addElement(emp);
        view.getCbEmpresas().setModel(comboModel);
        
        view.getCbEmpresas().setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Object[]) setText(((Object[]) value)[1].toString());
                return this;
            }
        });

        getListaOfrecimientos();
        view.getFrame().setVisible(true);
    }

    public void getListaOfrecimientos() {
        Object selected = view.getCbEmpresas().getSelectedItem();
        if (selected instanceof Object[]) {
            int idEmpresa = Integer.parseInt(((Object[]) selected)[0].toString());
            boolean verDecididos = view.getRdDecididos().isSelected();
            List<OfrecimientosDTO> datos = model.getOfrecimientosFiltrados(idEmpresa, verDecididos);
            
            String[] cols = verDecididos ? new String[]{"id", "evento", "agencia", "fecha", "decision"} 
                                         : new String[]{"id", "evento", "agencia", "fecha"};
            
            view.getTablaOfrecimientos().setModel(SwingUtil.getTableModelFromPojos(datos, cols));
        }
        clearDetail();
    }

    private void updateDetailSelection() {
        this.lastSelectedKey = SwingUtil.getSelectedKey(view.getTablaOfrecimientos());
        if (this.lastSelectedKey != null && !this.lastSelectedKey.isEmpty()) {
            OfrecimientoEntity ofr = model.getDetalleOfrecimiento(Integer.parseInt(this.lastSelectedKey));
            
           
            view.getDetalleEvento().setModel(SwingUtil.getRecordModelFromPojo(ofr, new String[]{"id", "evento", "agencia", "fecha", "decision"}));
            
            boolean tieneAccesoReal = (ofr.isAcceso() == true);
            
            view.getBtnAceptar().setEnabled(!tieneAccesoReal);
            view.getBtnRechazar().setEnabled(!tieneAccesoReal);
            view.getBtnEliminar().setEnabled(!tieneAccesoReal);
            
            if (tieneAccesoReal) {
                view.setMensajeDecision("BLOQUEADO: Acceso ya concedido por la agencia.");
            } else {
                view.setMensajeDecision("Editable: Puede modificar o eliminar la decisión.");
            }
        }
    }

    private void decidir(String d) {
        if (this.lastSelectedKey != null && !this.lastSelectedKey.isEmpty()) {
            model.updateDecision(Integer.parseInt(this.lastSelectedKey), d);
            getListaOfrecimientos();
            view.setMensajeDecision("Decisión actualizada correctamente.");
        }
    }

    private void clearDetail() {
        this.lastSelectedKey = "";
        view.getDetalleEvento().setModel(new DefaultTableModel());
        view.getBtnAceptar().setEnabled(false);
        view.getBtnRechazar().setEnabled(false);
        view.getBtnEliminar().setEnabled(false);
    }
}