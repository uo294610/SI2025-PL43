package adrian_modificarOfrecimiento_33609;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import net.miginfocom.swing.MigLayout;

public class OfrecimientoModView {
	private JFrame frame;
	private JTable tabEv, tabEmp;
	private JComboBox<String> cbFiltro;
	private JButton btnQuitar, btnOfrecer;
	private JTextField txtBuscar;

	public OfrecimientoModView() {
		frame = new JFrame("Gestión de Ofrecimientos (#33604 / #33609)");
		frame.setBounds(100, 100, 900, 650);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][][grow][]"));

		frame.getContentPane().add(new JLabel("1. Selecciona un Evento:"), "split 3");
		frame.getContentPane().add(new JLabel("Buscar:"), "gapleft 20");
		txtBuscar = new JTextField(20);
		frame.getContentPane().add(txtBuscar, "wrap");

		tabEv = new JTable();
		frame.getContentPane().add(new JScrollPane(tabEv), "grow, wrap");

		frame.getContentPane().add(new JLabel("Filtrar empresas:"), "split 2");
		cbFiltro = new JComboBox<>(new String[]{"Con Ofrecimiento (Para Quitar)", "Sin Ofrecimiento (Para Ofrecer)"});
		frame.getContentPane().add(cbFiltro, "wrap");

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
}