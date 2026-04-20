package adrian_interesFreelance_34116;

import javax.swing.*;
import net.miginfocom.swing.MigLayout;

public class FreelanceView {
	private JFrame frame;
	private JLabel lblCabecera;
	private JTable tabEventos;
	private JRadioButton rbInteresado, rbNoInteresado, rbEnDuda;
	private ButtonGroup bgInteres;
	private JButton btnGuardar;
	private JComboBox<Object> cbFreelances;

	public FreelanceView() {
		frame = new JFrame("Bolsa de Eventos Freelance (#34116)");
		frame.setBounds(100, 100, 800, 500);
		frame.getContentPane().setLayout(new MigLayout("", "[grow]", "[][][grow][][]"));
		
		JPanel pnlLogin = new JPanel(new MigLayout("ins 0", "[][grow]", "[]"));
		pnlLogin.add(new JLabel("Seleccionar Reportero:"));
		cbFreelances = new JComboBox<>();
		pnlLogin.add(cbFreelances, "growx");
		frame.getContentPane().add(pnlLogin, "wrap, growx, gapbottom 10");
		
		lblCabecera = new JLabel("Cargando perfil...");
		frame.getContentPane().add(lblCabecera, "wrap, gapbottom 10");

		frame.getContentPane().add(new JLabel("1. Eventos disponibles que coinciden con tus temáticas:"), "wrap");

		tabEventos = new JTable();
		tabEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		frame.getContentPane().add(new JScrollPane(tabEventos), "grow, wrap");

		frame.getContentPane().add(new JLabel("2. Indicar estado para el evento seleccionado:"), "wrap, gaptop 10");

		JPanel pnlRadios = new JPanel(new MigLayout("", "[][][]", "[]"));
		rbInteresado = new JRadioButton("Interesado");
		rbNoInteresado = new JRadioButton("No interesado");
		rbEnDuda = new JRadioButton("En duda");
		
		bgInteres = new ButtonGroup();
		bgInteres.add(rbInteresado);
		bgInteres.add(rbNoInteresado);
		bgInteres.add(rbEnDuda);
		
		pnlRadios.add(rbInteresado);
		pnlRadios.add(rbNoInteresado);
		pnlRadios.add(rbEnDuda);
		frame.getContentPane().add(pnlRadios, "wrap");

		btnGuardar = new JButton("Guardar estado de interés");
		frame.getContentPane().add(btnGuardar, "align center, gaptop 10");
	}

	public JFrame getFrame() { return frame; }
	public JLabel getLblCabecera() { return lblCabecera; }
	public JTable getTabEventos() { return tabEventos; }
	public JRadioButton getRbInteresado() { return rbInteresado; }
	public JRadioButton getRbNoInteresado() { return rbNoInteresado; }
	public JRadioButton getRbEnDuda() { return rbEnDuda; }
	public JButton getBtnGuardar() { return btnGuardar; }
	public ButtonGroup getBgInteres() { return bgInteres; }
	public JComboBox<Object> getCbFreelances() { return cbFreelances; }
}