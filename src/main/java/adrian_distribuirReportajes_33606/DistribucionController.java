package adrian_distribuirReportajes_33606;

import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;
import giis.demo.util.SwingUtil;

public class DistribucionController {
	private DistribucionModel model;
	private DistribucionView view;
	private List<EmpresaAceptadaDTO> empresasEnTabla;

	public DistribucionController(DistribucionModel m, DistribucionView v) {
		this.model = m; this.view = v;
	}

	public void initController() {
		view.getTabEv().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) { cargarEmpresas(); }
		});

		view.getCbFiltro().addActionListener(e -> {
			boolean sinAcceso = view.getCbFiltro().getSelectedIndex() == 0;
			view.getBtnDar().setEnabled(sinAcceso);
			view.getBtnQuitar().setEnabled(!sinAcceso);
			cargarEmpresas();
		});

		view.getBtnDar().addActionListener(e -> ejecutarDarAcceso());
		view.getBtnQuitar().addActionListener(e -> ejecutarQuitarAcceso());
	}

	public void initView() {
		var datos = model.getEventosConReportaje();
		view.getTabEv().setModel(SwingUtil.getTableModelFromPojos(datos, new String[]{"id", "nombreEvento", "estado"}));
		view.getCbFiltro().setSelectedIndex(0);
		view.getBtnQuitar().setEnabled(false);
		view.getFrame().setVisible(true);
	}

	private void cargarEmpresas() {
		int f = view.getTabEv().getSelectedRow();
		if (f < 0) return;
		
		String idEv = view.getTabEv().getValueAt(f, 0).toString();
		boolean conAcceso = view.getCbFiltro().getSelectedIndex() == 1;
		
		empresasEnTabla = model.getEmpresasPorAcceso(idEv, conAcceso);
		// AQUÍ USAMOS descargadoTexto para que salga el SÍ / NO en la tabla visual
		view.getTabEmp().setModel(SwingUtil.getTableModelFromPojos(empresasEnTabla, new String[]{"id", "nombreEmpresa", "descargadoTexto"}));
	}

	private void ejecutarDarAcceso() {
		int fEv = view.getTabEv().getSelectedRow();
		int[] fEmps = view.getTabEmp().getSelectedRows();
		
		if (fEv < 0 || fEmps.length == 0) return;

		String idEv = view.getTabEv().getValueAt(fEv, 0).toString();
		for (int i : fEmps) {
			model.darAcceso(idEv, empresasEnTabla.get(i).getId());
		}
		JOptionPane.showMessageDialog(null, "Acceso concedido.");
		cargarEmpresas();
	}

	private void ejecutarQuitarAcceso() {
		int fEv = view.getTabEv().getSelectedRow();
		int[] fEmps = view.getTabEmp().getSelectedRows();
		
		if (fEv < 0 || fEmps.length == 0) return;

		String idEv = view.getTabEv().getValueAt(fEv, 0).toString();
		
		for (int i : fEmps) {
			EmpresaAceptadaDTO emp = empresasEnTabla.get(i);
			
			// AQUÍ USAMOS descargadoValor para evaluar el 1 o el 0
			if (emp.getDescargadoValor() == 1) {
				JOptionPane.showMessageDialog(null, "No se puede revocar el acceso a " + emp.getNombreEmpresa() + " porque ya ha descargado el reportaje.", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}
			
			model.revocarAcceso(idEv, emp.getId());
		}
		cargarEmpresas();
	}
}