package nico_EntregarReportEvento;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class EntregaReportajeView {
    private JFrame frame;
    private JTable tabEventos;
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea areaCuerpo;
    private JButton btnEntregar;
    private JButton btnCancelar;
    private JComboBox<ReporteroDisplayDTO> cbReporteros;

    public EntregaReportajeView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Entregar Reportaje de Evento (#33603)");
        frame.setBounds(100, 100, 900, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Dividimos la pantalla: 2 columnas, 4 filas
        frame.getContentPane().setLayout(new MigLayout("", "[400px,grow][500px,grow]", "[][][grow][]"));

        // --- COLUMNA IZQUIERDA ---
        
        // 1. Selector de Reportero (Celda 0,0)
        // Usamos split 2 para que texto y desplegable compartan la fila
        frame.getContentPane().add(new JLabel("Reportero:"), "cell 0 0, split 2, alignx left");
        cbReporteros = new JComboBox<ReporteroDisplayDTO>();
        frame.getContentPane().add(cbReporteros, "cell 0 0, growx");

        // 2. Título de la tabla (Celda 0,1)
        frame.getContentPane().add(new JLabel("Eventos Asignados (Pendientes):"), "cell 0 1, gaptop 10");

        // 3. Tabla de eventos (Celda 0,2)
        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTable = new JScrollPane(tabEventos);
        frame.getContentPane().add(scrollTable, "cell 0 2, grow");


        // --- COLUMNA DERECHA ---
        
        // 4. Panel del Formulario
        JPanel panelForm = new JPanel(new MigLayout("", "[][grow]", "[][][grow]"));
        panelForm.setBorder(BorderFactory.createTitledBorder("Contenido del Reportaje"));
        
        panelForm.add(new JLabel("Título (único):"), "cell 0 0");
        txtTitulo = new JTextField();
        panelForm.add(txtTitulo, "cell 1 0, growx");

        panelForm.add(new JLabel("Subtítulo:"), "cell 0 1");
        txtSubtitulo = new JTextField();
        panelForm.add(txtSubtitulo, "cell 1 1, growx");

        panelForm.add(new JLabel("Cuerpo:"), "cell 0 2, aligny top");
        areaCuerpo = new JTextArea();
        areaCuerpo.setLineWrap(true);
        areaCuerpo.setWrapStyleWord(true);
        JScrollPane scrollCuerpo = new JScrollPane(areaCuerpo);
        panelForm.add(scrollCuerpo, "cell 1 2, grow");

        // Añadimos el panel a la celda 1,0 y le decimos que ocupe 3 filas hacia abajo (span y 3)
        frame.getContentPane().add(panelForm, "cell 1 0 1 3, grow");


        // --- FILA INFERIOR (Botones) ---
        
        btnEntregar = new JButton("Entregar");
        frame.getContentPane().add(btnEntregar, "flowx, cell 1 3, alignx right, width 100!");

        btnCancelar = new JButton("Cancelar");
        frame.getContentPane().add(btnCancelar, "cell 1 3, width 100!");
    }

    // --- GETTERS ---
    public JFrame getFrame() { return frame; }
    public JComboBox<ReporteroDisplayDTO> getCbReporteros() { return cbReporteros; }
    public JTable getTabEventos() { return tabEventos; }
    public JTextField getTxtTitulo() { return txtTitulo; }
    public JTextField getTxtSubtitulo() { return txtSubtitulo; }
    public JTextArea getAreaCuerpo() { return areaCuerpo; }
    public JButton getBtnEntregar() { return btnEntregar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
