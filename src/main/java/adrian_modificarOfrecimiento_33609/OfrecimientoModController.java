package adrian_modificarOfrecimiento_33609;

import java.awt.event.*;
import javax.swing.JOptionPane;
import giis.demo.util.SwingUtil;
import java.util.List;

public class OfrecimientoModController {
	private OfrecimientoModModel model;
	private OfrecimientoModView view;
	private List<EmpresaModDTO> empresasActuales;

	public OfrecimientoModController(OfrecimientoModModel m, OfrecimientoModView v) {
		this.model = m; this.view = v;
	}

	public void initController() {
		view.getTabEv().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) { cargarEmpresas(); }
		});
		
		view.getCbFiltro().addActionListener(e -> {
			// El botón "Quitar" solo tiene sentido si estamos viendo las que SÍ tienen ofrecimiento
			view.getBtnQuitar().setEnabled(view.getCbFiltro().getSelectedIndex() == 0);
			cargarEmpresas();
		});
		
		view.getBtnQuitar().addActionListener(e -> ejecutarAccion());
	}

	public void initView() {
		var datos = model.getEventosAsignados();
		view.getTabEv().setModel(SwingUtil.getTableModelFromPojos(datos, new String[]{"id","nombre","fecha","reportero"}));
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

	private void ejecutarAccion() {
		int fEv = view.getTabEv().getSelectedRow();
		int fEmp = view.getTabEmp().getSelectedRow();
		if (fEv < 0 || fEmp < 0) return;

		EmpresaModDTO empresa = empresasActuales.get(fEmp);

		// REGLA 1: Si tiene acceso (acceso == 1), NO se puede tocar
		if (empresa.getAcceso() == 1) {
			JOptionPane.showMessageDialog(null, 
				"Acción denegada: No se puede retirar el ofrecimiento porque la empresa ya tiene acceso al contenido.", 
				"Error de Validación", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// REGLA 2: Si aceptó (pero no tiene acceso), mandamos email
		if ("ACEPTADO".equalsIgnoreCase(empresa.getEstado())) {
			JOptionPane.showMessageDialog(null, 
				"Se va a proceder a retirar el ofrecimiento. Se enviará un email automático a " + empresa.getNombre() + " notificando la baja.");
		}

		String idEv = view.getTabEv().getValueAt(fEv, 0).toString();
		model.eliminarOfrecimiento(idEv, empresa.getId());
		JOptionPane.showMessageDialog(null, "Ofrecimiento eliminado con éxito.");
		cargarEmpresas();
	}
}