package adrian_distribuirReportajes_33606;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class DistribucionView {
	private JFrame frame;
	private JTable tabEv, tabEmp;
	private JButton btnDarAcceso, btnQuitarAcceso;
	private JComboBox<String> cbFiltroAcceso;
	private JLabel lblEmpresasAcceso;

	public DistribucionView() {
		frame = new JFrame("Distribuir Reportajes a Empresas (#34114)");
		frame.setBounds(100, 100, 850, 650);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow][][grow][][]"));

		frame.getContentPane().add(new JLabel("Distribución con Reportaje Entregado:"), "wrap");
		tabEv = new JTable();
		frame.getContentPane().add(new JScrollPane(tabEv), "grow, wrap");

		frame.getContentPane().add(new JLabel("Filtro Empresas:"), "split 2");
		cbFiltroAcceso = new JComboBox<>(new String[]{"Sin Acceso (Para Dar)", "Con Acceso (Para Quitar)"});
		frame.getContentPane().add(cbFiltroAcceso, "wrap");

		tabEmp = new JTable();
		tabEmp.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		frame.getContentPane().add(new JScrollPane(tabEmp), "grow, wrap");

		btnQuitarAcceso = new JButton("Quitar acceso al reportaje");
		btnDarAcceso = new JButton("Dar acceso al reportaje");
		
		frame.getContentPane().add(btnQuitarAcceso, "split 2, align center");
		frame.getContentPane().add(btnDarAcceso, "align center, wrap");

		lblEmpresasAcceso = new JLabel("Empresas con Acceso Actual: ---");
		frame.getContentPane().add(lblEmpresasAcceso, "growx");
	}

	public JFrame getFrame() { return frame; }
	public JTable getTabEv() { return tabEv; }
	public JTable getTabEmp() { return tabEmp; }
	public JComboBox<String> getCbFiltro() { return cbFiltroAcceso; }
	public JButton getBtnDar() { return btnDarAcceso; }
	public JButton getBtnQuitar() { return btnQuitarAcceso; }
	public void setTextoAcceso(String t) { lblEmpresasAcceso.setText("Empresas con Acceso Actual: " + t); }
}