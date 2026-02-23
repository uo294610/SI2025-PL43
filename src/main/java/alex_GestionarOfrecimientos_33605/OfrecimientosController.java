package alex_GestionarOfrecimientos_33605;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import giis.demo.util.SwingUtil;

public class OfrecimientosController {
    private OfrecimientosModel model;
    private OfrecimientosView view;
    private String lastSelectedKey = "";

    public OfrecimientosController(OfrecimientosModel m, OfrecimientosView v) {
        this.model = m;
        this.view = v;
        this.initController();
    }

    public void initController() {
        // Cambio de empresa
        view.getCbEmpresas().addActionListener(e -> SwingUtil.exceptionWrapper(() -> getListaOfrecimientos()));
        
        // Clic en la tabla - Usamos MouseAdapter para detectar la selección
        view.getTablaOfrecimientos().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                SwingUtil.exceptionWrapper(() -> {
                    // Forzamos la actualización al hacer clic
                    restoreDetail(); 
                });
            }
        });

        view.getBtnAceptar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> decidir("ACEPTADO")));
        view.getBtnRechazar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> decidir("RECHAZADO")));
    }



    public void initView() {
        // Carga de empresas con Renderer para ver solo el nombre
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

        this.getListaOfrecimientos();
        view.getFrame().setVisible(true);
    }

    public void getListaOfrecimientos() {
        Object selected = view.getCbEmpresas().getSelectedItem();
        if (selected instanceof Object[]) {
            int idEmpresa = Integer.parseInt(((Object[]) selected)[0].toString());
            List<OfrecimientosDTO> datos = model.getOfrecimientosPendientes(idEmpresa);
            TableModel tmodel = SwingUtil.getTableModelFromPojos(datos, new String[]{"id", "evento", "agencia", "fecha"});
            view.getTablaOfrecimientos().setModel(tmodel);
            SwingUtil.autoAdjustColumns(view.getTablaOfrecimientos());
        }
        this.restoreDetail();
    }

    private void decidir(String d) {
        if (this.lastSelectedKey != null && !this.lastSelectedKey.isEmpty()) {
            int id = Integer.parseInt(this.lastSelectedKey);
            
            // 1. Antes de borrarlo de la lista, obtenemos el detalle para saber el nombre del evento
            OfrecimientoEntity ofr = model.getDetalleOfrecimiento(id);
            String nombreEvento = (ofr != null) ? ofr.getEvento() : String.valueOf(id);

            // 2. Realizamos la actualización en la base de datos
            model.updateDecision(id, d);
            
            // 3. Limpiamos selección y refrescamos la tabla
            this.lastSelectedKey = ""; 
            getListaOfrecimientos();
            
            // 4. Mostramos el mensaje personalizado con el nombre del evento
            view.setMensajeDecision("Evento '" + nombreEvento + "' " + d);
        }
    }

    public void restoreDetail() {
        // Buscamos qué ID se ha pinchado
        this.lastSelectedKey = SwingUtil.getSelectedKey(view.getTablaOfrecimientos());
        
        if (this.lastSelectedKey == null || this.lastSelectedKey.isEmpty()) {
            view.getBtnAceptar().setEnabled(false);
            view.getBtnRechazar().setEnabled(false);
        } else {
            // ACTIVAMOS botones y cargamos detalle
            int id = Integer.parseInt(this.lastSelectedKey);
            OfrecimientoEntity ofr = model.getDetalleOfrecimiento(id);
            view.getDetalleEvento().setModel(SwingUtil.getRecordModelFromPojo(ofr, new String[]{"id", "evento", "agencia", "fecha"}));
            
            view.getBtnAceptar().setEnabled(true);
            view.getBtnRechazar().setEnabled(true);
        }
    }
}