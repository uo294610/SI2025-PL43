package nico_EntregarReportEvento;


import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import javax.swing.table.DefaultTableModel;

public class EntregaReportajeView {
    private JFrame frame;
    private JTable tabEventos;
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea areaCuerpo;
    private JButton btnEntregar;
    private JButton btnCancelar;
    private JLabel lblReportero;

    public EntregaReportajeView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Entregar Reportaje de Evento (#33603)");
        frame.setBounds(100, 100, 900, 550);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Usamos MigLayout para dividir la pantalla en dos columnas
        frame.getContentPane().setLayout(new MigLayout("", "[400px,grow][500px,grow]", "[][][grow][]"));

        lblReportero = new JLabel("Reportero: Juan Pérez Fdez"); // Según tu prototipo
        frame.getContentPane().add(lblReportero, "cell 0 0, gapbottom 10");

        frame.getContentPane().add(new JLabel("Eventos Asignados (Pendientes):"), "cell 0 1");
        
        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTable = new JScrollPane(tabEventos);
        frame.getContentPane().add(scrollTable, "cell 0 2, grow");

        // Panel de Formulario (Derecha)
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

        frame.getContentPane().add(panelForm, "cell 1 2, grow");

        btnEntregar = new JButton("Entregar");
        frame.getContentPane().add(btnEntregar, "flowx, cell 1 3, alignx right, width 100!");

        btnCancelar = new JButton("Cancelar");
        frame.getContentPane().add(btnCancelar, "cell 1 3, width 100!");
    }

    // Getters para que el Controller pueda interactuar
    public JFrame getFrame() { return frame; }
    public JTable getTabEventos() { return tabEventos; }
    public JTextField getTxtTitulo() { return txtTitulo; }
    public JTextField getTxtSubtitulo() { return txtSubtitulo; }
    public JTextArea getAreaCuerpo() { return areaCuerpo; }
    public JButton getBtnEntregar() { return btnEntregar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}
