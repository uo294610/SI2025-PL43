package adrian_ofrecerReportajes_33604;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class OfrecimientoView {
    private JFrame frame;
    private JTable tabEv, tabEmp;
    private JButton btnOfrecer;
    private JLabel lblOfrecimientosEnCurso;

    public OfrecimientoView() {
        frame = new JFrame("Ofrecer Reportajes a Empresas (#33604)");
        frame.setBounds(100, 100, 700, 550);
        frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow][][grow][][]"));

        frame.getContentPane().add(new JLabel("1. Eventos con Reportero Asignado (Selecciona uno):"), "wrap");
        tabEv = new JTable();
        frame.getContentPane().add(new JScrollPane(tabEv), "grow, wrap");

        frame.getContentPane().add(new JLabel("2. Empresas de comunicación (Sin ofrececimiento):"), "wrap");
        tabEmp = new JTable();
        tabEmp.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        frame.getContentPane().add(new JScrollPane(tabEmp), "grow, wrap");

        btnOfrecer = new JButton("Realizar Ofrecimiento");
        frame.getContentPane().add(btnOfrecer, "align center, wrap");

        // Esta es la lista que pedías abajo del todo
        lblOfrecimientosEnCurso = new JLabel("Ofrecimientos en curso: ---");
        frame.getContentPane().add(lblOfrecimientosEnCurso, "growx");
    }

    public JFrame getFrame() { return frame; }
    public JTable getTabEv() { return tabEv; }
    public JTable getTabEmp() { return tabEmp; }
    public JButton getBtn() { return btnOfrecer; }
    public void setTextoEstado(String t) { lblOfrecimientosEnCurso.setText("Ofrecimientos en curso: " + t); }
}