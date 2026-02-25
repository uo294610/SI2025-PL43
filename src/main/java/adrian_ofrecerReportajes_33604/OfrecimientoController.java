package adrian_ofrecerReportajes_33604;

import java.awt.event.*;
import javax.swing.JOptionPane;
import giis.demo.util.SwingUtil;

public class OfrecimientoController {
    private OfrecimientoModel model;
    private OfrecimientoView view;

    public OfrecimientoController(OfrecimientoModel m, OfrecimientoView v) {
        this.model = m; this.view = v;
    }

    public void initController() {
        // Al seleccionar evento, cargar sus empresas
        view.getTabEv().addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                int f = view.getTabEv().getSelectedRow();
                if (f >= 0) {
                    String idEv = view.getTabEv().getValueAt(f, 0).toString();
                    cargarEmpresas(idEv);
                }
            }
        });
        // Acción del botón principal
        view.getBtn().addActionListener(e -> realizarOfrecimiento());
    }

    public void initView() {
        var datos = model.getEventosConReportero();
        view.getTabEv().setModel(SwingUtil.getTableModelFromPojos(datos, new String[]{"id", "nombreEvento", "fecha", "nombreReportero"}));
        view.getFrame().setVisible(true);
    }

    private void cargarEmpresas(String idEv) {
        var emps = model.getEmpresasDisponibles(idEv);
        view.getTabEmp().setModel(SwingUtil.getTableModelFromPojos(emps, new String[]{"id", "nombre"}));
    }

    private void realizarOfrecimiento() {
        int fEv = view.getTabEv().getSelectedRow();
        int[] fEmps = view.getTabEmp().getSelectedRows();
        if (fEv < 0 || fEmps.length == 0) {
            JOptionPane.showMessageDialog(null, "Selecciona un evento y al menos una empresa.");
            return;
        }

        String idEv = view.getTabEv().getValueAt(fEv, 0).toString();
        StringBuilder nombres = new StringBuilder();

        for (int i : fEmps) {
            String idEmp = view.getTabEmp().getValueAt(i, 0).toString();
            String nom = view.getTabEmp().getValueAt(i, 1).toString();
            model.registrarOfrecimiento(idEv, idEmp);
            nombres.append(nom).append(i == fEmps[fEmps.length - 1] ? "" : ", ");
        }

        // Actualiza la lista del prototipo abajo
        view.setTextoEstado(nombres.toString());
        JOptionPane.showMessageDialog(null, "¡Ofrecimientos registrados!");
        cargarEmpresas(idEv); // Refresca para quitar las ya ofrecidas
    }
}