package diego_asignarReporteros;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
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
        // Cargar eventos al pulsar el botón
        view.getBtnCargarEventos().addActionListener(e -> SwingUtil.exceptionWrapper(() -> getEventosSinAsignar()));

        // Al seleccionar un evento, cargar reporteros disponibles
        view.getTabEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SwingUtil.exceptionWrapper(() -> actualizarTablasReporteros());
            }
        });

        // Asignar los reporteros seleccionados
        view.getBtnAsignar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> asignarReporteros()));
    }

    public void initView() {
        view.getFrame().setVisible(true);
        // Limpiamos las tablas al inicio
        view.getTabEventos().setModel(new DefaultTableModel());
        view.getTabReporteros().setModel(new DefaultTableModel());
    }

    private void getEventosSinAsignar() {
        // 1. Chivato al principio del todo para saber si el botón funciona
        System.out.println(">>> 1. ¡Clic detectado! Entrando al método...");

        try {
            int idAgencia = Integer.parseInt(view.getTxtAgenciaId().getText());
            System.out.println(">>> 2. ID de la agencia leído correctamente: " + idAgencia);

            // Si falla la Base de Datos, se romperá justo aquí y saltará al catch
            List<EventoDTO> eventos = model.getEventosSinAsignar(idAgencia);
            System.out.println(">>> 3. Consulta a la BD completada. Eventos: " + eventos.size());

            TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[] { "id", "nombre", "fecha" });
            view.getTabEventos().setModel(tmodel);
            SwingUtil.autoAdjustColumns(view.getTabEventos());
            
            view.getTabReporteros().setModel(new DefaultTableModel());
            System.out.println(">>> 4. Tablas actualizadas visualmente.");

        } catch (Exception e) {
            // Si hay un error silencioso, esto lo va a cazar sí o sí
            System.err.println(">>> ¡BINGO! Hemos cazado un error:");
            e.printStackTrace();
        }
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
            JOptionPane.showMessageDialog(view.getFrame(), "Debe seleccionar 1 evento y al menos 1 reportero.");
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(rowEvento, 0).toString());

        for (int row : rowsReporteros) {
            int idReportero = Integer.parseInt(view.getTabReporteros().getValueAt(row, 0).toString());
            model.asignarReportero(idEvento, idReportero);
        }

        JOptionPane.showMessageDialog(view.getFrame(), "Asignación realizada con éxito.");
        
        // EN LUGAR DE ACTUALIZAR LA TABLA DE EVENTOS, ACTUALIZAMOS LOS REPORTEROS
        // Así ves cómo pasan de una tabla a otra sin perder de vista el evento.
        actualizarTablasReporteros(); 
    }
}