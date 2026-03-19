package revisionOtrosReportajes;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import nico_EntregarReportEvento.ReporteroDisplayDTO;

public class RevisionOtrosReportajesView {
    private JFrame frame;
    private JComboBox<ReporteroDisplayDTO> cbRevisores;
    private JTable tabRevisiones;
    
    // Lectura del reportaje
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea areaCuerpo;
    private JTable tabMultimedia;
    
    // Comentarios y acciones
    private JTable tabComentarios;
    private JTextArea areaNuevoComentario;
    private JButton btnAñadirComentario;
    private JButton btnFinalizarRevision;

    public RevisionOtrosReportajesView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Centro de Revisión de Reportajes (#34113)");
        frame.setBounds(100, 100, 1100, 800);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[400px,grow][650px,grow]", "[][grow]"));

        // --- FILA 0: Selector Superior ---
        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelTop.add(new JLabel("Reportero (Revisor):")); 
        cbRevisores = new JComboBox<ReporteroDisplayDTO>();
        panelTop.add(cbRevisores);
        frame.getContentPane().add(panelTop, "cell 0 0 2 1, growx");

        // --- COLUMNA IZQUIERDA: Lista de Revisiones ---
        JPanel panelIzquierdo = new JPanel(new MigLayout("", "[grow]", "[grow][]")); 
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("1. Reportajes para revisar"));
        
        tabRevisiones = new JTable();
        tabRevisiones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // (La configuración visual de las columnas la hemos movido al Controlador para evitar el error 3 >= 0)
        panelIzquierdo.add(new JScrollPane(tabRevisiones), "cell 0 1, grow");
        
        btnFinalizarRevision = new JButton("Marcar como Finalizada");
        btnFinalizarRevision.setFont(new Font("Tahoma", Font.BOLD, 12));
        panelIzquierdo.add(btnFinalizarRevision, "cell 0 2, alignx center, gaptop 20");
        
        frame.getContentPane().add(panelIzquierdo, "cell 0 1, grow");

        // --- COLUMNA DERECHA: Visor y Comentarios ---
        JPanel panelDerecho = new JPanel(new MigLayout("insets 0", "[grow]", "[grow][][grow]"));
        
        // Visor de Textos (Bloqueado)
        JPanel panelLectura = new JPanel(new MigLayout("", "[][grow]", "[][][grow]"));
        panelLectura.setBorder(BorderFactory.createTitledBorder("Contenido del Reportaje (Sólo Lectura)"));
        panelLectura.add(new JLabel("Título:"), "cell 0 0");
        txtTitulo = new JTextField(); txtTitulo.setEditable(false);
        panelLectura.add(txtTitulo, "cell 1 0, growx");
        panelLectura.add(new JLabel("Subtítulo:"), "cell 0 1");
        txtSubtitulo = new JTextField(); txtSubtitulo.setEditable(false);
        panelLectura.add(txtSubtitulo, "cell 1 1, growx");
        panelLectura.add(new JLabel("Cuerpo:"), "cell 0 2, aligny top");
        areaCuerpo = new JTextArea(); areaCuerpo.setEditable(false);
        areaCuerpo.setLineWrap(true); areaCuerpo.setWrapStyleWord(true);
        panelLectura.add(new JScrollPane(areaCuerpo), "cell 1 2, grow");
        panelDerecho.add(panelLectura, "cell 0 0, grow");

        // Multimedia Adjunta
        JPanel panelMulti = new JPanel(new MigLayout("", "[grow]", "[grow]"));
        panelMulti.setBorder(BorderFactory.createTitledBorder("Multimedia adjunta"));
        tabMultimedia = new JTable();
        panelMulti.add(new JScrollPane(tabMultimedia), "cell 0 0, grow");
        panelDerecho.add(panelMulti, "cell 0 1, grow");

        // Zona de Comentarios
        JPanel panelComentarios = new JPanel(new MigLayout("", "[grow][]", "[grow][grow][]"));
        panelComentarios.setBorder(BorderFactory.createTitledBorder("Zona de Revisión"));
        tabComentarios = new JTable();
        panelComentarios.add(new JScrollPane(tabComentarios), "cell 0 0 2 1, grow");
        
        panelComentarios.add(new JLabel("Nuevo comentario:"), "cell 0 1 2 1");
        areaNuevoComentario = new JTextArea(3, 20);
        panelComentarios.add(new JScrollPane(areaNuevoComentario), "cell 0 2, grow");
        
        btnAñadirComentario = new JButton("Guardar Comentario"); 
        panelComentarios.add(btnAñadirComentario, "cell 1 2, aligny bottom");
        panelDerecho.add(panelComentarios, "cell 0 2, grow");

        frame.getContentPane().add(panelDerecho, "cell 1 1, grow");
    }

    // Getters
    public JFrame getFrame() { return frame; }
    public JComboBox<ReporteroDisplayDTO> getCbRevisores() { return cbRevisores; }
    public JTable getTabRevisiones() { return tabRevisiones; }
    public JTextField getTxtTitulo() { return txtTitulo; }
    public JTextField getTxtSubtitulo() { return txtSubtitulo; }
    public JTextArea getAreaCuerpo() { return areaCuerpo; }
    public JTable getTabMultimedia() { return tabMultimedia; }
    public JTable getTabComentarios() { return tabComentarios; }
    public JTextArea getAreaNuevoComentario() { return areaNuevoComentario; }
    public JButton getBtnAñadirComentario() { return btnAñadirComentario; }
    public JButton getBtnFinalizarRevision() { return btnFinalizarRevision; }
}