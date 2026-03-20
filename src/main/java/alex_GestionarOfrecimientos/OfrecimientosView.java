package alex_GestionarOfrecimientos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class OfrecimientosView {
    private JFrame frame;
    private JComboBox<Object> cbEmpresas, cbTematicas;
    private JRadioButton rdPendientes, rdDecididos;
    private JCheckBox chkFiltroEmpresa;
    private JTextField txtPrecioMin, txtPrecioMax;
    private JTable tablaOfrecimientos, detalleEvento;
    private JButton btnAceptar, btnRechazar, btnEliminar;
    private JLabel lblMensajeDecision;

    public OfrecimientosView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Modificar Decisión de Ofrecimiento");
        frame.setBounds(100, 100, 950, 680);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("ins 20, fillx", "[grow]", "[][][][grow][][][]"));

        // Empresa y Estado
        JPanel pnl1 = new JPanel(new MigLayout("ins 0", "[][grow][][]", "[]"));
        pnl1.add(new JLabel("Empresa:"));
        cbEmpresas = new JComboBox<>();
        pnl1.add(cbEmpresas, "growx, wmin 250");
        rdPendientes = new JRadioButton("Pendientes", true);
        rdDecididos = new JRadioButton("Decididos");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdPendientes); bg.add(rdDecididos);
        pnl1.add(rdPendientes, "gapleft 20");
        pnl1.add(rdDecididos);
        frame.getContentPane().add(pnl1, "growx, wrap");

        // Temáticas
        JPanel pnl2 = new JPanel(new MigLayout("ins 0", "[][][]", "[]"));
        chkFiltroEmpresa = new JCheckBox("Solo temáticas de la empresa", true);
        pnl2.add(chkFiltroEmpresa);
        pnl2.add(new JLabel("Temática:"), "gapleft 40");
        cbTematicas = new JComboBox<>();
        pnl2.add(cbTematicas, "wmin 250");
        frame.getContentPane().add(pnl2, "growx, gaptop 10, wrap");

        // Filtro precios
        JPanel pnl3 = new JPanel(new MigLayout("ins 0", "[][][][][]", "[]"));
        pnl3.add(new JLabel("Precio Mínimo:"));
        txtPrecioMin = new JTextField(6);
        pnl3.add(txtPrecioMin);
        pnl3.add(new JLabel("€"), "gapright 20");
        
        pnl3.add(new JLabel("Precio Máximo:"));
        txtPrecioMax = new JTextField(6);
        pnl3.add(txtPrecioMax);
        pnl3.add(new JLabel("€"));
        frame.getContentPane().add(pnl3, "growx, gaptop 5, wrap");

        // Tabla Principal
        tablaOfrecimientos = new JTable();
        tablaOfrecimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        frame.getContentPane().add(new JScrollPane(tablaOfrecimientos), "grow, gaptop 10, wrap");

        // Detalle Evento
        detalleEvento = new JTable();
        detalleEvento.setEnabled(false);
        detalleEvento.setBackground(SystemColor.control); 
        JScrollPane spDetalle = new JScrollPane(detalleEvento);
        spDetalle.setBorder(new TitledBorder("Información del Evento"));
        frame.getContentPane().add(spDetalle, "growx, height 100!, gaptop 10, wrap");

        // Botones
        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        btnAceptar = new JButton("ACEPTAR");
        btnRechazar = new JButton("RECHAZAR");
        btnEliminar = new JButton("ELIMINAR DECISIÓN");
        btnAceptar.setEnabled(false);
        btnRechazar.setEnabled(false);
        btnEliminar.setEnabled(false);
        pnlBtns.add(btnAceptar); pnlBtns.add(btnRechazar); pnlBtns.add(btnEliminar);
        frame.getContentPane().add(pnlBtns, "growx, wrap");

        // Mensaje
        lblMensajeDecision = new JLabel("Seleccione un ofrecimiento para ver si es editable.");
        lblMensajeDecision.setFont(new Font("Tahoma", Font.BOLD, 12));
        lblMensajeDecision.setHorizontalAlignment(SwingConstants.CENTER);
        frame.getContentPane().add(lblMensajeDecision, "growx, gaptop 5");
    }

    public void setMensajeDecision(String mensaje, boolean bloqueado) {
        lblMensajeDecision.setText(mensaje);
        lblMensajeDecision.setForeground(bloqueado ? Color.RED : Color.BLUE);
    }

    public JFrame getFrame() { return frame; }
    public JComboBox<Object> getCbEmpresas() { return cbEmpresas; }
    public JComboBox<Object> getCbTematicas() { return cbTematicas; }
    public JRadioButton getRdPendientes() { return rdPendientes; }
    public JRadioButton getRdDecididos() { return rdDecididos; }
    public JCheckBox getChkFiltroEmpresa() { return chkFiltroEmpresa; }
    public JTextField getTxtPrecioMin() { return txtPrecioMin; }
    public JTextField getTxtPrecioMax() { return txtPrecioMax; }
    public JTable getTablaOfrecimientos() { return tablaOfrecimientos; }
    public JTable getDetalleEvento() { return detalleEvento; }
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnRechazar() { return btnRechazar; }
    public JButton getBtnEliminar() { return btnEliminar; }
}