package nico_RestaurarVersionReport_33612;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class RestaurarVersionView {
    private JFrame frame;
    
    // Cabecera
    private JTextField txtEvento;
    private JTextField txtTituloGeneral;

    // Tabla de versiones
    private JTable tabVersiones;
    private JButton btnRestaurar;

    // Panel Versión Actual
    private JTextField txtTituloActual;
    private JTextField txtSubtituloActual;
    private JTextArea areaCuerpoActual;

    // Panel Versión Seleccionada
    private JTextField txtTituloSeleccionada;
    private JTextField txtSubtituloSeleccionada;
    private JTextArea areaCuerpoSeleccionada;

    // Botones finales
    private JButton btnGuardar;
    private JButton btnCancelar;

    public RestaurarVersionView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Restaurar Versión de Reportaje (#33612)");
        frame.setBounds(100, 100, 950, 700); // Un poco más alta para que quepa todo
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Dividimos en 2 columnas principales
        frame.getContentPane().setLayout(new MigLayout("", "[450px,grow][450px,grow]", "[][grow][]"));

        // --- FILA 0: CABECERA ---
        JPanel panelCabecera = new JPanel(new MigLayout("", "[][grow][][grow]", "[]"));
        panelCabecera.add(new JLabel("Evento:"));
        txtEvento = new JTextField();
        txtEvento.setEditable(false);
        panelCabecera.add(txtEvento, "growx");
        
        panelCabecera.add(new JLabel("Título del Reportaje:"), "gapleft 20");
        txtTituloGeneral = new JTextField();
        txtTituloGeneral.setEditable(false);
        panelCabecera.add(txtTituloGeneral, "growx");
        
        frame.getContentPane().add(panelCabecera, "cell 0 0 2 1, growx, gapbottom 10");

        // --- COLUMNA IZQUIERDA: Versiones ---
        JPanel panelIzquierdo = new JPanel(new MigLayout("", "[grow]", "[][grow][]"));
        panelIzquierdo.add(new JLabel("Versiones del Reportaje:"), "cell 0 0");
        
        tabVersiones = new JTable();
        tabVersiones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollVersiones = new JScrollPane(tabVersiones);
        panelIzquierdo.add(scrollVersiones, "cell 0 1, grow");
        
        btnRestaurar = new JButton("Restaurar");
        panelIzquierdo.add(btnRestaurar, "cell 0 2, alignx left");
        panelIzquierdo.add(new JLabel("* Se generará una nueva versión al guardar"), "cell 0 3");
        
        frame.getContentPane().add(panelIzquierdo, "cell 0 1, grow");

        // --- COLUMNA DERECHA: Comparativa ---
        JPanel panelDerecho = new JPanel(new MigLayout("", "[grow]", "[grow][grow]"));
        
        // 1. Versión Actual
        JPanel panelActual = new JPanel(new MigLayout("", "[][grow]", "[][][grow]"));
        panelActual.setBorder(BorderFactory.createTitledBorder("Versión Actual:"));
        panelActual.add(new JLabel("Título (único):"), "cell 0 0");
        txtTituloActual = new JTextField(); txtTituloActual.setEditable(false);
        panelActual.add(txtTituloActual, "cell 1 0, growx");
        panelActual.add(new JLabel("Subtítulo:"), "cell 0 1");
        txtSubtituloActual = new JTextField(); txtSubtituloActual.setEditable(false);
        panelActual.add(txtSubtituloActual, "cell 1 1, growx");
        panelActual.add(new JLabel("Cuerpo:"), "cell 0 2, aligny top");
        areaCuerpoActual = new JTextArea(); areaCuerpoActual.setEditable(false);
        areaCuerpoActual.setLineWrap(true); areaCuerpoActual.setWrapStyleWord(true);
        panelActual.add(new JScrollPane(areaCuerpoActual), "cell 1 2, grow");
        
        panelDerecho.add(panelActual, "cell 0 0, grow");

        // 2. Versión Seleccionada
        JPanel panelSel = new JPanel(new MigLayout("", "[][grow]", "[][][grow]"));
        panelSel.setBorder(BorderFactory.createTitledBorder("Versión Seleccionada:"));
        panelSel.add(new JLabel("Título (único):"), "cell 0 0");
        txtTituloSeleccionada = new JTextField(); txtTituloSeleccionada.setEditable(false);
        panelSel.add(txtTituloSeleccionada, "cell 1 0, growx");
        panelSel.add(new JLabel("Subtítulo:"), "cell 0 1");
        txtSubtituloSeleccionada = new JTextField(); txtSubtituloSeleccionada.setEditable(false);
        panelSel.add(txtSubtituloSeleccionada, "cell 1 1, growx");
        panelSel.add(new JLabel("Cuerpo:"), "cell 0 2, aligny top");
        areaCuerpoSeleccionada = new JTextArea(); areaCuerpoSeleccionada.setEditable(false);
        areaCuerpoSeleccionada.setLineWrap(true); areaCuerpoSeleccionada.setWrapStyleWord(true);
        panelSel.add(new JScrollPane(areaCuerpoSeleccionada), "cell 1 2, grow");

        panelDerecho.add(panelSel, "cell 0 1, grow");
        frame.getContentPane().add(panelDerecho, "cell 1 1, grow");

        // --- FILA 2: Botones Inferiores ---
        JPanel panelBotones = new JPanel(new MigLayout("", "[][]", "[]"));
        btnGuardar = new JButton("Guardar");
        btnCancelar = new JButton("Cancelar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);
        
        frame.getContentPane().add(panelBotones, "cell 0 2 2 1, alignx left, gaptop 10");
    }

    // --- GETTERS ---
    public JFrame getFrame() { return frame; }
    public JTextField getTxtEvento() { return txtEvento; }
    public JTextField getTxtTituloGeneral() { return txtTituloGeneral; }
    public JTable getTabVersiones() { return tabVersiones; }
    public JButton getBtnRestaurar() { return btnRestaurar; }
    public JTextField getTxtTituloActual() { return txtTituloActual; }
    public JTextField getTxtSubtituloActual() { return txtSubtituloActual; }
    public JTextArea getAreaCuerpoActual() { return areaCuerpoActual; }
    public JTextField getTxtTituloSeleccionada() { return txtTituloSeleccionada; }
    public JTextField getTxtSubtituloSeleccionada() { return txtSubtituloSeleccionada; }
    public JTextArea getAreaCuerpoSeleccionada() { return areaCuerpoSeleccionada; }
    public JButton getBtnGuardar() { return btnGuardar; }
    public JButton getBtnCancelar() { return btnCancelar; }
}