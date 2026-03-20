package adrian_interesFreelance_34116;

import java.awt.event.*;
import javax.swing.JOptionPane;
import giis.demo.util.SwingUtil;

public class FreelanceController {
	private FreelanceModel model;
	private FreelanceView view;
	
	// Usamos a Carlos Sainz (ID 14, Freelance de Deportes) para la demo
	private final int ID_FREELANCE_ACTUAL = 14; 

	public FreelanceController(FreelanceModel m, FreelanceView v) {
		this.model = m; this.view = v;
	}

	public void initController() {
		view.getBtnGuardar().addActionListener(e -> ejecutarGuardar());

		view.getTabEventos().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int f = view.getTabEventos().getSelectedRow();
				if (f >= 0) {
					String estadoActual = view.getTabEventos().getValueAt(f, 4).toString();
					switch (estadoActual) {
						case "Interesado": view.getRbInteresado().setSelected(true); break;
						case "No interesado": view.getRbNoInteresado().setSelected(true); break;
						case "En duda": view.getRbEnDuda().setSelected(true); break;
						default: view.getBgInteres().clearSelection(); break;
					}
				}
			}
		});
	}

	public void initView() {
		var info = model.getInfoFreelance(ID_FREELANCE_ACTUAL);
		if (!info.isEmpty()) {
			String nombre = info.get(0)[0].toString();
			String especialidades = info.get(0)[1].toString();
			view.getLblCabecera().setText("Hola, " + nombre + " - Tus especialidades detectadas: [" + especialidades + "]");
		}

		cargarTabla();
		view.getFrame().setVisible(true);
	}

	private void cargarTabla() {
		var eventos = model.getEventosDisponibles(ID_FREELANCE_ACTUAL);
		view.getTabEventos().setModel(SwingUtil.getTableModelFromPojos(eventos, 
				new String[]{"id", "nombreEvento", "fecha", "tematica", "Interes"}));
	}

	private void ejecutarGuardar() {
		int f = view.getTabEventos().getSelectedRow();
		if (f < 0) {
			JOptionPane.showMessageDialog(null, "Selecciona un evento de la lista primero.");
			return;
		}

		String idEv = view.getTabEventos().getValueAt(f, 0).toString();
		String estadoElegido = "";
		
		if (view.getRbInteresado().isSelected()) estadoElegido = "Interesado";
		else if (view.getRbNoInteresado().isSelected()) estadoElegido = "No interesado";
		else if (view.getRbEnDuda().isSelected()) estadoElegido = "En duda";
		else {
			JOptionPane.showMessageDialog(null, "Por favor, marca una de las 3 opciones.");
			return;
		}

		model.guardarInteres(ID_FREELANCE_ACTUAL, idEv, estadoElegido);
		JOptionPane.showMessageDialog(null, "Estado guardado correctamente en la Base de Datos.");
		cargarTabla(); 
	}
}