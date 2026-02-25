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
    private JRadioButton rbSinAsignar;
    private JRadioButton rbConAsignar;
    private JButton btnEliminar;
    
    public ReporteroView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestión de Asignaciones de Reporteros");
        frame.setBounds(0, 0, 650, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][][grow][][grow][][grow][]"));

        // Cabecera: ID Agencia y Filtro
        frame.getContentPane().add(new JLabel("ID Agencia:"), "flowx,cell 0 0");
        txtAgenciaId = new JTextField("1"); 
        frame.getContentPane().add(txtAgenciaId, "cell 0 0, width 50!");
        
        // 1. FILTRO (NUEVO)
        rbSinAsignar = new JRadioButton("Eventos SIN reporteros", true); // Seleccionado por defecto
        rbConAsignar = new JRadioButton("Eventos CON reporteros");
        ButtonGroup bg = new ButtonGroup(); // Agrupa para que solo se pueda elegir uno
        bg.add(rbSinAsignar);
        bg.add(rbConAsignar);
        frame.getContentPane().add(rbSinAsignar, "cell 0 1");
        frame.getContentPane().add(rbConAsignar, "cell 0 1");
        
        btnCargarEventos = new JButton("Cargar Eventos (Aplicar Filtro)");
        frame.getContentPane().add(btnCargarEventos, "cell 0 1");

        // Tabla de Eventos
        frame.getContentPane().add(new JLabel("1. Selecciona un Evento:"), "cell 0 2");
        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabEventos.setDefaultEditor(Object.class, null); 
        frame.getContentPane().add(new JScrollPane(tabEventos), "cell 0 3,grow");

        // Tabla de Reporteros Disponibles
        frame.getContentPane().add(new JLabel("2. Disponibles (Ctrl+Click para varios):"), "cell 0 4");
        tabReporteros = new JTable();
        tabReporteros.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabReporteros.setDefaultEditor(Object.class, null);
        frame.getContentPane().add(new JScrollPane(tabReporteros), "cell 0 5,grow");

        // Botón Asignar
        btnAsignar = new JButton("Asignar Reportero(s) ↓");
        frame.getContentPane().add(btnAsignar, "cell 0 6, align center");

        // Tabla de Reporteros Asignados (YA SE PUEDEN SELECCIONAR)
        frame.getContentPane().add(new JLabel("3. YA Asignados (Selecciona para eliminar):"), "cell 0 7");
        tabReporterosAsignados = new JTable();
        tabReporterosAsignados.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        tabReporterosAsignados.setDefaultEditor(Object.class, null);
        frame.getContentPane().add(new JScrollPane(tabReporterosAsignados), "cell 0 8,grow");

        // Botón Eliminar
        btnEliminar = new JButton("Eliminar Reportero(s) ↑");
        frame.getContentPane().add(btnEliminar, "cell 0 9, align right");
    }

    // Getters para el controlador
    public JFrame getFrame() { return frame; }
    public JTextField getTxtAgenciaId() { return txtAgenciaId; }
    public JButton getBtnCargarEventos() { return btnCargarEventos; }
    public JTable getTabEventos() { return tabEventos; }
    public JTable getTabReporteros() { return tabReporteros; }
    public JTable getTabReporterosAsignados() { return tabReporterosAsignados; }
    public JButton getBtnAsignar() { return btnAsignar; }
    
    // Getters NUEVOS
    public JRadioButton getRbSinAsignar() { return rbSinAsignar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}