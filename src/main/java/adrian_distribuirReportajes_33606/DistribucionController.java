package adrian_distribuirReportajes_33606;

import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import giis.demo.util.SwingUtil;

public class DistribucionController {
	private DistribucionModel model;
	private DistribucionView view;
	private List<EmpresaAceptadaDTO> empresasEnTabla;

	public DistribucionController(DistribucionModel m, DistribucionView v) {
		this.model = m; this.view = v;
	}

	public void initController() {
		// Al cambiar el combo de embargo, recargamos la tabla de arriba
		view.getCbFiltroEmbargo().addActionListener(e -> recargarEventos());

		// Al seleccionar un evento
		view.getTabEv().getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int row = view.getTabEv().getSelectedRow();
				if (row >= 0) {
					// HU #34308: Habilitar Acceso Parcial solo si hay embargo
					String embargoStr = view.getTabEv().getValueAt(row, 3).toString();
					boolean tieneEmbargo = "SÍ (ACTIVO)".equals(embargoStr);
					view.getRbAccesoParcial().setEnabled(tieneEmbargo);
					if (!tieneEmbargo) {
						view.getRbAccesoCompleto().setSelected(true); // Forzar completo
					}
				}
				cargarEmpresas();
			}
		});

		view.getCbFiltroAcceso().addActionListener(e -> {
			boolean sinAcceso = view.getCbFiltroAcceso().getSelectedIndex() == 0;
			view.getBtnDar().setEnabled(sinAcceso);
			view.getBtnQuitar().setEnabled(!sinAcceso);
			cargarEmpresas();
		});

		view.getBtnDar().addActionListener(e -> ejecutarDarAcceso());
		view.getBtnQuitar().addActionListener(e -> ejecutarQuitarAcceso());
	}

	public void initView() {
		recargarEventos();
		view.getCbFiltroAcceso().setSelectedIndex(0);
		view.getBtnQuitar().setEnabled(false);
		view.getFrame().setVisible(true);
	}

	private void recargarEventos() {
		String filtroEmbargo = view.getCbFiltroEmbargo().getSelectedItem().toString();
		var datos = model.getEventosConReportaje(filtroEmbargo);
		view.getTabEv().setModel(SwingUtil.getTableModelFromPojos(datos, new String[]{"id", "nombreEvento", "estado", "embargo"}));
		view.getTabEv().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	private void cargarEmpresas() {
		int f = view.getTabEv().getSelectedRow();
		if (f < 0) return;
		
		String idEv = view.getTabEv().getValueAt(f, 0).toString();
		boolean conAcceso = view.getCbFiltroAcceso().getSelectedIndex() == 1;
		
		empresasEnTabla = model.getEmpresasPorAcceso(idEv, conAcceso);
		// Actualizado con las columnas del S3
		view.getTabEmp().setModel(SwingUtil.getTableModelFromPojos(empresasEnTabla, new String[]{"id", "nombreEmpresa", "estadoPago", "descargadoTexto", "tipoAccesoActual"}));
	}

	private void ejecutarDarAcceso() {
		int fEv = view.getTabEv().getSelectedRow();
		int[] fEmps = view.getTabEmp().getSelectedRows();
		
		if (fEv < 0 || fEmps.length == 0) return;

		String idEv = view.getTabEv().getValueAt(fEv, 0).toString();
		String tipoAcceso = view.getRbAccesoParcial().isSelected() ? "PARCIAL" : "COMPLETO";

		for (int i : fEmps) {
			EmpresaAceptadaDTO emp = empresasEnTabla.get(i);
			
			// HU #34304: Validar estado de pagos antes de dar acceso
			if (emp.getTieneTarifaPlana() == 1 && emp.getTarifaPlanaPagada() == 0) {
				JOptionPane.showMessageDialog(null, "BLOQUEO: La empresa " + emp.getNombreEmpresa() + " tiene una DEUDA en su Tarifa Plana. No se le puede dar acceso.", "Impago detectado", JOptionPane.WARNING_MESSAGE);
				continue;
			}
			if (emp.getTieneTarifaPlana() == 0 && !"PAGADO".equals(emp.getEstadoPagoIndividual())) {
				JOptionPane.showMessageDialog(null, "BLOQUEO: La empresa " + emp.getNombreEmpresa() + " no ha pagado el importe de este reportaje.", "Impago detectado", JOptionPane.WARNING_MESSAGE);
				continue;
			}

			model.darAcceso(idEv, emp.getId(), tipoAcceso);
		}
		JOptionPane.showMessageDialog(null, "Proceso de distribución finalizado.");
		cargarEmpresas();
	}

	private void ejecutarQuitarAcceso() {
		int fEv = view.getTabEv().getSelectedRow();
		int[] fEmps = view.getTabEmp().getSelectedRows();
		
		if (fEv < 0 || fEmps.length == 0) return;
		String idEv = view.getTabEv().getValueAt(fEv, 0).toString();
		
		for (int i : fEmps) {
			EmpresaAceptadaDTO emp = empresasEnTabla.get(i);
			if (emp.getDescargadoValor() == 1) {
				JOptionPane.showMessageDialog(null, "No se puede revocar el acceso a " + emp.getNombreEmpresa() + " porque ya ha descargado el reportaje.", "Error", JOptionPane.ERROR_MESSAGE);
				continue;
			}
			model.revocarAcceso(idEv, emp.getId());
		}
		cargarEmpresas();
	}
}