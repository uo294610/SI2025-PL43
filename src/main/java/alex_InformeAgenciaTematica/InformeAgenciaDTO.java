package alex_InformeAgenciaTematica;

import java.util.ArrayList;
import java.util.List;

public class InformeAgenciaDTO {
    
    public static class EventoReporte {
        public int id;
        public String nombre;
        public double precio;
        public List<AccesoEmpresa> accesos = new ArrayList<>();
    }

    public static class AccesoEmpresa {
        public String nombreEmpresa;
        public String tipoAcceso;
        public String estadoPago;
        public boolean tieneTarifaPlana;
        public double cuotaMensual;
    }
}