package nico_RestaurarVersionReport_33612;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import giis.demo.util.SwingUtil;

public class RestaurarVersionController {
    private RestaurarVersionModel model;
    private RestaurarVersionView view;
    
    private int idReportaje;
    private int idReporteroLogueado;
    private int idAutorReportaje; // Para comprobar permisos

    // Constructor: Recibe el ID del reportaje que vamos a mirar y quién es el que está usando la app
    public RestaurarVersionController(RestaurarVersionModel m, RestaurarVersionView v, int idReportaje, int idReporteroLogueado) {
        this.model = m;
        this.view = v;
        this.idReportaje = idReportaje;
        this.idReporteroLogueado = idReporteroLogueado;
    }

    public void initController() {
        cargarDatosCabecera();
        cargarTablaVersiones();
        configurarListeners();
        
        view.getFrame().setVisible(true);
    }

    private void configurarListeners() {
        // Al hacer clic en una fila de la tabla de versiones
        view.getTabVersiones().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarVersionSeleccionada();
            }
        });

        // Botón Guardar (o Restaurar)
        view.getBtnGuardar().addActionListener(e -> guardarRestauracion());
        view.getBtnRestaurar().addActionListener(e -> guardarRestauracion()); // Hacen lo mismo
        
        // Botón Cancelar
        view.getBtnCancelar().addActionListener(e -> view.getFrame().dispose());
    }

    private void cargarDatosCabecera() {
        List<Object[]> datos = model.getDatosCabeceraReportaje(idReportaje);
        if (datos != null && !datos.isEmpty()) {
            Object[] fila = datos.get(0);
            view.getTxtEvento().setText(fila[0].toString()); // Nombre del Evento
            view.getTxtTituloGeneral().setText(fila[1].toString()); // Título
            idAutorReportaje = Integer.parseInt(fila[2].toString()); // ID del autor original
        }
        
        // Comprobar permisos: Si no eres el autor, bloqueamos los botones
        if (idReporteroLogueado != idAutorReportaje) {
            view.getBtnGuardar().setEnabled(false);
            view.getBtnRestaurar().setEnabled(false);
            JOptionPane.showMessageDialog(view.getFrame(), "Solo el reportero que hizo la entrega (" + idAutorReportaje + ") puede restaurar versiones.");
        }
    }

    private void cargarTablaVersiones() {
        List<VersionDisplayDTO> versiones = model.getVersionesReportaje(idReportaje);
        
        // Mostramos las columnas en la tabla
        view.getTabVersiones().setModel(SwingUtil.getTableModelFromPojos(versiones, new String[] {"fecha_hora", "que_cambio"}));
        SwingUtil.autoAdjustColumns(view.getTabVersiones());
        
        // Cargamos la versión ACTUAL (que siempre es la primera de la lista, la más reciente)
        if (!versiones.isEmpty()) {
            VersionDisplayDTO actual = versiones.get(0);
            view.getTxtTituloActual().setText(view.getTxtTituloGeneral().getText());
            view.getTxtSubtituloActual().setText(actual.getSubtitulo());
            view.getAreaCuerpoActual().setText(actual.getCuerpo());
        }
    }

    private void mostrarVersionSeleccionada() {
        int fila = view.getTabVersiones().getSelectedRow();
        if (fila < 0) return;

        // Cogemos la lista de nuevo para sacar los datos de la fila clicada
        List<VersionDisplayDTO> versiones = model.getVersionesReportaje(idReportaje);
        VersionDisplayDTO seleccionada = versiones.get(fila);

        // Rellenamos el panel de abajo a la derecha
        view.getTxtTituloSeleccionada().setText(view.getTxtTituloGeneral().getText());
        view.getTxtSubtituloSeleccionada().setText(seleccionada.getSubtitulo());
        view.getAreaCuerpoSeleccionada().setText(seleccionada.getCuerpo());
    }

    private void guardarRestauracion() {
        int fila = view.getTabVersiones().getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Primero debes seleccionar una versión de la tabla para restaurarla.");
            return;
        }
        
        if (fila == 0) {
            JOptionPane.showMessageDialog(view.getFrame(), "Esa ya es la versión actual. Selecciona una versión más antigua.");
            return;
        }

        // Cogemos el texto de la versión antigua a la que queremos volver
        String subtituloRestaurado = view.getTxtSubtituloSeleccionada().getText();
        String cuerpoRestaurado = view.getAreaCuerpoSeleccionada().getText();
        String fechaAntigua = view.getTabVersiones().getValueAt(fila, 0).toString(); // Fecha de la versión que restauramos

        // Preparamos el nuevo registro para la base de datos
        nico_EntregarReportEvento.VersionReportajeEntity nuevaVersion = new nico_EntregarReportEvento.VersionReportajeEntity();
        nuevaVersion.setId(model.getUltimoId("VersionReportaje") + 1);
        nuevaVersion.setReportaje_id(idReportaje);
        nuevaVersion.setSubtitulo(subtituloRestaurado);
        nuevaVersion.setCuerpo(cuerpoRestaurado);
        
        // Registramos la fecha y hora EXACTA de ahora mismo
        java.sql.Timestamp fechaHoraCambio = new java.sql.Timestamp(System.currentTimeMillis());
        nuevaVersion.setFecha_hora(fechaHoraCambio);
        
        // Cumplimos el requisito: "Registrar qué ha cambiado". En este caso, que se ha restaurado.
        nuevaVersion.setQue_cambio("Restauración a la versión del: " + fechaAntigua);

        try {
            model.insertarVersionRestaurada(nuevaVersion);
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            JOptionPane.showMessageDialog(view.getFrame(), 
                "¡Versión restaurada con éxito!\n" +
                "Se ha generado una nueva versión en el historial.\n" +
                "Fecha y hora: " + sdf.format(fechaHoraCambio));
                
            // Recargamos todo para que la tabla se actualice
            cargarTablaVersiones();
            
            // Limpiamos la selección de abajo
            view.getTxtSubtituloSeleccionada().setText("");
            view.getAreaCuerpoSeleccionada().setText("");
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(view.getFrame(), "Error al restaurar: " + ex.getMessage());
        }
    }
}