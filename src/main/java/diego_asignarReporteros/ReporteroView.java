package diego_asignarReporteros;

import javax.swing.JFrame;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JButton;

public class ReporteroView {

    private JFrame frame;
    private JTextField txtIdAgencia;
    private JButton btnCargarEventos;
    private JTable tabEventos;
    private JTable tabReporteros;
    private JButton btnAsignar;

    public ReporteroView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Asignación de Reporteros");
        frame.setBounds(0, 0, 600, 500);
        frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][grow][]"));

        JLabel lblAgencia = new JLabel("ID Agencia:");
        frame.getContentPane().add(lblAgencia, "flowx,cell 0 0");

        txtIdAgencia = new JTextField();
        txtIdAgencia.setText("1"); // Valor por defecto (Agencia EFE en tu data.sql)
        frame.getContentPane().add(txtIdAgencia, "cell 0 0");
        txtIdAgencia.setColumns(10);

        btnCargarEventos = new JButton("Ver Eventos sin asignar");
        frame.getContentPane().add(btnCargarEventos, "cell 0 0");

        JLabel lblEventos = new JLabel("Selecciona un Evento:");
        frame.getContentPane().add(lblEventos, "cell 0 1");

        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabEventos.setDefaultEditor(Object.class, null); // readonly
        JScrollPane scrollEventos = new JScrollPane(tabEventos);
        frame.getContentPane().add(scrollEventos, "cell 0 2,grow");

        JLabel lblReporteros = new JLabel("Reporteros Disponibles (Usa Ctrl/Cmd para selección múltiple):");
        frame.getContentPane().add(lblReporteros, "cell 0 3");

        tabReporteros = new JTable();
        // IMPORTANTE: Permitir seleccionar varios reporteros a la vez
        tabReporteros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabReporteros.setDefaultEditor(Object.class, null); // readonly
        JScrollPane scrollReporteros = new JScrollPane(tabReporteros);
        frame.getContentPane().add(scrollReporteros, "cell 0 4,grow");

        btnAsignar = new JButton("Asignar Reporteros Seleccionados");
        frame.getContentPane().add(btnAsignar, "cell 0 5,alignx right");
    }

    // Getters
    public JFrame getFrame() { return this.frame; }
    public String getIdAgencia() { return this.txtIdAgencia.getText(); }
    public JButton getBtnCargarEventos() { return this.btnCargarEventos; }
    public JTable getTablaEventos() { return this.tabEventos; }
    public JTable getTablaReporteros() { return this.tabReporteros; }
    public JButton getBtnAsignar() { return this.btnAsignar; }
}