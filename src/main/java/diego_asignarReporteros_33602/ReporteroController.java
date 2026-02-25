package diego_asignarReporteros_33602;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JOptionPane;
import giis.demo.util.SwingUtil;

public class ReporteroController {
    private ReporteroModel model;
    private ReporteroView view;

    public ReporteroController(ReporteroModel m, ReporteroView v) {
        this.model = m;
        this.view = v;
        this.initView();
    }

    public void initController() {
        // Cargar eventos (ahora leerá el filtro)
        view.getBtnCargarEventos().addActionListener(e -> SwingUtil.exceptionWrapper(() -> getEventos()));

        // Cargar reporteros al seleccionar evento
        view.getTabEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SwingUtil.exceptionWrapper(() -> actualizarTablasReporteros());
            }
        });

        // Asignar
        view.getBtnAsignar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> asignarReporteros()));
        
        // Eliminar (NUEVO)
        view.getBtnEliminar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> eliminarReporteros()));
    }

    public void initView() {
        view.getFrame().setVisible(true);
        view.getTabEventos().setModel(new DefaultTableModel());
        view.getTabReporteros().setModel(new DefaultTableModel());
        view.getTabReporterosAsignados().setModel(new DefaultTableModel());
    }

    private void getEventos() {
        int idAgencia = Integer.parseInt(view.getTxtAgenciaId().getText());
        List<EventoDTO> eventos;
        
        // LEEMOS EL FILTRO PARA DECIDIR QUÉ MÉTODO DEL MODELO LLAMAR
        if (view.getRbSinAsignar().isSelected()) {
            eventos = model.getEventosSinAsignar(idAgencia);
        } else {
            eventos = model.getEventosConAsignacion(idAgencia);
        }
        
        TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[] { "id", "nombre", "fecha" });
        view.getTabEventos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabEventos());
        
        // Limpiamos las tablas de abajo
        view.getTabReporteros().setModel(new DefaultTableModel());
        view.getTabReporterosAsignados().setModel(new DefaultTableModel());
    }

    private void actualizarTablasReporteros() {
        int row = view.getTabEventos().getSelectedRow();
        if (row < 0) return; 

        int idAgencia = Integer.parseInt(view.getTxtAgenciaId().getText());
        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(row, 0).toString());
        String fechaEvento = view.getTabEventos().getValueAt(row, 2).toString(); 

        // 1. Cargar disponibles
        List<ReporteroDTO> reporteros = model.getReporterosDisponibles(idAgencia, fechaEvento);
        TableModel tmodelDisp = SwingUtil.getTableModelFromPojos(reporteros, new String[] { "id", "nombre" });
        view.getTabReporteros().setModel(tmodelDisp);
        SwingUtil.autoAdjustColumns(view.getTabReporteros());

        // 2. Cargar asignados
        List<ReporteroDTO> asignados = model.getReporterosAsignados(idEvento);
        TableModel tmodelAsig = SwingUtil.getTableModelFromPojos(asignados, new String[] { "id", "nombre" });
        view.getTabReporterosAsignados().setModel(tmodelAsig);
        SwingUtil.autoAdjustColumns(view.getTabReporterosAsignados());
    }

    private void asignarReporteros() {
        int rowEvento = view.getTabEventos().getSelectedRow();
        int[] rowsReporteros = view.getTabReporteros().getSelectedRows();

        if (rowEvento < 0 || rowsReporteros.length == 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debe seleccionar 1 evento y al menos 1 reportero de la lista de disponibles.");
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(rowEvento, 0).toString());

        for (int row : rowsReporteros) {
            int idReportero = Integer.parseInt(view.getTabReporteros().getValueAt(row, 0).toString());
            model.asignarReportero(idEvento, idReportero);
        }

        JOptionPane.showMessageDialog(view.getFrame(), "Asignación realizada.");
        actualizarTablasReporteros(); 
    }

    // NUEVO MÉTODO
    private void eliminarReporteros() {
        int rowEvento = view.getTabEventos().getSelectedRow();
        int[] rowsReporterosAsignados = view.getTabReporterosAsignados().getSelectedRows();

        if (rowEvento < 0 || rowsReporterosAsignados.length == 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debe seleccionar 1 evento y al menos 1 reportero de la lista de ASIGNADOS.");
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(rowEvento, 0).toString());

        // Recorremos los que queremos borrar
        for (int row : rowsReporterosAsignados) {
            int idReportero = Integer.parseInt(view.getTabReporterosAsignados().getValueAt(row, 0).toString());
            model.eliminarAsignacion(idEvento, idReportero);
        }

        JOptionPane.showMessageDialog(view.getFrame(), "Reportero(s) eliminado(s) del evento.");
        actualizarTablasReporteros(); 
    }
}