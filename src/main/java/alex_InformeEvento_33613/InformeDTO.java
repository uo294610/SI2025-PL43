package alex_InformeEvento_33613;

import java.util.ArrayList;
import java.util.List;

public class InformeDTO {
	private List<String> reporterosAsignados = new ArrayList<>();
    private boolean tieneReportaje = false;
    private String reporteroEntrega = "";
    private List<String> empresasConAcceso = new ArrayList<>();

    // Getters y Setters
    public List<String> getReporterosAsignados() { return reporterosAsignados; }
    public void setReporterosAsignados(List<String> r) { this.reporterosAsignados = r; }
    public boolean isTieneReportaje() { return tieneReportaje; }
    public void setTieneReportaje(boolean t) { this.tieneReportaje = t; }
    public String getReporteroEntrega() { return reporteroEntrega; }
    public void setReporteroEntrega(String r) { this.reporteroEntrega = r; }
    public List<String> getEmpresasConAcceso() { return empresasConAcceso; }
    public void setEmpresasConAcceso(List<String> e) { this.empresasConAcceso = e; }
}

