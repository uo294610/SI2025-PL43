package adrian_modificarOfrecimiento_33609;

import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import giis.demo.util.SwingUtil;

public class OfrecimientoModController {
	private OfrecimientoModModel model;
	private OfrecimientoModView view;
	private List<EmpresaModDTO> empresasActuales;
	private List<EventoModDTO> eventosActuales;

	public OfrecimientoModController(OfrecimientoModModel m, OfrecimientoModView v) {
		this.model = m; this.view = v;
	}

	public void initController() {
		view.getTxtBuscar().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String texto = view.getTxtBuscar().getText();
				DefaultTableModel tableModel = (DefaultTableModel) view.getTabEv().getModel();
				TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
				view.getTabEv().setRowSorter(sorter);
				sorter.setRowFilter(RowFilter.regexFilter("(?i)" + texto));
			}
		});

		view.getTabEv().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int f = view.getTabEv().getSelectedRow();
				if (f >= 0) {
					String nombreTematica = view.getTabEv().getValueAt(f, 4).toString(); 
					view.getLblTematicaEvento().setText("Temática detectada: " + nombreTematica);
					cargarEmpresas();
				}
			}
		});
		
		view.getCbFiltro().addActionListener(e -> {
			boolean sinOfrecimiento = view.getCbFiltro().getSelectedIndex() == 1;
			view.getBtnOfrecer().setEnabled(sinOfrecimiento);
			view.getBtnQuitar().setEnabled(!sinOfrecimiento);
			cargarEmpresas();
		});

		view.getChkFiltrarTematica().addActionListener(e -> cargarEmpresas());
		view.getBtnOfrecer().addActionListener(e -> ejecutarOfrecer());
		view.getBtnQuitar().addActionListener(e -> ejecutarQuitar());
	}

	public void initView() {
		eventosActuales = model.getEventosConReportero();
		// Añadimos la columna embargo S3
		view.getTabEv().setModel(SwingUtil.getTableModelFromPojos(eventosActuales, new String[]{"id","nombre","fecha","reportero", "tematica", "embargo"}));
		
		view.getCbFiltro().setSelectedIndex(0);
		view.getBtnOfrecer().setEnabled(false);
		view.getFrame().setVisible(true);
	}

	private void cargarEmpresas() {
		int vistaFila = view.getTabEv().getSelectedRow();
		if (vistaFila < 0) return;

		int modeloFila = view.getTabEv().convertRowIndexToModel(vistaFila);
		EventoModDTO evSeleccionado = eventosActuales.get(modeloFila);
		String idEv = evSeleccionado.getId();
		
		// HU #34307: Comprobamos si el evento seleccionado tiene embargo activo
		boolean embargoActivo = "SÍ (ACTIVO)".equals(evSeleccionado.getEmbargo());
		
		boolean sinOfrecimiento = view.getCbFiltro().getSelectedIndex() == 1;
		boolean filtrarPorTematica = view.getChkFiltrarTematica().isSelected();

		if (sinOfrecimiento) {
			if (filtrarPorTematica) empresasActuales = model.getEmpresasSinOfrecimientoPorTematica(idEv, embargoActivo);
			else empresasActuales = model.getEmpresasSinOfrecimiento(idEv, embargoActivo);
		} else {
			if (filtrarPorTematica) empresasActuales = model.getEmpresasConOfrecimientoPorTematica(idEv, embargoActivo);
			else empresasActuales = model.getEmpresasConOfrecimiento(idEv, embargoActivo);
		}
		
		// Añadimos la columna aceptaEmbargos S3
		view.getTabEmp().setModel(SwingUtil.getTableModelFromPojos(empresasActuales, new String[]{"id","nombre","estado","especialidad", "aceptaEmbargos"}));
	}

	private void ejecutarOfrecer() {
		int fEv = view.getTabEv().getSelectedRow();
		int fEmp = view.getTabEmp().getSelectedRow();
		if (fEv < 0 || fEmp < 0) return;

		model.insertarOfrecimiento(view.getTabEv().getValueAt(fEv, 0).toString(), empresasActuales.get(fEmp).getId());
		JOptionPane.showMessageDialog(null, "Ofrecimiento enviado con éxito.");
		cargarEmpresas();
	}

	private void ejecutarQuitar() {
		int fEv = view.getTabEv().getSelectedRow();
		int fEmp = view.getTabEmp().getSelectedRow();
		if (fEv < 0 || fEmp < 0) return;

		EmpresaModDTO empresa = empresasActuales.get(fEmp);

		if (empresa.getAcceso() == 1) { 
			JOptionPane.showMessageDialog(null, "Acción denegada: Ya tiene acceso.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (empresa.getEstado().toUpperCase().contains("ACEPTADO")) {
			JOptionPane.showMessageDialog(null, "Se enviará un email automático a " + empresa.getNombre() + " notificando la baja.");
		}

		model.eliminarOfrecimiento(view.getTabEv().getValueAt(fEv, 0).toString(), empresa.getId());
		JOptionPane.showMessageDialog(null, "Ofrecimiento eliminado.");
		cargarEmpresas();
	}
}