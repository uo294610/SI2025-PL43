package diego_ReportajesEvento_33607;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
        view.getBtnCargarEventos().addActionListener(e -> SwingUtil.exceptionWrapper(() -> cargarEventos()));

        view.getTabEventos().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                SwingUtil.exceptionWrapper(() -> cargarReportaje());
            }
        });
        view.getBtnDescargarJson().addActionListener(e -> SwingUtil.exceptionWrapper(() -> descargarJson()));
    }

    public void initView() {
        view.getFrame().setVisible(true);
        view.getTabEventos().setModel(new DefaultTableModel());
        limpiarTextos();
    }

    private void cargarEventos() {
        int idEmpresa = Integer.parseInt(view.getTxtEmpresaId().getText());
        List<EventoAccesoDTO> eventos = model.getEventosConAcceso(idEmpresa);
        
        TableModel tmodel = SwingUtil.getTableModelFromPojos(eventos, new String[] { "id", "nombre", "fecha", "estadoEmbargo", "estadoAcceso" });
        view.getTabEventos().setModel(tmodel);
        SwingUtil.autoAdjustColumns(view.getTabEventos());
        
        limpiarTextos(); 
    }

    private void cargarReportaje() {
        int row = view.getTabEventos().getSelectedRow();
        if (row < 0) return;

        String estadoEmbargo = view.getTabEventos().getValueAt(row, 3) != null ? view.getTabEventos().getValueAt(row, 3).toString() : "";
        String estadoAcceso = view.getTabEventos().getValueAt(row, 4) != null ? view.getTabEventos().getValueAt(row, 4).toString() : "";
        boolean isEmbargado = "Embargado".equals(estadoEmbargo);

        if (isEmbargado && ("NINGUNO".equalsIgnoreCase(estadoAcceso) || estadoAcceso.isEmpty())) {
            limpiarTextos();
            view.getBtnDescargarJson().setEnabled(false);
            JOptionPane.showMessageDialog(view.getFrame(), 
                "⛔ Acceso denegado. Este reportaje está bajo embargo y no tienes concedido el acceso parcial.", 
                "Embargo Activo", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(row, 0).toString());
        ReportajeDetalleDTO reportaje = model.getUltimaVersionReportaje(idEvento);

        if (reportaje != null) {
            view.getTxtTitulo().setText(reportaje.getTitulo());
            view.getTxtSubtitulo().setText(reportaje.getSubtitulo());
            view.getTxtCuerpo().setText(reportaje.getCuerpo());
            
            if (isEmbargado && "PARCIAL".equalsIgnoreCase(estadoAcceso)) {
                DefaultTableModel mediaModel = new DefaultTableModel(new Object[]{"ruta", "tipo"}, 0);
                mediaModel.addRow(new Object[]{"⚠️ Contenido oculto. Acceso parcial por embargo.", "---"});
                view.getTabMultimedia().setModel(mediaModel);
                SwingUtil.autoAdjustColumns(view.getTabMultimedia());
                
                view.getBtnDescargarJson().setEnabled(false);
            } else {
                List<MultimediaDTO> multimedia = model.getMultimediaDefinitiva(idEvento);
                TableModel tmodelMedia = SwingUtil.getTableModelFromPojos(multimedia, new String[] { "ruta", "tipo" });
                view.getTabMultimedia().setModel(tmodelMedia);
                SwingUtil.autoAdjustColumns(view.getTabMultimedia());
                
                view.getBtnDescargarJson().setEnabled(true);
            }
            
        } else {
            limpiarTextos();
            view.getTxtCuerpo().setText("No se encontró el contenido de este reportaje.");
        }
    }

    private void limpiarTextos() {
        view.getTxtTitulo().setText("");
        view.getTxtSubtitulo().setText("");
        view.getTxtCuerpo().setText("");
        view.getTabMultimedia().setModel(new DefaultTableModel()); 
        view.getBtnDescargarJson().setEnabled(false); 
    }
    
    // Método para crear y guardar el JSON
    private void descargarJson() {
        int row = view.getTabEventos().getSelectedRow();
        
        if (row < 0 || view.getTxtTitulo().getText().isEmpty()) {
            JOptionPane.showMessageDialog(view.getFrame(), "Seleccione primero un evento con un reportaje válido.");
            return;
        }

        int idEvento = Integer.parseInt(view.getTabEventos().getValueAt(row, 0).toString());
        
        String titulo = escapeJson(view.getTxtTitulo().getText());
        String subtitulo = escapeJson(view.getTxtSubtitulo().getText());
        String cuerpo = escapeJson(view.getTxtCuerpo().getText());
        List<MultimediaDTO> multimedia = model.getMultimediaDefinitiva(idEvento);
        
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"titulo\": \"").append(titulo).append("\",\n");
        json.append("  \"subtitulo\": \"").append(subtitulo).append("\",\n");
        json.append("  \"cuerpo\": \"").append(cuerpo).append("\",\n");
        json.append("  \"multimedia\": [\n");
        
        for (int i = 0; i < multimedia.size(); i++) {
            MultimediaDTO m = multimedia.get(i);
            json.append("    { \"ruta\": \"").append(escapeJson(m.getRuta())).append("\", \"tipo\": \"").append(escapeJson(m.getTipo())).append("\" }");
            if (i < multimedia.size() - 1) json.append(","); 
            json.append("\n");
        }
        json.append("  ]\n");
        json.append("}");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Reportaje como JSON");
        fileChooser.setSelectedFile(new java.io.File("reportaje_evento_" + idEvento + ".json")); 
        
        int userSelection = fileChooser.showSaveDialog(view.getFrame());
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            try {
                java.nio.file.Files.writeString(fileToSave.toPath(), json.toString(), java.nio.charset.StandardCharsets.UTF_8);
                model.marcarComoDescargado(idEvento);
                JOptionPane.showMessageDialog(view.getFrame(), "Reportaje descargado con éxito en:\n" + fileToSave.getAbsolutePath());
            } catch (java.io.IOException ex) {
                JOptionPane.showMessageDialog(view.getFrame(), "Error al guardar el archivo: " + ex.getMessage());
            }
        }
    }

    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "");
    }
}