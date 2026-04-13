package adrian_distribuirReportajes_33606;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class DistribucionView {
	private JFrame frame;
	private JTable tabEv, tabEmp;
	private JButton btnDarAcceso, btnQuitarAcceso;
	private JComboBox<String> cbFiltroAcceso, cbFiltroEmbargo;
	private JLabel lblEmpresasAcceso;
	private JRadioButton rbAccesoCompleto, rbAccesoParcial;
	private ButtonGroup bgAcceso;

	public DistribucionView() {
		frame = new JFrame("Distribuir Reportajes a Empresas (S3 - #34304 / #34308)");
		frame.setBounds(100, 100, 950, 700);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow][][grow][][][]"));

		// SECCIÓN 1: EVENTOS Y FILTRO EMBARGO
		// Le decimos a MigLayout que la columna del medio crezca [grow] para empujar el resto a la derecha
		JPanel pnlTop = new JPanel(new MigLayout("", "[][grow][]", "[]"));
		pnlTop.add(new JLabel("Distribución con Reportaje Entregado:"));
		pnlTop.add(new JLabel("Filtro Embargo:"), "align right"); 
		cbFiltroEmbargo = new JComboBox<>(new String[]{"Todos", "Solo Embargo Activo", "Sin Embargo / Caducado"});
		pnlTop.add(cbFiltroEmbargo, "wrap");
		frame.getContentPane().add(pnlTop, "growx, wrap");

		tabEv = new JTable();
		frame.getContentPane().add(new JScrollPane(tabEv), "grow, wrap");

		// SECCIÓN 2: EMPRESAS Y ACCESOS
		frame.getContentPane().add(new JLabel("Filtro Empresas:"), "split 2");
		cbFiltroAcceso = new JComboBox<>(new String[]{"Sin Acceso (Para Dar)", "Con Acceso (Para Quitar)"});
		frame.getContentPane().add(cbFiltroAcceso, "wrap");

		tabEmp = new JTable();
		tabEmp.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		frame.getContentPane().add(new JScrollPane(tabEmp), "grow, wrap");

		// SECCIÓN 3: RADIO BUTTONS TIPO DE ACCESO
		JPanel pnlRadios = new JPanel(new MigLayout("", "[grow]", "[][]"));
		pnlRadios.add(new JLabel("Tipo de acceso a conceder (Solo disponible si hay embargo):"), "wrap");
		rbAccesoCompleto = new JRadioButton("Acceso Completo (Multimedia habilitado)", true);
		rbAccesoParcial = new JRadioButton("Acceso Parcial (Solo texto, sin descargas)");
		rbAccesoParcial.setEnabled(false); // Por defecto deshabilitado hasta seleccionar embargo
		bgAcceso = new ButtonGroup();
		bgAcceso.add(rbAccesoCompleto); bgAcceso.add(rbAccesoParcial);
		pnlRadios.add(rbAccesoCompleto, "split 2");
		pnlRadios.add(rbAccesoParcial, "wrap");
		frame.getContentPane().add(pnlRadios, "growx, wrap");

		// SECCIÓN 4: BOTONES
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
	public JComboBox<String> getCbFiltroAcceso() { return cbFiltroAcceso; }
	public JComboBox<String> getCbFiltroEmbargo() { return cbFiltroEmbargo; }
	public JRadioButton getRbAccesoCompleto() { return rbAccesoCompleto; }
	public JRadioButton getRbAccesoParcial() { return rbAccesoParcial; }
	public JButton getBtnDar() { return btnDarAcceso; }
	public JButton getBtnQuitar() { return btnQuitarAcceso; }
	public void setTextoAcceso(String t) { lblEmpresasAcceso.setText("Empresas con Acceso Actual: " + t); }
}