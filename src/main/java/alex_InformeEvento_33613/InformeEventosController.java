package alex_InformeEvento_33613;

import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import giis.demo.util.SwingUtil;

public class InformeEventosController {
	private InformeEventosModel model;
    private InformeEventosView view;
    private String lastSelectedEventId = "";
    private InformeDTO informeActual = null;
    private String nombreEventoActual = "";

    public InformeEventosController(InformeEventosModel m, InformeEventosView v) {
        this.model = m;
        this.view = v;
    }

    public void initController() {
        view.getCbAgencias().addActionListener(e -> cargarEventos());
        
        view.getTablaEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                generarInformePantalla();
            }
        });

        view.getBtnExportarCSV().addActionListener(e -> exportarACSV());
    }

    public void initView() {
        List<Object[]> agencias = model.getListaAgencias();
        DefaultComboBoxModel<Object> comboModel = new DefaultComboBoxModel<>();
        for (Object[] ag : agencias) comboModel.addElement(ag);
        view.getCbAgencias().setModel(comboModel);
        
        view.getCbAgencias().setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Object[]) setText(((Object[]) value)[1].toString());
                return this;
            }
        });

        cargarEventos();
        view.getFrame().setVisible(true);
    }

    private void cargarEventos() {
        Object selected = view.getCbAgencias().getSelectedItem();
        if (selected instanceof Object[]) {
            int idAgencia = Integer.parseInt(((Object[]) selected)[0].toString());
            List<EventoDTO> eventos = model.getEventosPorAgencia(idAgencia);
            view.getTablaEventos().setModel(SwingUtil.getTableModelFromPojos(eventos, new String[]{"id", "nombre", "fecha"}));
        }
        view.getTxtInforme().setText("Seleccione un evento para ver su informe.");
        view.getBtnExportarCSV().setEnabled(false);
    }

    private void generarInformePantalla() {
        int fila = view.getTablaEventos().getSelectedRow();
        if (fila == -1) return;

        this.lastSelectedEventId = view.getTablaEventos().getValueAt(fila, 0).toString();
        this.nombreEventoActual = view.getTablaEventos().getValueAt(fila, 1).toString();
        
        informeActual = model.generarInforme(Integer.parseInt(this.lastSelectedEventId));

        // Formatear texto para la pantalla
        StringBuilder sb = new StringBuilder();
        sb.append("=== INFORME DEL EVENTO ===\n");
        sb.append("Evento: ").append(nombreEventoActual).append("\n\n");
        
        sb.append("1. REPORTEROS ASIGNADOS:\n");
        if (informeActual.getReporterosAsignados().isEmpty()) {
            sb.append("     \n");
        } else {
            for (String r : informeActual.getReporterosAsignados()) sb.append("   - ").append(r).append("\n");
        }
        sb.append("\n");

        sb.append("2. ESTADO DEL REPORTAJE:\n");
        if (informeActual.isTieneReportaje()) {
            sb.append("   - Entregado SI\n");
            sb.append("   - Entregado por: ").append(informeActual.getReporteroEntrega()).append("\n");
        } else {
            sb.append("   - Entregado NO\n");
        }
        sb.append("\n");

        sb.append("3. EMPRESAS CON ACCESO CONCEDIDO:\n");
        if (informeActual.getEmpresasConAcceso().isEmpty()) {
            sb.append("   \n");
        } else {
            for (String e : informeActual.getEmpresasConAcceso()) sb.append("   - ").append(e).append("\n");
        }

        view.getTxtInforme().setText(sb.toString());
        view.getBtnExportarCSV().setEnabled(true);
    }

    private void exportarACSV() {
        if (informeActual == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Informe CSV");
        fileChooser.setSelectedFile(new java.io.File("Informe_" + nombreEventoActual.replaceAll(" ", "_") + ".csv"));

        int userSelection = fileChooser.showSaveDialog(view.getFrame());
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter fw = new FileWriter(fileToSave)) {
                
                // Cabeceras CSV
                fw.write("Evento,Reporteros_Asignados,Reportaje_Entregado,Reportero_Entrega,Empresas_Con_Acceso\n");
                
                // Formatear listas a strings separadas por un guion o espacio
                String reporteros = String.join(" | ", informeActual.getReporterosAsignados());
                if(reporteros.isEmpty()) reporteros = "Ninguno";
                
                String entregado = informeActual.isTieneReportaje() ? "SI" : "NO";
                String reporteroEntrega = informeActual.getReporteroEntrega();
                
                String empresas = String.join(" | ", informeActual.getEmpresasConAcceso());
                if(empresas.isEmpty()) empresas = "Ninguna";

                // Escribir la línea de datos
                fw.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                        nombreEventoActual, reporteros, entregado, reporteroEntrega, empresas));

                JOptionPane.showMessageDialog(view.getFrame(), "CSV exportado correctamente a:\n" + fileToSave.getAbsolutePath(), "Éxito", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(view.getFrame(), "Error al guardar el archivo:\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
