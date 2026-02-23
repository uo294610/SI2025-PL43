package diego_asignarReporteros_33602;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class ReporteroView {
    private JFrame frame;
    private JTextField txtAgenciaId;
    private JButton btnCargarEventos;
    private JTable tabEventos;
    private JTable tabReporteros;
    private JTable tabReporterosAsignados; // NUEVA TABLA
    private JButton btnAsignar;

    public ReporteroView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Asignación de Reporteros");
        frame.setBounds(0, 0, 600, 650); // He hecho la ventana un poco más alta
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Mejorado para que no cierre todo el programa
        
        // Fíjate que he añadido más filas al layout: [][][grow][][grow][][grow][]
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][grow][][grow][]"));

        // Simulación de sesión de Agencia
        frame.getContentPane().add(new JLabel("ID Agencia (Simulación):"), "flowx,cell 0 0");
        txtAgenciaId = new JTextField("1"); 
        frame.getContentPane().add(txtAgenciaId, "cell 0 0, width 50!");
        btnCargarEventos = new JButton("Ver Eventos sin asignar");
        frame.getContentPane().add(btnCargarEventos, "cell 0 0");

        // 1. Tabla de Eventos
        frame.getContentPane().add(new JLabel("1. Selecciona un Evento sin asignar:"), "cell 0 1");
        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabEventos.setDefaultEditor(Object.class, null); 
        frame.getContentPane().add(new JScrollPane(tabEventos), "cell 0 2,grow");

        // 2. Tabla de Reporteros Disponibles
        frame.getContentPane().add(new JLabel("2. Reporteros Disponibles (Ctrl+Click para selección múltiple):"), "cell 0 3");
        tabReporteros = new JTable();
        tabReporteros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabReporteros.setDefaultEditor(Object.class, null);
        frame.getContentPane().add(new JScrollPane(tabReporteros), "cell 0 4,grow");

        // 3. NUEVA: Tabla de Reporteros Asignados
        frame.getContentPane().add(new JLabel("3. Reporteros YA Asignados a este Evento:"), "cell 0 5");
        tabReporterosAsignados = new JTable();
        tabReporterosAsignados.setRowSelectionAllowed(false);
        tabReporterosAsignados.setDefaultEditor(Object.class, null);
        frame.getContentPane().add(new JScrollPane(tabReporterosAsignados), "cell 0 6,grow");

        // Botón de acción
        btnAsignar = new JButton("Asignar Reportero(s)");
        frame.getContentPane().add(btnAsignar, "cell 0 7, align right");
    }

    // Getters para el controlador
    public JFrame getFrame() { return this.frame; }
    public JTextField getTxtAgenciaId() { return this.txtAgenciaId; }
    public JButton getBtnCargarEventos() { return this.btnCargarEventos; }
    public JTable getTabEventos() { return this.tabEventos; }
    public JTable getTabReporteros() { return this.tabReporteros; }
    public JButton getBtnAsignar() { return this.btnAsignar; }
    public JTable getTabReporterosAsignados() { return this.tabReporterosAsignados; }
}