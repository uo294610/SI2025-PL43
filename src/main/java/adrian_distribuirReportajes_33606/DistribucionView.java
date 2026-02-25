package adrian_distribuirReportajes_33606;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class DistribucionView {
	private JFrame frame;
	private JTable tabEv, tabEmp;
	private JButton btnDarAcceso;
	private JLabel lblEmpresasAcceso;

	public DistribucionView() {
		frame = new JFrame("Distribuir Reportajes a Empresas (#33606)");
		frame.setBounds(100, 100, 750, 600);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow][][grow][][]"));

		frame.getContentPane().add(new JLabel("Distribución con Reportaje Entregado:"), "wrap");
		tabEv = new JTable();
		frame.getContentPane().add(new JScrollPane(tabEv), "grow, wrap");

		frame.getContentPane().add(new JLabel("Empresas con Ofrecimiento Aceptado (sin acceso):"), "wrap");
		tabEmp = new JTable();
		tabEmp.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		frame.getContentPane().add(new JScrollPane(tabEmp), "grow, wrap");

		btnDarAcceso = new JButton("Dar acceso al reportaje");
		frame.getContentPane().add(btnDarAcceso, "align center, wrap");

		lblEmpresasAcceso = new JLabel("Empresas con Acceso Actual: ---");
		frame.getContentPane().add(lblEmpresasAcceso, "growx");
	}

	public JFrame getFrame() { return frame; }
	public JTable getTabEv() { return tabEv; }
	public JTable getTabEmp() { return tabEmp; }
	public JButton getBtn() { return btnDarAcceso; }
	public void setTextoAcceso(String t) { lblEmpresasAcceso.setText("Empresas con Acceso Actual: " + t); }
}