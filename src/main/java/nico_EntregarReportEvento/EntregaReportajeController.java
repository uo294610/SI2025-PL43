package nico_EntregarReportEvento;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import giis.demo.util.SwingUtil;

public class EntregaReportajeController {
    private EntregaReportajeModel model;
    private EntregaReportajeView view;
    private int idReportero;

    public EntregaReportajeController(EntregaReportajeModel m, EntregaReportajeView v, int idReportero) {
        this.model = m;
        this.view = v;
        this.idReportero = idReportero;
    }

    public void initController() {
        // Carga inicial de datos
        cargarEventos();
        
        // Listener para el botón Entregar
        view.getBtnEntregar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> guardarEntrega()));
        
        // Listener para Cancelar
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());

        view.getFrame().setVisible(true);
    }

    private void cargarEventos() {
        // Filtra eventos asignados al reportero sin entrega previa
        List<EventoResumenDTO> eventos = model.getEventosPendientes(idReportero);
        TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[] {"id", "nombre", "fecha"});
        view.getTabEventos().setModel(tmodel);
    }

    private void guardarEntrega() {
        int fila = view.getTabEventos().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Selecciona un evento de la lista.");
            return;
        }

        String titulo = view.getTxtTitulo().getText();
        if (titulo.isEmpty() || model.existeTitulo(titulo)) { // Validación de título único
            JOptionPane.showMessageDialog(view.getFrame(), "El título es obligatorio y debe ser único.");
            return;
        }

        // Crear Entidades según el modelo relacional
        int idEvento = (int) view.getTabEventos().getValueAt(fila, 0);
        int nuevoIdRep = model.getUltimoId("Reportaje") + 1;

        ReportajeEntity re = new ReportajeEntity();
        re.setId(nuevoIdRep);
        re.setTitulo(titulo);
        re.setEvento_id(idEvento);
        re.setReportero_entrega_id(idReportero);

        VersionReportajeEntity ve = new VersionReportajeEntity();
        ve.setId(model.getUltimoId("VersionReportaje") + 1);
        ve.setReportaje_id(nuevoIdRep);
        ve.setSubtitulo(view.getTxtSubtitulo().getText());
        ve.setCuerpo(view.getAreaCuerpo().getText());
        ve.setFecha_hora(new java.sql.Timestamp(System.currentTimeMillis()));
        ve.setQue_cambio("Versión inicial");

        // Persistencia doble en base de datos
        model.insertarReportaje(re);
        model.insertarVersion(ve);

        JOptionPane.showMessageDialog(view.getFrame(), "¡Reportaje entregado correctamente!");
        view.getFrame().dispose();
        
        
    }
}