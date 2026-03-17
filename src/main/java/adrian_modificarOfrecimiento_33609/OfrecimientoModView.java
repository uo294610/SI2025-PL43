package adrian_modificarOfrecimiento_33609;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class OfrecimientoModView {
	private JFrame frame;
	private JTable tabEv, tabEmp;
	private JComboBox<String> cbFiltro;
	private JButton btnQuitar, btnOfrecer;
	private JTextField txtBuscar;
	private JCheckBox chkFiltrarTematica; // NUEVO
	private JLabel lblTematicaEvento;     // NUEVO

	public OfrecimientoModView() {
		frame = new JFrame("Gestión de Ofrecimientos (#33604 / #34110)");
		frame.setBounds(100, 100, 950, 650);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][][grow][]"));

		frame.getContentPane().add(new JLabel("1. Selecciona un Evento:"), "split 3");
		frame.getContentPane().add(new JLabel("Buscar:"), "gapleft 20");
		txtBuscar = new JTextField(20);
		frame.getContentPane().add(txtBuscar, "wrap");

		tabEv = new JTable();
		frame.getContentPane().add(new JScrollPane(tabEv), "grow, wrap");

		// ZONA DE FILTROS CENTRAL
		JPanel pnlFiltros = new JPanel(new MigLayout("", "[][grow][]", "[]"));
		pnlFiltros.add(new JLabel("Filtro estado:"));
		cbFiltro = new JComboBox<>(new String[]{"Con Ofrecimiento", "Sin Ofrecimiento"});
		pnlFiltros.add(cbFiltro);
		
		lblTematicaEvento = new JLabel("Temática detectada: ---");
		pnlFiltros.add(lblTematicaEvento, "gapleft 30");
		
		chkFiltrarTematica = new JCheckBox("Filtrar solo empresas de esta temática");
		pnlFiltros.add(chkFiltrarTematica, "gapleft 10");
		frame.getContentPane().add(pnlFiltros, "growx, wrap");

		frame.getContentPane().add(new JLabel("2. Listado de Empresas:"), "wrap");
		tabEmp = new JTable();
		frame.getContentPane().add(new JScrollPane(tabEmp), "grow, wrap");

		btnQuitar = new JButton("Quitar Ofrecimiento Seleccionado");
		btnOfrecer = new JButton("Ofrecer Reportaje");
		
		frame.getContentPane().add(btnQuitar, "split 2, align center");
		frame.getContentPane().add(btnOfrecer, "align center");
	}

	public JFrame getFrame() { return frame; }
	public JTable getTabEv() { return tabEv; }
	public JTable getTabEmp() { return tabEmp; }
	public JComboBox<String> getCbFiltro() { return cbFiltro; }
	public JButton getBtnQuitar() { return btnQuitar; }
	public JButton getBtnOfrecer() { return btnOfrecer; }
	public JTextField getTxtBuscar() { return txtBuscar; }
	public JCheckBox getChkFiltrarTematica() { return chkFiltrarTematica; }
	public JLabel getLblTematicaEvento() { return lblTematicaEvento; }
}