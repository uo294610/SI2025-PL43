package nico_ModificaEntrega_33610;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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
    private JRadioButton rdbtnPendientes;
    private JRadioButton rdbtnEntregados;
    private JLabel lblPermisoModificar;

    // Componentes Multimedia
    private JTable tabImagenes;
    private JTable tabVideos;
    private JButton btnAnadirImagen;
    private JButton btnEliminarImagen;
    private JButton btnFijarImgDefinitiva;
    private JButton btnAnadirVideo;
    private JButton btnEliminarVideo;
    private JButton btnFijarVidDefinitivo;

    // --- COMPONENTES REVISIÓN ---
    private JPanel panelRevision;
    private JButton btnSolicitarRevision;

    public ModificaEntregaView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Modificar Reportaje y Gestionar Revisión (#33610 + #34112)");
        frame.setBounds(100, 100, 1100, 850); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[380px,grow][650px,grow]", "[][][grow][][][]"));

        frame.getContentPane().add(new JLabel("Reportero Activo:"), "cell 0 0, split 2, alignx left");
        cbReporteros = new JComboBox<ReporteroDisplayDTO>();
        frame.getContentPane().add(cbReporteros, "cell 0 0, growx");

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelFiltro.add(new JLabel("Eventos:"));
        rdbtnPendientes = new JRadioButton("Pendiente", true);
        rdbtnEntregados = new JRadioButton("Entregado");
        ButtonGroup grupoFiltro = new ButtonGroup();
        grupoFiltro.add(rdbtnPendientes);
        grupoFiltro.add(rdbtnEntregados);
        panelFiltro.add(rdbtnPendientes);
        panelFiltro.add(rdbtnEntregados);
        frame.getContentPane().add(panelFiltro, "cell 1 0, alignx left");

        frame.getContentPane().add(new JLabel("Eventos Asignados:"), "cell 0 1, gaptop 10");

        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        frame.getContentPane().add(new JScrollPane(tabEventos), "cell 0 2, grow"); 

        JPanel panelDerecho = new JPanel(new MigLayout("insets 0", "[grow]", "[][grow]"));
        
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
        panelForm.add(new JScrollPane(areaCuerpo), "cell 1 2, grow");
        panelDerecho.add(panelForm, "cell 0 0, grow");

        JPanel panelMultimedia = new JPanel(new MigLayout("", "[grow][]", "[][grow][][grow]"));
        panelMultimedia.setBorder(BorderFactory.createTitledBorder("Contenido Multimedia"));
        
        panelMultimedia.add(new JLabel("- Imágenes:"), "cell 0 0");
        tabImagenes = new JTable();
        panelMultimedia.add(new JScrollPane(tabImagenes), "cell 0 1, grow");
        JPanel panelBotonesImg = new JPanel(new MigLayout("insets 0", "[]", "[][][]"));
        btnAnadirImagen = new JButton("Añadir Imagen");
        btnEliminarImagen = new JButton("Eliminar Imagen");
        btnFijarImgDefinitiva = new JButton("Fijar Definitiva");
        panelBotonesImg.add(btnAnadirImagen, "cell 0 0, growx");
        panelBotonesImg.add(btnEliminarImagen, "cell 0 1, growx");
        panelBotonesImg.add(btnFijarImgDefinitiva, "cell 0 2, growx");
        panelMultimedia.add(panelBotonesImg, "cell 1 1, aligny top");

        panelMultimedia.add(new JLabel("- Vídeos:"), "cell 0 2");
        tabVideos = new JTable();
        panelMultimedia.add(new JScrollPane(tabVideos), "cell 0 3, grow");
        JPanel panelBotonesVid = new JPanel(new MigLayout("insets 0", "[]", "[][][]"));
        btnAnadirVideo = new JButton("Añadir Vídeo");
        btnEliminarVideo = new JButton("Eliminar Vídeo");
        btnFijarVidDefinitivo = new JButton("Fijar Definitiva");
        panelBotonesVid.add(btnAnadirVideo, "cell 0 0, growx");
        panelBotonesVid.add(btnEliminarVideo, "cell 0 1, growx");
        panelBotonesVid.add(btnFijarVidDefinitivo, "cell 0 2, growx");
        panelMultimedia.add(panelBotonesVid, "cell 1 3, aligny top");

        panelDerecho.add(panelMultimedia, "cell 0 1, grow");
        frame.getContentPane().add(panelDerecho, "cell 1 2 1 3, grow");

        // --- PANEL DE EDICIÓN Y REVISIÓN (IZQUIERDA ABAJO) ---
        lblPermisoModificar = new JLabel("¿Puede modificar?: -");
        lblPermisoModificar.setFont(new Font("Tahoma", Font.BOLD, 12));
        frame.getContentPane().add(lblPermisoModificar, "cell 0 3");
        
        btnGuardarCambio = new JButton("Guardar Cambio");
        btnGuardarCambio.setVisible(false);
        frame.getContentPane().add(btnGuardarCambio, "cell 0 4, alignx left");

        // Nuevo Panel de Revisión Limpio (Sin ComboBox)
        panelRevision = new JPanel(new MigLayout("", "[grow]", "[]"));
        panelRevision.setBorder(BorderFactory.createTitledBorder("Gestión de Revisión"));
        btnSolicitarRevision = new JButton("Solicitar Revisión");
        panelRevision.add(btnSolicitarRevision, "align center");
        panelRevision.setVisible(false); // Oculto por defecto
        frame.getContentPane().add(panelRevision, "cell 0 5, growx");

        JPanel panelAccionesFinales = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEntregar = new JButton("Entregar");
        btnCancelar = new JButton("Cancelar");
        panelAccionesFinales.add(btnEntregar);
        panelAccionesFinales.add(btnCancelar);
        frame.getContentPane().add(panelAccionesFinales, "cell 1 5, growx");
    }

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
    public JTable getTabImagenes() { return tabImagenes; }
    public JTable getTabVideos() { return tabVideos; }
    public JButton getBtnAnadirImagen() { return btnAnadirImagen; }
    public JButton getBtnEliminarImagen() { return btnEliminarImagen; }
    public JButton getBtnFijarImgDefinitiva() { return btnFijarImgDefinitiva; }
    public JButton getBtnAnadirVideo() { return btnAnadirVideo; }
    public JButton getBtnEliminarVideo() { return btnEliminarVideo; }
    public JButton getBtnFijarVidDefinitivo() { return btnFijarVidDefinitivo; }
    
    public JPanel getPanelRevision() { return panelRevision; }
    public JButton getBtnSolicitarRevision() { return btnSolicitarRevision; }
}