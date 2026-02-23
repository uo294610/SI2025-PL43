package diego_asignarReporteros;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class ReporteroView {
    private JFrame frame;
    private JTextField txtAgenciaId;
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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][grow][]"));

        // Simulación de sesión de Agencia
        frame.getContentPane().add(new JLabel("ID Agencia (Simulación):"), "flowx,cell 0 0");
        txtAgenciaId = new JTextField("1"); // EFE por defecto
        frame.getContentPane().add(txtAgenciaId, "cell 0 0, width 50!");
        btnCargarEventos = new JButton("Ver Eventos sin asignar");
        frame.getContentPane().add(btnCargarEventos, "cell 0 0");

        // Tabla de Eventos
        frame.getContentPane().add(new JLabel("1. Selecciona un Evento sin asignar:"), "cell 0 1");
        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabEventos.setDefaultEditor(Object.class, null); 
        frame.getContentPane().add(new JScrollPane(tabEventos), "cell 0 2,grow");

        // Tabla de Reporteros
        frame.getContentPane().add(new JLabel("2. Reporteros Disponibles (Ctrl+Click para selección múltiple):"), "cell 0 3");
        tabReporteros = new JTable();
        // Permite seleccionar varios reporteros a la vez
        tabReporteros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabReporteros.setDefaultEditor(Object.class, null);
        frame.getContentPane().add(new JScrollPane(tabReporteros), "cell 0 4,grow");

        // Botón de acción
        btnAsignar = new JButton("Asignar Reportero(s)");
        frame.getContentPane().add(btnAsignar, "cell 0 5, align right");
    }

    // Getters para el controlador
    public JFrame getFrame() { return this.frame; }
    public JTextField getTxtAgenciaId() { return this.txtAgenciaId; }
    public JButton getBtnCargarEventos() { return this.btnCargarEventos; }
    public JTable getTabEventos() { return this.tabEventos; }
    public JTable getTabReporteros() { return this.tabReporteros; }
    public JButton getBtnAsignar() { return this.btnAsignar; }
}