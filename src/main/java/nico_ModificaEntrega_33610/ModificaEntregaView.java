package nico_ModificaEntrega_33610;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.FlowLayout;

// Importamos el DTO de la historia anterior para reutilizarlo
import nico_EntregarReportEvento.ReporteroDisplayDTO;

public class ModificaEntregaView {
    private JFrame frame;
    private JTable tabEventos;
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea areaCuerpo;
    private JButton btnEntregar; 
    private JButton btnGuardarCambio; 
    private JButton btnCancelar;
    private JComboBox<ReporteroDisplayDTO> cbReporteros;
    
    // Nuevos componentes para la HU 33610
    private JRadioButton rdbtnPendientes;
    private JRadioButton rdbtnEntregados;
    private JLabel lblPermisoModificar;

    public ModificaEntregaView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Modificar Entrega de Reportaje (#33610)");
        frame.setBounds(100, 100, 950, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        frame.getContentPane().setLayout(new MigLayout("", "[450px,grow][500px,grow]", "[][][grow][][]"));

        // --- FILA 0: Selector de Reportero y Filtro ---
        frame.getContentPane().add(new JLabel("Reportero:"), "cell 0 0, split 2, alignx left");
        cbReporteros = new JComboBox<ReporteroDisplayDTO>();
        frame.getContentPane().add(cbReporteros, "cell 0 0, growx");

        // Panel para los RadioButtons (Filtro)
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.add(new JLabel("Eventos:"));
        rdbtnPendientes = new JRadioButton("Pendiente", true); // Seleccionado por defecto
        rdbtnEntregados = new JRadioButton("Entregado");
        
        ButtonGroup grupoFiltro = new ButtonGroup();
        grupoFiltro.add(rdbtnPendientes);
        grupoFiltro.add(rdbtnEntregados);
        
        panelFiltro.add(rdbtnPendientes);
        panelFiltro.add(rdbtnEntregados);
        frame.getContentPane().add(panelFiltro, "cell 1 0, alignx left");

        // --- FILA 1: Título de la tabla ---
        frame.getContentPane().add(new JLabel("Eventos Asignados:"), "cell 0 1, gaptop 10");

        // --- FILA 2: Tabla y Formulario ---
        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollTable = new JScrollPane(tabEventos);
        frame.getContentPane().add(scrollTable, "cell 0 2, grow");

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

        frame.getContentPane().add(panelForm, "cell 1 1 1 3, grow");

        // --- FILAS INFERIORES IZQUIERDA (Controles de modificación) ---
        lblPermisoModificar = new JLabel("¿Puede modificar?: -");
        frame.getContentPane().add(lblPermisoModificar, "cell 0 3");
        
        btnGuardarCambio = new JButton("Guardar Cambio");
        btnGuardarCambio.setVisible(false); // Oculto por defecto
        frame.getContentPane().add(btnGuardarCambio, "cell 0 4, alignx left");

        // --- FILA INFERIOR DERECHA ---
        btnEntregar = new JButton("Entregar");
        frame.getContentPane().add(btnEntregar, "flowx, cell 1 4, alignx right, width 100!");

        btnCancelar = new JButton("Cancelar");
        frame.getContentPane().add(btnCancelar, "cell 1 4, width 100!");
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
    public JRadioButton getRdbtnPendientes() { return rdbtnPendientes; }
    public JRadioButton getRdbtnEntregados() { return rdbtnEntregados; }
    public JButton getBtnGuardarCambio() { return btnGuardarCambio; }
    public JLabel getLblPermisoModificar() { return lblPermisoModificar; }
}