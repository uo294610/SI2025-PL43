package alex_GestionarOfrecimientos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class OfrecimientosView {
    private JFrame frame;
    private JComboBox<Object> cbEmpresas, cbTematicas;
    private JComboBox<String> cbFiltroEmbargo;
    private JRadioButton rdPendientes, rdDecididos;
    private JCheckBox chkFiltroEmpresa;
    private JTextField txtPrecioMin, txtPrecioMax;
    private JTable tablaOfrecimientos;
    private JTextArea txtDetalleEvento; // Cambio de JTable a JTextArea
    private JButton btnAceptar, btnRechazar, btnEliminar;

    public OfrecimientosView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Modificar Decisión de Ofrecimiento");
        frame.setBounds(100, 100, 1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("ins 20, fill", "[grow]", "[][][][][grow][grow][]"));

        // Empresa y Estado
        JPanel pnl1 = new JPanel(new MigLayout("ins 0", "[][grow][][]", "[]"));
        pnl1.add(new JLabel("Empresa:"));
        cbEmpresas = new JComboBox<>();
        pnl1.add(cbEmpresas, "wmin 200");
        rdPendientes = new JRadioButton("Pendientes", true);
        rdDecididos = new JRadioButton("Decididos");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdPendientes); bg.add(rdDecididos);
        pnl1.add(rdPendientes, "gapleft 20");
        pnl1.add(rdDecididos);
        frame.getContentPane().add(pnl1, "growx, wrap");

        // Temáticas
        JPanel pnl2 = new JPanel(new MigLayout("ins 0", "[][][]", "[]"));
        chkFiltroEmpresa = new JCheckBox("Solo temáticas de la empresa");
        pnl2.add(chkFiltroEmpresa);
        pnl2.add(new JLabel("Temática:"), "gapleft 30");
        cbTematicas = new JComboBox<>();
        pnl2.add(cbTematicas, "wmin 250");
        frame.getContentPane().add(pnl2, "growx, gaptop 5, wrap");

        // Precios
        JPanel pnl3 = new JPanel(new MigLayout("ins 0", "[][][][][]", "[]"));
        pnl3.add(new JLabel("Precio Mínimo:"));
        txtPrecioMin = new JTextField(8);
        pnl3.add(txtPrecioMin);
        pnl3.add(new JLabel("€"), "gapright 20");
        pnl3.add(new JLabel("Precio Máximo:"));
        txtPrecioMax = new JTextField(8);
        pnl3.add(txtPrecioMax);
        pnl3.add(new JLabel("€"));
        frame.getContentPane().add(pnl3, "growx, gaptop 5, wrap");

        // Embargo
        JPanel pnl4 = new JPanel(new MigLayout("ins 0", "[][]", "[]"));
        pnl4.add(new JLabel("Embargo:"));
        cbFiltroEmbargo = new JComboBox<>(new String[]{"Todos", "Embargados", "Sin embargo"});
        pnl4.add(cbFiltroEmbargo, "wmin 150");
        frame.getContentPane().add(pnl4, "growx, gaptop 5, wrap");

        // Tabla Principal
        tablaOfrecimientos = new JTable();
        tablaOfrecimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        frame.getContentPane().add(new JScrollPane(tablaOfrecimientos), "grow, gaptop 10, wrap");

        // Detalle Evento 
        txtDetalleEvento = new JTextArea();
        txtDetalleEvento.setEditable(false);
        txtDetalleEvento.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtDetalleEvento.setBackground(new Color(245, 245, 245));
        JScrollPane spDetalle = new JScrollPane(txtDetalleEvento);
        spDetalle.setBorder(new TitledBorder(null, "Información del Evento:", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12)));
        frame.getContentPane().add(spDetalle, "grow, height 180!, gaptop 10, wrap");

        // Botones
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        btnAceptar = new JButton("ACEPTAR");
        btnRechazar = new JButton("RECHAZAR");
        btnEliminar = new JButton("ELIMINAR DECISIÓN");
        btnAceptar.setEnabled(false);
        btnRechazar.setEnabled(false);
        btnEliminar.setEnabled(false);
        pnlBtns.add(btnAceptar); pnlBtns.add(btnRechazar); pnlBtns.add(btnEliminar);
        frame.getContentPane().add(pnlBtns, "growx");
    }

    public JFrame getFrame() { return frame; }
    public JComboBox<Object> getCbEmpresas() { return cbEmpresas; }
    public JComboBox<Object> getCbTematicas() { return cbTematicas; }
    public JComboBox<String> getCbFiltroEmbargo() { return cbFiltroEmbargo; }
    public JRadioButton getRdPendientes() { return rdPendientes; }
    public JRadioButton getRdDecididos() { return rdDecididos; }
    public JCheckBox getChkFiltroEmpresa() { return chkFiltroEmpresa; }
    public JTextField getTxtPrecioMin() { return txtPrecioMin; }
    public JTextField getTxtPrecioMax() { return txtPrecioMax; }
    public JTable getTablaOfrecimientos() { return tablaOfrecimientos; }
    public JTextArea getTxtDetalleEvento() { return txtDetalleEvento; }
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnRechazar() { return btnRechazar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}