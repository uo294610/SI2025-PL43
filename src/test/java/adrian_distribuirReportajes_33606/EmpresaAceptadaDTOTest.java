package adrian_distribuirReportajes_33606;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class EmpresaAceptadaDTOTest {

    @Test
    public void testCE1_1_TarifaPlanaPagada() {
        // Preparar (Arrange)
        EmpresaAceptadaDTO empresa = new EmpresaAceptadaDTO();
        empresa.setTieneTarifaPlana(1);
        empresa.setTarifaPlanaPagada(1);

        // Ejecutar (Act)
        String resultado = empresa.getEstadoPago();

        // Comprobar (Assert)
        assertEquals("PAGADO (T.Plana)", resultado, "Fallo en CE1.1: Debería estar pagado por tarifa plana.");
    }

    @Test
    public void testCE1_2_TarifaPlanaConDeuda() {
        // Preparar
        EmpresaAceptadaDTO empresa = new EmpresaAceptadaDTO();
        empresa.setTieneTarifaPlana(1);
        empresa.setTarifaPlanaPagada(0);

        // Ejecutar
        String resultado = empresa.getEstadoPago();

        // Comprobar
        assertEquals("DEUDA T.PLANA", resultado, "Fallo en CE1.2: Debería detectar la deuda de la tarifa plana.");
    }

    @Test
    public void testCE2_1_PagoIndividualPagado() {
        // Preparar
        EmpresaAceptadaDTO empresa = new EmpresaAceptadaDTO();
        empresa.setTieneTarifaPlana(0);
        empresa.setEstadoPagoIndividual("PAGADO");

        // Ejecutar
        String resultado = empresa.getEstadoPago();

        // Comprobar
        assertEquals("PAGADO", resultado, "Fallo en CE2.1: Debería detectar el pago individual abonado.");
    }

    @Test
    public void testCE2_2_PagoIndividualPendiente() {
        // Preparar
        EmpresaAceptadaDTO empresa = new EmpresaAceptadaDTO();
        empresa.setTieneTarifaPlana(0);
        empresa.setEstadoPagoIndividual("PENDIENTE");

        // Ejecutar
        String resultado = empresa.getEstadoPago();

        // Comprobar
        assertEquals("PENDIENTE", resultado, "Fallo en CE2.2: Debería detectar el pago individual pendiente.");
    }
}