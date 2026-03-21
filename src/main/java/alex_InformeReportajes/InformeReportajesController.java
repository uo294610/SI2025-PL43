package alex_InformeReportajes;

import java.awt.Component;
import java.util.List;
import javax.swing.*;

public class InformeReportajesController {
    private InformeReportajesModel model;
    private InformeReportajesView view;

    public InformeReportajesController(InformeReportajesModel m, InformeReportajesView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        view.getCbEmpresas().addActionListener(e -> generarInforme());
        view.getCbAgencias().addActionListener(e -> generarInforme());
        view.getTxtFechaInicio().addActionListener(e -> generarInforme());
        view.getTxtFechaFin().addActionListener(e -> generarInforme());
    }

    public void initView() {
        cargarCombos();
        view.getFrame().setLocationRelativeTo(null);
        view.getFrame().setVisible(true);
    }

    private void cargarCombos() {
        // Cargar Empresas
        List<Object[]> empresas = model.getListaEmpresas();
        DefaultComboBoxModel<Object> mEmp = new DefaultComboBoxModel<>();
        for (Object[] e : empresas) mEmp.addElement(e);
        view.getCbEmpresas().setModel(mEmp);
        view.getCbEmpresas().setRenderer(new ListaRenderer());

        // Cargar Agencias
        List<Object[]> agencias = model.getListaAgencias();
        DefaultComboBoxModel<Object> mAg = new DefaultComboBoxModel<>();
        for (Object[] a : agencias) mAg.addElement(a);
        
        java.awt.event.ActionListener[] listeners = view.getCbAgencias().getActionListeners();
        for (java.awt.event.ActionListener l : listeners) view.getCbAgencias().removeActionListener(l);
        
        view.getCbAgencias().setModel(mAg);
        view.getCbAgencias().setRenderer(new ListaRenderer());
        
        for (java.awt.event.ActionListener l : listeners) view.getCbAgencias().addActionListener(l);
        
        
        view.getTxtInforme().setText("\n  Pulse 'Intro' en las fechas para generar el informe.");
    }

    private void generarInforme() {
        Object[] empresa = (Object[]) view.getCbEmpresas().getSelectedItem();
        Object[] agencia = (Object[]) view.getCbAgencias().getSelectedItem();
        if (empresa == null || agencia == null) return;

        String fechaInicio = view.getTxtFechaInicio().getText().trim();
        String fechaFin = view.getTxtFechaFin().getText().trim();

        if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "El filtro por rango de fechas es obligatorio.", 
                "Faltan datos", JOptionPane.WARNING_MESSAGE);
            return; 
        }

        List<InformeReportajeDTO> datos = model.getReportajesAccesibles(
            (int) empresa[0], (int) agencia[0], fechaInicio, fechaFin
        );

        // Construir el informe directamente en texto 
        StringBuilder informe = new StringBuilder();
        String nombreAgencia = agencia[1].toString().toUpperCase();
        
        informe.append("\n  REPORTE DE ACCESOS - ").append(nombreAgencia).append("\n");
        informe.append("  --------------------------------------------------------------\n\n");

        double totalAcumulado = 0.0;

        if (datos.isEmpty()) {
            informe.append("    No hay reportajes con acceso para estos filtros.\n\n");
        } else {
            for (InformeReportajeDTO rep : datos) {
                informe.append(String.format("  * Título: %s\n", rep.getTituloReportaje()));
                informe.append(String.format("    Evento: %-25s | Fecha: %s | Precio: %.2f €\n\n", 
                                             rep.getNombreEvento(), rep.getFechaEvento(), rep.getPrecio()));
                totalAcumulado += rep.getPrecio();
            }
        }

        informe.append("  --------------------------------------------------------------\n");
        informe.append(String.format("  TOTAL ACUMULADO: %.2f €\n", totalAcumulado));

        view.getTxtInforme().setText(informe.toString());
        view.getTxtInforme().setCaretPosition(0); 
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