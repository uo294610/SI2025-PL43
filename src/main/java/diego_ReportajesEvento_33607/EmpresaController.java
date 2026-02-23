package diego_ReportajesEvento_33607;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import giis.demo.util.SwingUtil;

public class EmpresaController {
    private EmpresaModel model;
    private EmpresaView view;

    public EmpresaController(EmpresaModel m, EmpresaView v) {
        this.model = m;
        this.view = v;
        this.initView();
    }

    public void initController() {
        // Cargar eventos
        view.getBtnCargarEventos().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarEventos()));

        // Cargar texto del reportaje al hacer clic en un evento
        view.getTabEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SwingUtil.exceptionWrapper(() -> cargarReportaje());
            }
        });
    }

    public void initView() {
        view.getFrame().setVisible(true);
        view.getTabEventos().setModel(new DefaultTableModel());
        limpiarTextos();
    }

    private void cargarEventos() {
        int idEmpresa = Integer.parseInt(view.getTxtEmpresaId().getText());
        List<EventoAccesoDTO> eventos = model.getEventosConAcceso(idEmpresa);
        
        TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[] { "id", "nombre", "fecha" });
        view.getTabEventos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabEventos());
        
        limpiarTextos(); // Limpiamos la pantalla de lectura si cambiamos de empresa
    }

    private void cargarReportaje() {
        int row = view.getTabEventos().getSelectedRow();
        if (row < 0) return;

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(row, 0).toString());
        ReportajeDetalleDTO reportaje = model.getUltimaVersionReportaje(idEvento);

        if (reportaje != null) {
            view.getTxtTitulo().setText(reportaje.getTitulo());
            view.getTxtSubtitulo().setText(reportaje.getSubtitulo());
            view.getTxtCuerpo().setText(reportaje.getCuerpo());
        } else {
            limpiarTextos();
            view.getTxtCuerpo().setText("No se encontró el contenido de este reportaje.");
        }
    }

    private void limpiarTextos() {
        view.getTxtTitulo().setText("");
        view.getTxtSubtitulo().setText("");
        view.getTxtCuerpo().setText("");
    }
}
