package alex_GestionarOfrecimientos;

import java.awt.Component;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        view.getCbFiltroEmbargo().addActionListener(e -> cargarTabla());
        view.getRdPendientes().addActionListener(e -> cargarTabla());
        view.getRdDecididos().addActionListener(e -> cargarTabla());
        
        view.getTxtPrecioMin().addActionListener(e -> cargarTabla());
        view.getTxtPrecioMax().addActionListener(e -> cargarTabla());

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
        } catch (NumberFormatException e) { return; }

        try {
            String txtMax = view.getTxtPrecioMax().getText().trim();
            if (!txtMax.isEmpty()) maxPrecio = Double.parseDouble(txtMax);
        } catch (NumberFormatException e) { return; }

        String filtroEmbargo = view.getCbFiltroEmbargo().getSelectedItem().toString();

        listaActual = model.getOfrecimientos(
            (int) emp[0], view.getChkFiltroEmpresa().isSelected(), 
            (int) tem[0], view.getRdPendientes().isSelected(),
            minPrecio, maxPrecio, filtroEmbargo
        );

        String[] columnas = {"id", "evento", "agencia", "precio", "temática", "embargo", "fin embargo"};
        DefaultTableModel tm = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        DateTimeFormatter dbFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter outFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (OfrecimientosDTO dto : listaActual) {
            String estadoEmbargoStr = "No";
            String finEmbargoStr = "-";
            
            if (dto.getFechaFinEmbargo() != null) {
                if ("PARCIAL".equalsIgnoreCase(dto.getTipoAcceso())) estadoEmbargoStr = "Parcial";
                else estadoEmbargoStr = "Sí";
                
                try {
                    LocalDateTime ldt = LocalDateTime.parse(dto.getFechaFinEmbargo().substring(0, 19), dbFmt);
                    finEmbargoStr = ldt.format(outFmt);
                } catch (Exception e) { finEmbargoStr = dto.getFechaFinEmbargo(); }
            }

            Object[] fila = {
                dto.getId(),
                dto.getNombreEvento(),
                dto.getNombreAgencia(),
                String.format("%,.0f €", dto.getPrecio()),
                dto.getNombreTematica(),
                estadoEmbargoStr,
                finEmbargoStr
            };
            tm.addRow(fila);
        }

        view.getTablaOfrecimientos().setModel(tm);
        comprobarEstadoFila();
    }

    private void comprobarEstadoFila() {
        int fila = view.getTablaOfrecimientos().getSelectedRow();
        
        if (fila == -1 || listaActual == null || listaActual.isEmpty() || fila >= listaActual.size()) {
            view.getBtnAceptar().setEnabled(false);
            view.getBtnRechazar().setEnabled(false);
            view.getBtnEliminar().setEnabled(false);
            view.getTxtDetalleEvento().setText("\n  Seleccione un evento de la tabla.");
            return;
        }

        OfrecimientosDTO dto = listaActual.get(fila);
        
        boolean estaBloqueado = dto.isAcceso() && "COMPLETO".equalsIgnoreCase(dto.getTipoAcceso());
        view.getBtnAceptar().setEnabled(!estaBloqueado);
        view.getBtnRechazar().setEnabled(!estaBloqueado);
        view.getBtnEliminar().setEnabled(!estaBloqueado);

        // Construir el texto del JTextArea
        StringBuilder sb = new StringBuilder();
        String tituloRep = dto.getTituloReportaje() != null ? dto.getTituloReportaje() : "Reportaje sin asignar";
        
        sb.append(String.format(" Título: %s (%s)\n", tituloRep, dto.getNombreEvento()));
        sb.append(String.format(" Agencia: %-30s Precio: %,.0f €\n\n", dto.getNombreAgencia(), dto.getPrecio()));

        if (dto.getFechaFinEmbargo() != null) {
            String accesoTxt = "PARCIAL".equalsIgnoreCase(dto.getTipoAcceso()) ? "Acceso Parcial" : "Bloqueado";
            sb.append(" ESTADO DE EMBARGO: ").append(accesoTxt).append(".\n");
            
            try {
                DateTimeFormatter dbFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime ldt = LocalDateTime.parse(dto.getFechaFinEmbargo().substring(0, 19), dbFmt);
                sb.append(" Fin de restricción: ").append(ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'a las' HH:mm'h'"))).append(".\n\n");
            } catch (Exception e) {
                sb.append(" Fin de restricción: ").append(dto.getFechaFinEmbargo()).append(".\n\n");
            }

            if (!estaBloqueado) {
                sb.append(" * Tienes permiso para revisar parte del contenido y tomar una decisión.\n");
            } else {
                sb.append(" * BLOQUEADO: Acceso COMPLETO concedido. No se puede modificar.\n");
            }
        } else {
            sb.append(" ESTADO DE EMBARGO: Sin embargo.\n\n");
            if (estaBloqueado) {
                sb.append(" * BLOQUEADO: Acceso COMPLETO concedido. No se puede modificar.\n");
            } else {
                sb.append(" * Evento libre de restricciones. Puedes tomar una decisión.\n");
            }
        }

        view.getTxtDetalleEvento().setText(sb.toString());
        view.getTxtDetalleEvento().setCaretPosition(0);
    }

    private void procesarDecision(String estado) {
        int fila = view.getTablaOfrecimientos().getSelectedRow();
        if (fila == -1) return;

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
            if (v instanceof Object[]) setText(((Object[]) v)[1].toString());
            return this;
        }
    }
}