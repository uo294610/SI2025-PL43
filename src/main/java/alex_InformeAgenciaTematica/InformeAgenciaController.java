package alex_InformeAgenciaTematica;

import java.awt.Component;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;

public class InformeAgenciaController {
    private InformeAgenciaModel model;
    private InformeAgenciaView view;

    public InformeAgenciaController(InformeAgenciaModel m, InformeAgenciaView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        view.getCbAgencias().addActionListener(e -> generarInforme());
        view.getCbTematicas().addActionListener(e -> generarInforme());
    }

    public void initView() {
        cargarCombos();
        view.getFrame().setLocationRelativeTo(null);
        view.getFrame().setVisible(true);
    }

    private void cargarCombos() {
        List<Object[]> agencias = model.getAgencias();
        DefaultComboBoxModel<Object> mAg = new DefaultComboBoxModel<>();
        for (Object[] a : agencias) mAg.addElement(a);
        view.getCbAgencias().setModel(mAg);
        view.getCbAgencias().setRenderer(new ListaRenderer());

        java.awt.event.ActionListener[] listeners = view.getCbTematicas().getActionListeners();
        for (java.awt.event.ActionListener l : listeners) view.getCbTematicas().removeActionListener(l);

        List<Object[]> tematicas = model.getTematicas();
        DefaultComboBoxModel<Object> mTem = new DefaultComboBoxModel<>();
        for (Object[] t : tematicas) mTem.addElement(t);
        view.getCbTematicas().setModel(mTem);
        view.getCbTematicas().setRenderer(new ListaRenderer());

        for (java.awt.event.ActionListener l : listeners) view.getCbTematicas().addActionListener(l);

        view.getTxtInforme().setText("\n  Seleccione una agencia y una temática para generar el informe económico.");
    }

    private void generarInforme() {
        Object[] ag = (Object[]) view.getCbAgencias().getSelectedItem();
        Object[] tem = (Object[]) view.getCbTematicas().getSelectedItem();
        if (ag == null || tem == null) return;

        int idAgencia = (int) ag[0];
        String nombreTematica = tem[1].toString().toUpperCase();

        List<InformeAgenciaDTO.EventoReporte> eventos = model.getInformeData(idAgencia, (int) tem[0]);
        
        double ingresosExtraIndividuales = 0.0;
        // Colección para guardar empresas únicas y evitar sumar sus cuotas dos veces
        Map<String, Double> cuotasUnicas = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        sb.append("\n  ------------------------------------------------------------------------------------\n");
        sb.append("   INFORME DE INGRESOS Y ACCESOS \n");
        sb.append("   Temática: ").append(nombreTematica).append("\n");
        sb.append("  ------------------------------------------------------------------------------------\n\n");

        if (eventos.isEmpty()) {
            sb.append("   No hay eventos registrados para esta agencia con esta temática.\n\n");
        }

        for (InformeAgenciaDTO.EventoReporte ev : eventos) {
            sb.append(String.format("  ■ EVENTO: %s (ID: %d) - Valor: %,.0f €\n", ev.nombre, ev.id, ev.precio));
            sb.append("    -----------------------------------------------------------------------\n");
            
            sb.append("    > Empresas con Tarifa Plana (Acceso concedido por suscripción):\n");
            boolean hayTarifaPlana = false;
            for (InformeAgenciaDTO.AccesoEmpresa acc : ev.accesos) {
                if (acc.tieneTarifaPlana) {
                    cuotasUnicas.put(acc.nombreEmpresa, acc.cuotaMensual);
                    sb.append(String.format("      - %s (Acceso %s) [Cuota: %,.0f €/mes]\n", acc.nombreEmpresa, acc.tipoAcceso, acc.cuotaMensual));
                    hayTarifaPlana = true;
                }
            }
            if (!hayTarifaPlana) sb.append("      - Ninguna.\n");
            sb.append("\n");

            sb.append("    > Empresas sin Tarifa Plana (Compra individual del reportaje):\n");
            boolean haySinTarifa = false;
            for (InformeAgenciaDTO.AccesoEmpresa acc : ev.accesos) {
                if (!acc.tieneTarifaPlana) {
                    sb.append(String.format("      - %s (Acceso %s / %s)\n", acc.nombreEmpresa, acc.tipoAcceso, acc.estadoPago));
                    ingresosExtraIndividuales += ev.precio;
                    haySinTarifa = true;
                }
            }
            if (!haySinTarifa) sb.append("      - Ninguna.\n");
            sb.append("\n\n");
        }

        // Sumamos las cuotas únicas de las empresas que han participado
        double sumaCuotasBase = 0.0;
        for (Double cuota : cuotasUnicas.values()) {
            sumaCuotasBase += cuota;
        }

        sb.append("  ====================================================================================\n");
        sb.append("   RESUMEN ECONÓMICO TOTAL (Temática: ").append(tem[1].toString()).append(")\n");
        sb.append("  ====================================================================================\n");
        
        sb.append(String.format("   Suma total aportada por empresas CON Tarifa Plana:            %,.0f €/mes\n", sumaCuotasBase));
        sb.append("   (Cubierto por las cuotas mensuales conjuntas de la agencia)\n\n");
        
        sb.append(String.format("   Suma total aportada por empresas SIN Tarifa Plana:            %,.0f €\n", ingresosExtraIndividuales));
        sb.append("   (Suma directa de pagos por reportaje individual)\n");
        sb.append("  ------------------------------------------------------------------------------------\n");
        sb.append(String.format("   TOTAL INGRESOS EXTRA GENERADOS:                               %,.0f €\n", ingresosExtraIndividuales));
        sb.append("  ====================================================================================\n");

        view.getTxtInforme().setText(sb.toString());
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