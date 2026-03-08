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

	public OfrecimientoModController(OfrecimientoModModel m, OfrecimientoModView v) {
		this.model = m; this.view = v;
	}

	public void initController() {
		// Buscador de eventos
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

		view.getTabEv().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) { cargarEmpresas(); }
		});
		
		view.getCbFiltro().addActionListener(e -> {
			boolean sinOfrecimiento = view.getCbFiltro().getSelectedIndex() == 1;
			view.getBtnOfrecer().setEnabled(sinOfrecimiento);
			view.getBtnQuitar().setEnabled(!sinOfrecimiento);
			cargarEmpresas();
		});
		
		view.getBtnOfrecer().addActionListener(e -> ejecutarOfrecer());
		view.getBtnQuitar().addActionListener(e -> ejecutarQuitar());
	}

	public void initView() {
		var datos = model.getEventosConReportero();
		view.getTabEv().setModel(SwingUtil.getTableModelFromPojos(datos, new String[]{"id","nombre","fecha","reportero"}));
		view.getBtnOfrecer().setEnabled(false);
		view.getFrame().setVisible(true);
	}

	private void cargarEmpresas() {
		int f = view.getTabEv().getSelectedRow();
		if (f < 0) return;
		String idEv = view.getTabEv().getValueAt(f, 0).toString();
		
		if (view.getCbFiltro().getSelectedIndex() == 1) {
			empresasActuales = model.getEmpresasSinOfrecimiento(idEv);
		} else {
			empresasActuales = model.getEmpresasConOfrecimiento(idEv);
		}
		view.getTabEmp().setModel(SwingUtil.getTableModelFromPojos(empresasActuales, new String[]{"id","nombre","estado"}));
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

		if (empresa.getAcceso() == 1) { // Bloqueo
			JOptionPane.showMessageDialog(null, "Acción denegada: Ya tiene acceso.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (empresa.getEstado().toUpperCase().contains("ACEPTADO")) { // Email
			JOptionPane.showMessageDialog(null, "Se enviará un email automático a " + empresa.getNombre() + " notificando la baja.");
		}

		model.eliminarOfrecimiento(view.getTabEv().getValueAt(fEv, 0).toString(), empresa.getId());
		JOptionPane.showMessageDialog(null, "Ofrecimiento eliminado.");
		cargarEmpresas();
	}
}