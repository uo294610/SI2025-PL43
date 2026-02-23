package adrian_distribuirReportajes_33606;

import java.awt.event.*;
import javax.swing.JOptionPane;
import giis.demo.util.SwingUtil;
import java.util.List;

public class DistribucionController {
	private DistribucionModel model;
	private DistribucionView view;

	public DistribucionController(DistribucionModel m, DistribucionView v) {
		this.model = m; this.view = v;
	}

	public void initController() {
		view.getTabEv().addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				int f = view.getTabEv().getSelectedRow();
				if (f >= 0) {
					String idEv = view.getTabEv().getValueAt(f, 0).toString();
					cargarEmpresas(idEv);
				}
			}
		});
		view.getBtn().addActionListener(e -> ejecutarDistribucion());
	}

	public void initView() {
		var datos = model.getEventosConReportaje();
		view.getTabEv().setModel(SwingUtil.getTableModelFromPojos(datos, new String[]{"id", "nombreEvento", "estado"}));
		view.getFrame().setVisible(true);
	}

	private void cargarEmpresas(String idEv) {
		var emps = model.getEmpresasAceptadasSinAcceso(idEv);
		view.getTabEmp().setModel(SwingUtil.getTableModelFromPojos(emps, new String[]{"id", "nombreEmpresa"}));
	}

	private void ejecutarDistribucion() {
		int fEv = view.getTabEv().getSelectedRow();
		int[] fEmps = view.getTabEmp().getSelectedRows();
		if (fEv < 0 || fEmps.length == 0) return;

		String idEv = view.getTabEv().getValueAt(fEv, 0).toString();
		StringBuilder nombres = new StringBuilder();

		for (int i : fEmps) {
			String idEmp = view.getTabEmp().getValueAt(i, 0).toString();
			String nom = view.getTabEmp().getValueAt(i, 1).toString();
			model.darAcceso(idEv, idEmp);
			nombres.append(nom).append(", ");
		}

		view.setTextoAcceso(nombres.toString()); // Cumple prototipo
		JOptionPane.showMessageDialog(null, "Acceso concedido a las empresas seleccionadas.");
		cargarEmpresas(idEv);
	}
}