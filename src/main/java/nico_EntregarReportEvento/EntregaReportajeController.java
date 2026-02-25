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
        // 1. Llenamos el desplegable de reporteros primero
        cargarReporteros();
        
        // 2. Cargamos los eventos del primer reportero que salga seleccionado por defecto
        cargarEventosPendientes();

        // 3. ESTA ES LA MAGIA: Cada vez que cambies de reportero, se actualiza la tabla
        view.getCbReporteros().addActionListener(e -> cargarEventosPendientes());

        // 4. Listener para el botón de entregar
        view.getBtnEntregar().addActionListener(e -> giis.demo.util.SwingUtil.exceptionWrapper(() -> guardarEntrega()));

        view.getFrame().setVisible(true);
    }
    
    private void cargarReporteros() {
        List<ReporteroDisplayDTO> lista = model.getListaReporteros();
        // Limpiamos por si acaso y añadimos uno a uno
        view.getCbReporteros().removeAllItems(); 
        for (ReporteroDisplayDTO rep : lista) {
            view.getCbReporteros().addItem(rep);
        }
    }

    private void cargarEventosPendientes() {
        // 1. Miramos qué reportero está seleccionado ahora mismo en el JComboBox
        ReporteroDisplayDTO repSeleccionado = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();
        
        // Si no hay ninguno seleccionado (al arrancar puede pasar), no hacemos nada
        if (repSeleccionado == null) {
            return;
        }

        // 2. Sacamos su ID real
        int idSeleccionado = Integer.parseInt(repSeleccionado.getId());

        // 3. Buscamos en base de datos LOS EVENTOS DE ESE ID
        List<EventoResumenDTO> eventos = model.getEventosPendientes(idSeleccionado);
        
        // 4. Actualizamos la tabla
        javax.swing.table.TableModel tmodel = giis.demo.util.SwingUtil.getTableModelFromPojos(eventos, new String[] {"id", "nombre", "fecha"});
        view.getTabEventos().setModel(tmodel);
        giis.demo.util.SwingUtil.autoAdjustColumns(view.getTabEventos());
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
        
     // Recogemos el reportero que el usuario ha elegido en el desplegable
        ReporteroDisplayDTO repSeleccionado = (ReporteroDisplayDTO) view.getCbReporteros().getSelectedItem();

        if (repSeleccionado == null) {
            JOptionPane.showMessageDialog(view.getFrame(), "Debe seleccionar un reportero del desplegable.");
            return;
        }

        // Sacamos su ID real para guardarlo en la base de datos
        int idReporteroSeleccionado = Integer.parseInt(repSeleccionado.getId());


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