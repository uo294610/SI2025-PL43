package alex_InformeAgenciaTematica;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.*;

public class InformeAgenciaView {
    private JFrame frame;
    private JComboBox<Object> cbAgencias;
    private JComboBox<Object> cbTematicas;
    private JTextArea txtInforme;

    public InformeAgenciaView() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Informe Económico por Temática (Agencia)");
        frame.setBounds(100, 100, 950, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(new MigLayout("ins 20, fill", "[grow]", "[][grow]"));

        // Controles de selección
        JPanel pnlCabecera = new JPanel(new MigLayout("ins 0", "[][grow][][grow]", "[]"));
        pnlCabecera.add(new JLabel("Agencia actual:"));
        cbAgencias = new JComboBox<>();
        pnlCabecera.add(cbAgencias, "wmin 250");
        
        pnlCabecera.add(new JLabel("Seleccione Temática:"), "gapleft 40");
        cbTematicas = new JComboBox<>();
        pnlCabecera.add(cbTematicas, "wmin 250");
        
        frame.getContentPane().add(pnlCabecera, "growx, wrap");

        // Visor del Informe
        txtInforme = new JTextArea();
        txtInforme.setEditable(false);
        txtInforme.setFont(new Font("Monospaced", Font.PLAIN, 14));
        txtInforme.setBackground(new Color(245, 245, 245));
        
        JScrollPane spInforme = new JScrollPane(txtInforme);
        spInforme.setBorder(new TitledBorder(null, "INFORME POR PANTALLA", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.BOLD, 12)));
        frame.getContentPane().add(spInforme, "grow, gaptop 15");
    }

    public JFrame getFrame() { return frame; }
    public JComboBox<Object> getCbAgencias() { return cbAgencias; }
    public JComboBox<Object> getCbTematicas() { return cbTematicas; }
    public JTextArea getTxtInforme() { return txtInforme; }
}