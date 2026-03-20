package alex_InformeReportajes;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class InformeReportajesView {
    private JFrame frame;
    private JComboBox<Object> cbEmpresas;
    private JComboBox<Object> cbAgencias;
    private JTextField txtFechaInicio, txtFechaFin;
    private JTable tablaReportajes;
    private JTextArea txtInforme;

    public InformeReportajesView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Generación de Informes de Reportajes");
        frame.setBounds(100, 100, 900, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("ins 20, fillx", "[grow]", "[][][grow][grow]"));

        // Empresa y Agencia
        JPanel pnlCabecera = new JPanel(new MigLayout("ins 0", "[][grow][][grow]", "[]"));
        pnlCabecera.add(new JLabel("Empresa:"));
        cbEmpresas = new JComboBox<>();
        pnlCabecera.add(cbEmpresas, "wmin 200");
        
        pnlCabecera.add(new JLabel("Seleccione Agencia de Prensa:"), "gapleft 20");
        cbAgencias = new JComboBox<>();
        pnlCabecera.add(cbAgencias, "wmin 200");
        frame.getContentPane().add(pnlCabecera, "growx, wrap");

        // Fechas
        JPanel pnlFechas = new JPanel(new MigLayout("ins 0", "[][][][][]", "[]"));
        pnlFechas.add(new JLabel("Fecha Inicio (YYYY-MM-DD):"));
        txtFechaInicio = new JTextField("2026-01-01", 10);
        pnlFechas.add(txtFechaInicio);
        
        pnlFechas.add(new JLabel("Fecha Fin:"), "gapleft 20");
        txtFechaFin = new JTextField("2026-12-31", 10);
        pnlFechas.add(txtFechaFin);
        frame.getContentPane().add(pnlFechas, "growx, gaptop 10, wrap");

        // Tabla
        tablaReportajes = new JTable();
        frame.getContentPane().add(new JScrollPane(tablaReportajes), "grow, gaptop 10, height 150!, wrap");

        // El Informe por pantalla
        txtInforme = new JTextArea();
        txtInforme.setEditable(false);
        txtInforme.setFont(new Font("Monospaced", Font.PLAIN, 13)); 
        txtInforme.setBackground(new Color(245, 245, 245));
        
        JScrollPane spInforme = new JScrollPane(txtInforme);
        spInforme.setBorder(new TitledBorder(null, "INFORME POR PANTALLA:", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12)));
        frame.getContentPane().add(spInforme, "grow, gaptop 10");
    }

    public JFrame getFrame() { return frame; }
    public JComboBox<Object> getCbEmpresas() { return cbEmpresas; }
    public JComboBox<Object> getCbAgencias() { return cbAgencias; }
    public JTextField getTxtFechaInicio() { return txtFechaInicio; }
    public JTextField getTxtFechaFin() { return txtFechaFin; }
    public JTable getTablaReportajes() { return tablaReportajes; }
    public JTextArea getTxtInforme() { return txtInforme; }
}