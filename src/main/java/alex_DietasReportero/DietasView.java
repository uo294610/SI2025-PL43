package alex_DietasReportero;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class DietasView {
    private JFrame frame;
    private JComboBox<Object> cbReporteros;
    private JLabel lblAgencia, lblProvincia, lblPais;
    private JTable tablaEventos;
    private JTextArea txtDesglose;

    public DietasView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Mi Panel de Dietas y Liquidaciones");
        frame.setBounds(100, 100, 950, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("ins 20, fill", "[grow]", "[][][grow][grow][]"));

        // Selector y Cabecera de Datos
        JPanel pnlCabecera = new JPanel(new MigLayout("ins 10", "[][grow][][grow]", "[][]"));
        pnlCabecera.setBorder(BorderFactory.createEtchedBorder());
        
        pnlCabecera.add(new JLabel("Reportero:"), "right");
        cbReporteros = new JComboBox<>();
        pnlCabecera.add(cbReporteros, "wmin 250");
        
        pnlCabecera.add(new JLabel("Provincia Base:"), "right, gapleft 30");
        lblProvincia = new JLabel("---");
        lblProvincia.setFont(new Font("Tahoma", Font.BOLD, 12));
        pnlCabecera.add(lblProvincia, "wrap");

        pnlCabecera.add(new JLabel("Agencia:"), "right");
        lblAgencia = new JLabel("---");
        lblAgencia.setFont(new Font("Tahoma", Font.BOLD, 12));
        pnlCabecera.add(lblAgencia);

        pnlCabecera.add(new JLabel("País Base:"), "right, gapleft 30");
        lblPais = new JLabel("---");
        lblPais.setFont(new Font("Tahoma", Font.BOLD, 12));
        pnlCabecera.add(lblPais);
        
        frame.getContentPane().add(pnlCabecera, "growx, wrap");

        // Título de la tabla
        frame.getContentPane().add(new JLabel("LISTADO DE EVENTOS ASIGNADOS (Histórico Completo)"), "gaptop 10, wrap");

        // Tabla
        tablaEventos = new JTable();
        tablaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        frame.getContentPane().add(new JScrollPane(tablaEventos), "grow, height 150!, wrap");

        // Desglose de cálculo
        txtDesglose = new JTextArea();
        txtDesglose.setEditable(false);
        txtDesglose.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtDesglose.setBackground(new Color(245, 245, 245));
        
        JScrollPane spDesglose = new JScrollPane(txtDesglose);
        spDesglose.setBorder(new TitledBorder(null, "DESGLOSE DE CÁLCULO", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12)));
        frame.getContentPane().add(spDesglose, "grow, gaptop 10, wrap");

    }

    public JFrame getFrame() { return frame; }
    public JComboBox<Object> getCbReporteros() { return cbReporteros; }
    public JLabel getLblAgencia() { return lblAgencia; }
    public JLabel getLblProvincia() { return lblProvincia; }
    public JLabel getLblPais() { return lblPais; }
    public JTable getTablaEventos() { return tablaEventos; }
    public JTextArea getTxtDesglose() { return txtDesglose; }
}