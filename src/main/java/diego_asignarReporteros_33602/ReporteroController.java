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
        view.getBtnCargarEventos().addActionListener(e -> SwingUtil.exceptionWrapper(() -> getEventos()));
        view.getChkFiltroTematica().addActionListener(e -> SwingUtil.exceptionWrapper(() -> actualizarTablasReporteros()));
        view.getCbTipoReportero().addActionListener(e -> SwingUtil.exceptionWrapper(() -> actualizarTablasReporteros()));

        view.getTabEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SwingUtil.exceptionWrapper(() -> actualizarTablasReporteros());
            }
        });

        view.getBtnAsignar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> asignarReporteros()));
        view.getBtnEliminar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> eliminarReporteros()));
        view.getBtnHacerResponsable().addActionListener(e -> SwingUtil.exceptionWrapper(() -> hacerResponsable()));
        view.getBtnFinalizarAsignacion().addActionListener(e -> SwingUtil.exceptionWrapper(() -> finalizarAsignacion()));
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
        
        if (view.getRbSinAsignar().isSelected()) {
            eventos = model.getEventosSinAsignar(idAgencia);
        } else {
            eventos = model.getEventosConAsignacion(idAgencia);
        }
        
        TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[] { "id", "nombre", "fecha", "fechaFin", "tematica", "asignacionFinalizada" });
        view.getTabEventos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabEventos());
        
        view.getTabReporteros().setModel(new DefaultTableModel());
        view.getTabReporterosAsignados().setModel(new DefaultTableModel());
    }

    private void actualizarTablasReporteros() {
        int row = view.getTabEventos().getSelectedRow();
        if (row < 0) return; 

        int idAgencia = Integer.parseInt(view.getTxtAgenciaId().getText());
        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(row, 0).toString());
        
        String fechaInicio = view.getTabEventos().getValueAt(row, 2).toString(); 
        String fechaFin = view.getTabEventos().getValueAt(row, 3).toString(); 
        boolean finalizada = Boolean.parseBoolean(view.getTabEventos().getValueAt(row, 5).toString());
        
        // Bloqueo si el evento está finalizado
        view.getBtnAsignar().setEnabled(!finalizada);
        view.getBtnEliminar().setEnabled(!finalizada);
        view.getBtnHacerResponsable().setEnabled(!finalizada);
        view.getBtnFinalizarAsignacion().setEnabled(!finalizada);

        boolean filtrarTematica = view.getChkFiltroTematica().isSelected();
        String tipoFiltro = view.getCbTipoReportero().getSelectedItem().toString();

        List<ReporteroDTO> reporteros = model.getReporterosDisponibles(idAgencia, fechaInicio, fechaFin, idEvento, filtrarTematica, tipoFiltro);
        TableModel tmodelDisp = SwingUtil.getTableModelFromPojos(reporteros, new String[] { "id", "nombre", "tipo", "tematica" });
        view.getTabReporteros().setModel(tmodelDisp);
        SwingUtil.autoAdjustColumns(view.getTabReporteros());

        List<ReporteroDTO> asignados = model.getReporterosAsignados(idEvento);
        TableModel tmodelAsig = SwingUtil.getTableModelFromPojos(asignados, new String[] { "id", "nombre", "tipo", "tematica", "rol" });
        view.getTabReporterosAsignados().setModel(tmodelAsig);
        SwingUtil.autoAdjustColumns(view.getTabReporterosAsignados());
    }

    private void asignarReporteros() {
        int rowEvento = view.getTabEventos().getSelectedRow();
        int[] rowsReporteros = view.getTabReporteros().getSelectedRows();

        if (rowEvento < 0 || rowsReporteros.length == 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debe seleccionar 1 evento y al menos 1 reportero disponible.");
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(rowEvento, 0).toString());
        for (int row : rowsReporteros) {
            int idReportero = Integer.parseInt(view.getTabReporteros().getValueAt(row, 0).toString());
            model.asignarReportero(idEvento, idReportero);
        }
        actualizarTablasReporteros(); 
    }

    private void eliminarReporteros() {
        int rowEvento = view.getTabEventos().getSelectedRow();
        int[] rowsReporterosAsignados = view.getTabReporterosAsignados().getSelectedRows();

        if (rowEvento < 0 || rowsReporterosAsignados.length == 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debe seleccionar 1 evento y al menos 1 reportero ASIGNADO.");
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(rowEvento, 0).toString());
        for (int row : rowsReporterosAsignados) {
            int idReportero = Integer.parseInt(view.getTabReporterosAsignados().getValueAt(row, 0).toString());
            model.eliminarAsignacion(idEvento, idReportero);
        }
        actualizarTablasReporteros(); 
    }

    private void hacerResponsable() {
        int rowEvento = view.getTabEventos().getSelectedRow();
        int[] rowsAsignados = view.getTabReporterosAsignados().getSelectedRows();

        if (rowEvento < 0 || rowsAsignados.length != 1) {
            JOptionPane.showMessageDialog(view.getFrame(), "Para designar a un responsable, seleccione EXACTAMENTE UN reportero de la lista de asignados.");
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(rowEvento, 0).toString());
        int idReportero = Integer.parseInt(view.getTabReporterosAsignados().getValueAt(rowsAsignados[0], 0).toString());
        
        model.setResponsable(idEvento, idReportero);
        actualizarTablasReporteros();
    }

    private void finalizarAsignacion() {
        int rowEvento = view.getTabEventos().getSelectedRow();
        if (rowEvento < 0) return;

        int countResponsables = 0;
        int countBases = 0;

        for (int i = 0; i < view.getTabReporterosAsignados().getRowCount(); i++) {
            String rol = view.getTabReporterosAsignados().getValueAt(i, 4).toString();
            if (rol.equals("Responsable")) countResponsables++;
            if (rol.equals("Base")) countBases++;
        }

        if (countResponsables != 1 || countBases < 1) {
            JOptionPane.showMessageDialog(view.getFrame(), 
                "ERROR: Para finalizar la asignación debe existir obligatoriamente:\n" +
                "- Exactamente 1 reportero Responsable.\n" +
                "- Al menos 1 reportero Base.", 
                "Reglas de Asignación", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(rowEvento, 0).toString());
        model.finalizarAsignacion(idEvento);
        
        JOptionPane.showMessageDialog(view.getFrame(), "¡Asignación finalizada con éxito! Ya no se podrán hacer modificaciones.");
        getEventos(); 
    }
}