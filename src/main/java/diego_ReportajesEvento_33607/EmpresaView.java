package diego_ReportajesEvento_33607;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class EmpresaView {
    private JFrame frame;
    private JTextField txtEmpresaId;
    private JButton btnCargarEventos;
    private JTable tabEventos;
    
    // Componentes para ver el reportaje
    private JTextField txtTitulo;
    private JTextField txtSubtitulo;
    private JTextArea txtCuerpo;

    public EmpresaView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Lector de Reportajes (Empresas)");
        frame.setBounds(0, 0, 700, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][][][grow]"));

        // Simulación de sesión de Empresa
        frame.getContentPane().add(new JLabel("ID Empresa (Simulación):"), "flowx,cell 0 0");
        txtEmpresaId = new JTextField("200"); // Atresmedia por defecto (id 200)
        frame.getContentPane().add(txtEmpresaId, "cell 0 0, width 50!");
        btnCargarEventos = new JButton("Ver Eventos Autorizados");
        frame.getContentPane().add(btnCargarEventos, "cell 0 0");

        // Tabla de Eventos
        frame.getContentPane().add(new JLabel("1. Eventos disponibles:"), "cell 0 1");
        tabEventos = new JTable();
        tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabEventos.setDefaultEditor(Object.class, null); 
        frame.getContentPane().add(new JScrollPane(tabEventos), "cell 0 2,grow");

        // Visor del Reportaje
        frame.getContentPane().add(new JLabel("Título:"), "flowx, cell 0 3");
        txtTitulo = new JTextField();
        txtTitulo.setEditable(false); // Solo lectura
        frame.getContentPane().add(txtTitulo, "cell 0 3, growx");

        frame.getContentPane().add(new JLabel("Subtítulo:"), "flowx, cell 0 4");
        txtSubtitulo = new JTextField();
        txtSubtitulo.setEditable(false);
        frame.getContentPane().add(txtSubtitulo, "cell 0 4, growx");

        frame.getContentPane().add(new JLabel("Cuerpo del reportaje:"), "cell 0 5");
        txtCuerpo = new JTextArea();
        txtCuerpo.setEditable(false);
        txtCuerpo.setLineWrap(true);
        txtCuerpo.setWrapStyleWord(true);
        frame.getContentPane().add(new JScrollPane(txtCuerpo), "cell 0 6,grow");
    }

    public JFrame getFrame() { return frame; }
    public JTextField getTxtEmpresaId() { return txtEmpresaId; }
    public JButton getBtnCargarEventos() { return btnCargarEventos; }
    public JTable getTabEventos() { return tabEventos; }
    public JTextField getTxtTitulo() { return txtTitulo; }
    public JTextField getTxtSubtitulo() { return txtSubtitulo; }
    public JTextArea getTxtCuerpo() { return txtCuerpo; }
}  
