package alex_DietasReportero;

import java.awt.Component;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DietasController {
    private DietasModel model;
    private DietasView view;
    private List<DietaDTO> listaDietasActual;
    private Object[] infoReporteroActual;

    public DietasController(DietasModel m, DietasView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        view.getCbReporteros().addActionListener(e -> cargarDatosReportero());
        
        view.getTablaEventos().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDesglose();
            }
        });
    }

    public void initView() {
        cargarCombos();
        view.getFrame().setLocationRelativeTo(null);
        view.getFrame().setVisible(true);
    }

    private void cargarCombos() {
        List<Object[]> reporteros = model.getListaReporteros();
        DefaultComboBoxModel<Object> comboModel = new DefaultComboBoxModel<>();
        for (Object[] r : reporteros) comboModel.addElement(r);
        
        view.getCbReporteros().setModel(comboModel);
        view.getCbReporteros().setRenderer(new ListaRenderer());
        
        cargarDatosReportero();
    }

    private void cargarDatosReportero() {
        Object[] reporteroSeleccionado = (Object[]) view.getCbReporteros().getSelectedItem();
        if (reporteroSeleccionado == null) return;

        int idReportero = (int) reporteroSeleccionado[0];
        infoReporteroActual = model.getInfoReportero(idReportero);

        if (infoReporteroActual != null) {
            String agencia = infoReporteroActual[4] != null ? infoReporteroActual[4].toString() : "Freelance (Sin Agencia)";
            view.getLblAgencia().setText(agencia);
            view.getLblProvincia().setText(infoReporteroActual[2].toString());
            view.getLblPais().setText(infoReporteroActual[3].toString());
            
            cargarTablaEventos(idReportero, (int) infoReporteroActual[5]);
        }
    }

    private void cargarTablaEventos(int idReportero, int idProvinciaBase) {
        listaDietasActual = model.getDietasEventos(idReportero, idProvinciaBase);

        String[] columnas = {"Fecha", "Evento", "Ubicación", "Días", "Manut.", "Aloj.", "Total"};
        DefaultTableModel tm = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        for (DietaDTO dto : listaDietasActual) {
            Object[] fila = {
                dto.getFecha(),
                dto.getEventoNombre(),
                dto.getUbicacion(),
                dto.getDias(),
                String.format("%.2f €", dto.getTotalManutencion()),
                String.format("%.2f €", dto.getTotalAlojamiento()),
                String.format("%.2f €", dto.getTotalApercibir())
            };
            tm.addRow(fila);
        }

        view.getTablaEventos().setModel(tm);
        view.getTxtDesglose().setText("\n  Seleccione un evento de la tabla para ver el desglose de tarifas aplicadas.");
    }

    private void mostrarDesglose() {
        int fila = view.getTablaEventos().getSelectedRow();
        if (fila == -1 || listaDietasActual == null) return;

        DietaDTO dto = listaDietasActual.get(fila);
        String provinciaBase = infoReporteroActual[2].toString();
        String paisBase = infoReporteroActual[3].toString();
        
        // Extraemos nombre de país y provincia del evento 
        String ubicacion = dto.getUbicacion();
        String provEvento = ubicacion.substring(0, ubicacion.indexOf("(")).trim();
        String paisEvento = ubicacion.substring(ubicacion.indexOf("(") + 1, ubicacion.indexOf(")"));

        StringBuilder sb = new StringBuilder();
        sb.append("\n  CONFIGURACIÓN DE TARIFAS APLICADAS:\n");
        sb.append("  ----------------------------------------------------------------------------------\n\n");
        
        // Manutención
        sb.append(String.format("  > MANUTENCIÓN (País: %s): %.2f €/día\n", paisEvento, dto.getTarifaManutencion()));
        sb.append(String.format("    Cálculo: %d día(s) x %.2f € = %.2f €\n\n", dto.getDias(), dto.getTarifaManutencion(), dto.getTotalManutencion()));

        // Alojamiento
        if (dto.isAplicaAlojamiento()) {
            sb.append(String.format("  > ALOJAMIENTO (Provincia: %s): %.2f €/día\n", provEvento, dto.getTarifaAlojamiento()));
            sb.append(String.format("    Cálculo: %d día(s) x %.2f € = %.2f €\n\n", dto.getDias(), dto.getTarifaAlojamiento(), dto.getTotalAlojamiento()));
        } else {
            sb.append(String.format("  > ALOJAMIENTO (Provincia: %s): 0.00 €\n\n", provEvento));
        }

        sb.append("  ----------------------------------------------------------------------------------\n");
        sb.append(String.format("  TOTAL A PERCIBIR POR ESTE EVENTO: %.2f €\n", dto.getTotalApercibir()));

        view.getTxtDesglose().setText(sb.toString());
        view.getTxtDesglose().setCaretPosition(0);
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