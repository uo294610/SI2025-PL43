package diego_asignarReporteros;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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
        // Cargar eventos al hacer clic en el botón superior
        view.getBtnCargarEventos().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarEventos()));

        // Al seleccionar un evento de la tabla superior, cargar los reporteros disponibles abajo
        view.getTablaEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SwingUtil.exceptionWrapper(() -> cargarReporterosDisponibles());
            }
        });

        // Al pulsar el botón de asignar
        view.getBtnAsignar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> realizarAsignacion()));
    }

    public void initView() {
        cargarEventos(); // Carga inicial
        view.getFrame().setVisible(true);
    }

    private void cargarEventos() {
        int idAgencia = Integer.parseInt(view.getIdAgencia());
        List<EventoDisplayDTO> eventos = model.getEventosSinAsignar(idAgencia);
        TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[] { "id", "nombre", "fecha" });
        view.getTablaEventos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTablaEventos());
        
        // Limpiamos la tabla de reporteros porque la selección de evento ha cambiado/desaparecido
        view.getTablaReporteros().setModel(new DefaultTableModel());
    }

    private void cargarReporterosDisponibles() {
        int idAgencia = Integer.parseInt(view.getIdAgencia());
        
        // Obtenemos la fila seleccionada
        int filaSeleccionada = view.getTablaEventos().getSelectedRow();
        if (filaSeleccionada >= 0) {
            // Sacamos la fecha del evento de la columna 2 (índice 2)
            String fechaEvento = (String) view.getTablaEventos().getValueAt(filaSeleccionada, 2);
            
            List<ReporteroDisplayDTO> reporteros = model.getReporterosDisponibles(idAgencia, fechaEvento);
            TableModel tmodel = SwingUtil.getTableModelFromPojos(reporteros, new String[] { "id", "nombre" });
            view.getTablaReporteros().setModel(tmodel);
            SwingUtil.autoAdjustColumns(view.getTablaReporteros());
        }
    }

    private void realizarAsignacion() {
        int filaEvento = view.getTablaEventos().getSelectedRow();
        int[] filasReporteros = view.getTablaReporteros().getSelectedRows();

        if (filaEvento < 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debes seleccionar un evento primero.");
            return;
        }
        if (filasReporteros.length == 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debes seleccionar al menos un reportero.");
            return;
        }

        // Obtener el ID del evento seleccionado
        int idEvento = Integer.parseInt((String) view.getTablaEventos().getValueAt(filaEvento, 0));

        // Por cada reportero seleccionado, realizar el insert
        StringBuilder asignados = new StringBuilder("Asignados con éxito:\n");
        for (int filaRep : filasReporteros) {
            int idReportero = Integer.parseInt((String) view.getTablaReporteros().getValueAt(filaRep, 0));
            String nombreReportero = (String) view.getTablaReporteros().getValueAt(filaRep, 1);
            
            model.asignarReportero(idEvento, idReportero);
            asignados.append("- ").append(nombreReportero).append("\n");
        }

        // Mostrar confirmación
        JOptionPane.showMessageDialog(view.getFrame(), asignados.toString());

        // Refrescar la vista (el evento desaparecerá porque ya tiene asignaciones)
        cargarEventos();
    }
}