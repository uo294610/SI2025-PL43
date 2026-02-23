package alex_ModificarOfrecimientos_33611;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class OfrecimientosView {
    private JFrame frame;
    private JComboBox<Object> cbEmpresas;
    private JTable tabOfrecimientos, tabDetalle;
    private JButton btnAceptar, btnRechazar, btnEliminar;
    private JRadioButton rdPendientes, rdDecididos;
    private JLabel lblMensaje;

    public OfrecimientosView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Modificar Decisión de Ofrecimiento");
        frame.setBounds(100, 100, 800, 650);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][grow][][]"));

        JPanel pnlFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlFiltros.add(new JLabel("Empresa:"));
        cbEmpresas = new JComboBox<>();
        pnlFiltros.add(cbEmpresas);
        
        rdPendientes = new JRadioButton("Pendientes", true);
        rdDecididos = new JRadioButton("Decididos");
        ButtonGroup group = new ButtonGroup();
        group.add(rdPendientes); group.add(rdDecididos);
        pnlFiltros.add(rdPendientes); pnlFiltros.add(rdDecididos);
        frame.getContentPane().add(pnlFiltros, "wrap");

        tabOfrecimientos = new JTable() { public boolean isCellEditable(int r, int c) { return false; } };
        frame.getContentPane().add(new JScrollPane(tabOfrecimientos), "grow, wrap");

        frame.getContentPane().add(new JLabel("Información del Evento:"), "gaptop 10, wrap");
        tabDetalle = new JTable() { public boolean isCellEditable(int r, int c) { return false; } };
        tabDetalle.setRowHeight(25);
        JScrollPane spDetalle = new JScrollPane(tabDetalle);
        spDetalle.setPreferredSize(new Dimension(300, 150));
        frame.getContentPane().add(spDetalle, "grow, wrap");

        JPanel pnlBtns = new JPanel();
        btnAceptar = new JButton("ACEPTAR");
        btnRechazar = new JButton("RECHAZAR");
        btnEliminar = new JButton("ELIMINAR DECISIÓN");
        pnlBtns.add(btnAceptar); pnlBtns.add(btnRechazar); pnlBtns.add(btnEliminar);
        frame.getContentPane().add(pnlBtns, "center, wrap");

        lblMensaje = new JLabel(" ");
        lblMensaje.setForeground(Color.RED);
        lblMensaje.setFont(new Font("Tahoma", Font.BOLD, 12));
        frame.getContentPane().add(lblMensaje, "growx");
    }

    public JFrame getFrame() { return frame; }
    public JComboBox<Object> getCbEmpresas() { return cbEmpresas; }
    public JTable getTablaOfrecimientos() { return tabOfrecimientos; }
    public JTable getDetalleEvento() { return tabDetalle; }
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnRechazar() { return btnRechazar; }
    public JButton getBtnEliminar() { return btnEliminar; }
    public JRadioButton getRdPendientes() { return rdPendientes; }
    public JRadioButton getRdDecididos() { return rdDecididos; }
    public void setMensajeDecision(String msg) { lblMensaje.setText(msg); }
}