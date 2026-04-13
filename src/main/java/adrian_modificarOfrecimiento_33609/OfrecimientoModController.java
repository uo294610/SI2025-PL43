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
					int modeloFila = view.getTabEv().convertRowIndexToModel(f);
					EventoModDTO evSeleccionado = eventosActuales.get(modeloFila);
					
					view.getLblTematicaEvento().setText("Temática detectada: " + evSeleccionado.getTematica());
					
					// HU #XXXXX: Bloquear si la asignación no está finalizada
					boolean sinOfrecimiento = view.getCbFiltro().getSelectedIndex() == 1;
					if (sinOfrecimiento && !evSeleccionado.isAsignacionFinalizada()) {
						view.getBtnOfrecer().setEnabled(false);
						view.getBtnOfrecer().setToolTipText("Debe finalizar la asignación de reporteros primero.");
					} else if (sinOfrecimiento && evSeleccionado.isAsignacionFinalizada()) {
						view.getBtnOfrecer().setEnabled(true);
						view.getBtnOfrecer().setToolTipText(null);
					}
					
					cargarEmpresas();
				}
			}
		});
		
		view.getCbFiltro().addActionListener(e -> {
			boolean sinOfrecimiento = view.getCbFiltro().getSelectedIndex() == 1;
			view.getBtnQuitar().setEnabled(!sinOfrecimiento);
			
			// Re-evaluar el bloqueo del botón ofrecer
			int f = view.getTabEv().getSelectedRow();
			if (f >= 0 && sinOfrecimiento) {
				int modeloFila = view.getTabEv().convertRowIndexToModel(f);
				boolean finalizada = eventosActuales.get(modeloFila).isAsignacionFinalizada();
				view.getBtnOfrecer().setEnabled(finalizada);
			} else {
				view.getBtnOfrecer().setEnabled(false);
			}
			
			cargarEmpresas();
		});

		view.getChkFiltrarTematica().addActionListener(e -> cargarEmpresas());
		view.getChkTarifaPlana().addActionListener(e -> cargarEmpresas()); // NUEVO LISTENER
		view.getBtnOfrecer().addActionListener(e -> ejecutarOfrecer());
		view.getBtnQuitar().addActionListener(e -> ejecutarQuitar());
	}

	public void initView() {
		eventosActuales = model.getEventosConReportero();
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
		String idAgencia = evSeleccionado.getAgenciaId();
		
		boolean embargoActivo = "SÍ (ACTIVO)".equals(evSeleccionado.getEmbargo());
		boolean sinOfrecimiento = view.getCbFiltro().getSelectedIndex() == 1;
		boolean filtrarPorTematica = view.getChkFiltrarTematica().isSelected();
		boolean soloTarifaPlana = view.getChkTarifaPlana().isSelected();

		if (sinOfrecimiento) {
			if (filtrarPorTematica) empresasActuales = model.getEmpresasSinOfrecimientoPorTematica(idEv, embargoActivo, idAgencia, soloTarifaPlana);
			else empresasActuales = model.getEmpresasSinOfrecimiento(idEv, embargoActivo, idAgencia, soloTarifaPlana);
		} else {
			if (filtrarPorTematica) empresasActuales = model.getEmpresasConOfrecimientoPorTematica(idEv, embargoActivo, idAgencia, soloTarifaPlana);
			else empresasActuales = model.getEmpresasConOfrecimiento(idEv, embargoActivo, idAgencia, soloTarifaPlana);
		}
		
		// Añadida la columna tarifaPlana
		view.getTabEmp().setModel(SwingUtil.getTableModelFromPojos(empresasActuales, new String[]{"id","nombre","estado","especialidad", "aceptaEmbargos", "tarifaPlana", "estadoPago"}));
	}

	private void ejecutarOfrecer() {
		int fEv = view.getTabEv().getSelectedRow();
		int fEmp = view.getTabEmp().getSelectedRow();
		if (fEv < 0 || fEmp < 0) return;

		EmpresaModDTO empresa = empresasActuales.get(fEmp);

		// NUEVO HU: Validación de morosos
		if ("SÍ".equals(empresa.getTarifaPlana()) && !empresa.isAlCorrientePago()) {
			JOptionPane.showMessageDialog(view.getFrame(), 
				"No se puede ofrecer reportajes a " + empresa.getNombre() + ".\nMotivo: Tiene una Tarifa Plana contratada pero NO está al corriente de pago.", 
				"Bloqueo por Impago", JOptionPane.ERROR_MESSAGE);
			return; // Cancelamos el proceso
		}

		model.insertarOfrecimiento(view.getTabEv().getValueAt(fEv, 0).toString(), empresa.getId());
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