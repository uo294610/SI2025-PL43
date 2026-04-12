package adrian_distribuirReportajes_33606;

public class EmpresaAceptadaDTO {
	private String id;
	private String nombreEmpresa;
	private int descargadoValor; 
	
	// NUEVO SPRINT 3
	private int tieneTarifaPlana;
	private int tarifaPlanaPagada;
	private String estadoPagoIndividual;
	private String tipoAccesoActual;

	public EmpresaAceptadaDTO() {}

	public int getDescargadoValor() { return descargadoValor; }
	public void setDescargadoValor(int descargadoValor) { this.descargadoValor = descargadoValor; }
	public String getDescargadoTexto() { return (descargadoValor == 1) ? "SÍ" : "NO"; }

	// Lógica de visualización para la tabla
	public String getEstadoPago() {
		if (tieneTarifaPlana == 1) {
			return (tarifaPlanaPagada == 1) ? "PAGADO (T.Plana)" : "DEUDA T.PLANA";
		} else {
			return estadoPagoIndividual; // PAGADO o PENDIENTE
		}
	}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getNombreEmpresa() { return nombreEmpresa; }
	public void setNombreEmpresa(String nombreEmpresa) { this.nombreEmpresa = nombreEmpresa; }

	public int getTieneTarifaPlana() { return tieneTarifaPlana; }
	public void setTieneTarifaPlana(int tieneTarifaPlana) { this.tieneTarifaPlana = tieneTarifaPlana; }
	public int getTarifaPlanaPagada() { return tarifaPlanaPagada; }
	public void setTarifaPlanaPagada(int tarifaPlanaPagada) { this.tarifaPlanaPagada = tarifaPlanaPagada; }
	public String getEstadoPagoIndividual() { return estadoPagoIndividual; }
	public void setEstadoPagoIndividual(String estadoPagoIndividual) { this.estadoPagoIndividual = estadoPagoIndividual; }
	public String getTipoAccesoActual() { return tipoAccesoActual; }
	public void setTipoAccesoActual(String tipoAccesoActual) { this.tipoAccesoActual = tipoAccesoActual; }
}