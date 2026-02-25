package alex_InformeEvento_33613;

import javax.swing.*;
import java.awt.*;
import net.miginfocom.swing.MigLayout;

public class InformeEventosView {
	private JFrame frame;
    private JComboBox<Object> cbAgencias;
    private JTable tabEventos;
    private JTextArea txtInforme;
    private JButton btnExportarCSV;

    public InformeEventosView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Generación de Informes de Eventos");
        frame.setBounds(100, 100, 850, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[400px:400px,grow][grow]", "[][grow][]"));

        // Cabecera Agencias
        JPanel pnlTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTop.add(new JLabel("Seleccione Agencia de Prensa:"));
        cbAgencias = new JComboBox<>();
        pnlTop.add(cbAgencias);
        frame.getContentPane().add(pnlTop, "span, wrap");

        // Tabla Eventos 
        tabEventos = new JTable() { public boolean isCellEditable(int r, int c) { return false; } };
        frame.getContentPane().add(new JScrollPane(tabEventos), "grow");

        // Área del Informe 
        txtInforme = new JTextArea();
        txtInforme.setEditable(false);
        txtInforme.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtInforme.setMargin(new Insets(10, 10, 10, 10));
        frame.getContentPane().add(new JScrollPane(txtInforme), "grow, wrap");

        // Botón Exportar (Abajo)
        btnExportarCSV = new JButton("Exportar a CSV");
        btnExportarCSV.setEnabled(false);
        frame.getContentPane().add(btnExportarCSV, "skip 1, right");
    }

    public JFrame getFrame() { return frame; }
    public JComboBox<Object> getCbAgencias() { return cbAgencias; }
    public JTable getTablaEventos() { return tabEventos; }
    public JTextArea getTxtInforme() { return txtInforme; }
    public JButton getBtnExportarCSV() { return btnExportarCSV; }
}

