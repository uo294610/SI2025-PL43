package alex_GestionarOfrecimientos_33605;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;

public class OfrecimientosView {
    private JFrame frame;
    private JComboBox<Object> cbEmpresas;
    private JTable tabOfrecimientos;
    private JTable tabDetalle;
    private JButton btnAceptar;
    private JButton btnRechazar;
    private JLabel lblMensaje;

    public OfrecimientosView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Gestión de Ofrecimientos");
        frame.setBounds(100, 100, 750, 650); 
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][grow][][]"));

        frame.getContentPane().add(new JLabel("Empresa:"), "split 2");
        cbEmpresas = new JComboBox<>();
        frame.getContentPane().add(cbEmpresas, "growx, wrap");

        frame.getContentPane().add(new JLabel("Ofrecimientos Pendientes:"), "wrap, gaptop 5");
        tabOfrecimientos = new JTable() {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabOfrecimientos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        frame.getContentPane().add(new JScrollPane(tabOfrecimientos), "grow, wrap");

        frame.getContentPane().add(new JLabel("Información Detallada del Evento:"), "wrap, gaptop 10");
        tabDetalle = new JTable() {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabDetalle.setRowHeight(25);
        
        JScrollPane scrollDetalle = new JScrollPane(tabDetalle);
        scrollDetalle.setPreferredSize(new Dimension(300, 200)); // Panel de detalle amplio
        frame.getContentPane().add(scrollDetalle, "grow, wrap");

        btnAceptar = new JButton("ACEPTAR OFRECIMIENTO");
        btnAceptar.setBackground(new Color(200, 255, 200)); 
        btnRechazar = new JButton("RECHAZAR OFRECIMIENTO");
        btnRechazar.setBackground(new Color(255, 200, 200));
        
        btnAceptar.setEnabled(false);
        btnRechazar.setEnabled(false);
        
        frame.getContentPane().add(btnAceptar, "split 2, alignx center, gaptop 10, height 40!");
        frame.getContentPane().add(btnRechazar, "height 40!, wrap");

        lblMensaje = new JLabel(" ");
        lblMensaje.setFont(new Font("Tahoma", Font.BOLD, 13)); 
        lblMensaje.setForeground(new Color(0, 102, 204)); 
        frame.getContentPane().add(lblMensaje, "growx, gaptop 10");
    }

    public JFrame getFrame() { return frame; }
    public JComboBox<Object> getCbEmpresas() { return cbEmpresas; }
    public JTable getTablaOfrecimientos() { return tabOfrecimientos; }
    public JTable getDetalleEvento() { return tabDetalle; }
    public JButton getBtnAceptar() { return btnAceptar; }
    public JButton getBtnRechazar() { return btnRechazar; }
    public void setMensajeDecision(String msg) { lblMensaje.setText(msg); }
}