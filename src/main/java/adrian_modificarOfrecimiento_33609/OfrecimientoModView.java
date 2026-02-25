package adrian_modificarOfrecimiento_33609;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class OfrecimientoModView {
	private JFrame frame;
	private JTable tabEv, tabEmp;
	private JComboBox<String> cbFiltro;
	private JButton btnQuitar;

	public OfrecimientoModView() {
		frame = new JFrame("Modificar Ofrecimientos (#33609)");
		frame.setBounds(100, 100, 800, 600);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][grow][][][grow][]"));

		frame.getContentPane().add(new JLabel("1. Selecciona un Evento:"), "wrap");
		tabEv = new JTable();
		frame.getContentPane().add(new JScrollPane(tabEv), "grow, wrap");

		frame.getContentPane().add(new JLabel("Filtrar empresas:"), "split 2");
		// Filtro corregido según tus instrucciones
		cbFiltro = new JComboBox<>(new String[]{"Con Ofrecimiento", "Sin Ofrecimiento"});
		frame.getContentPane().add(cbFiltro, "wrap");

		frame.getContentPane().add(new JLabel("2. Listado de Empresas:"), "wrap");
		tabEmp = new JTable();
		frame.getContentPane().add(new JScrollPane(tabEmp), "grow, wrap");

		btnQuitar = new JButton("Quitar Ofrecimiento Seleccionado");
		frame.getContentPane().add(btnQuitar, "align center");
	}

	public JFrame getFrame() { return frame; }
	public JTable getTabEv() { return tabEv; }
	public JTable getTabEmp() { return tabEmp; }
	public JComboBox<String> getCbFiltro() { return cbFiltro; }
	public JButton getBtnQuitar() { return btnQuitar; }
}