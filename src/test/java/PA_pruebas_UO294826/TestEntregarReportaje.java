package PA_pruebas_UO294826;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import giis.demo.util.Database;
import nico_ModificaEntrega_33610.ModificaEntregaModel;

public class TestEntregarReportaje {

    private Database db;
    private ModificaEntregaModel model;

    @BeforeEach
    public void setUp() {
        db = new Database();
        model = new ModificaEntregaModel();

        // 1. Limpiamos por si quedó algún dato colgado
        limpiarDatosPrueba();

        // 2. Insertamos Eventos de prueba con IDs altos (9900+)
        // Usamos la Agencia 1 (Agencia EFE) y Provincia 1 (Madrid) que SÍ existen en tu BD real
        db.executeUpdate("INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (9901, 'Evento Test 1', '2026-01-01', '2026-01-01', 100, 1, 1, TRUE)");
        db.executeUpdate("INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (9902, 'Evento Test 2', '2026-01-02', '2026-01-02', 100, 1, 1, TRUE)");
        db.executeUpdate("INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (9903, 'Evento Test 3', '2026-01-03', '2026-01-03', 100, 1, 1, TRUE)");

        // Asignamos al Reportero 10 (Diego Abella) a dos de esos eventos
        db.executeUpdate("INSERT INTO Asignacion (evento_id, reportero_id, rol) VALUES (9901, 10, 'Base')");
        db.executeUpdate("INSERT INTO Asignacion (evento_id, reportero_id, rol) VALUES (9903, 10, 'Base')");
        
        // Simulamos que el Reportero 10 ya entregó un reportaje previamente para el evento 9903
        db.executeUpdate("INSERT INTO Reportaje (id, titulo, reportero_entrega_id, estado) VALUES (9999, 'Reportaje Viejo', 10, 'EN_CURSO')");
        
        // Faltaba añadir el 'id' (9999) en esta tabla porque es NOT NULL
        db.executeUpdate("INSERT INTO EvaluacionReportaje (id, reportaje_id, evento_id, estado) VALUES (9999, 9999, 9903, 'PENDIENTE')");
    }

    @AfterEach
    public void tearDown() {
        limpiarDatosPrueba();
    }

    private void limpiarDatosPrueba() {
        db.executeUpdate("DELETE FROM VersionReportaje WHERE reportaje_id >= 9900");
        db.executeUpdate("DELETE FROM EvaluacionReportaje WHERE evento_id >= 9900");
        db.executeUpdate("DELETE FROM Reportaje WHERE id >= 9900");
        db.executeUpdate("DELETE FROM Asignacion WHERE evento_id >= 9900");
        db.executeUpdate("DELETE FROM Evento WHERE id >= 9900");
    }

    @Test
    public void testCP1_ExitoEnLaEntrega() {
        // CP1: Asignado = Verdadero (9901), Entregado Previamente = Falso
        model.validarYEntregarReportaje(9901, 10, "Título Éxito", "Subtítulo", "Cuerpo");
        
        List<Object[]> result = db.executeQueryArray("SELECT count(*) FROM EvaluacionReportaje WHERE evento_id = 9901");
        assertEquals("1", result.get(0)[0].toString(), "Debe haberse creado la relación en EvaluacionReportaje.");
    }

    @Test
    public void testCP2_ErrorPorNoEstarAsignado() {
        // CP2: Asignado = Falso (9902), Entregado Previamente = Falso
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            model.validarYEntregarReportaje(9902, 10, "Título Fallo", "Sub", "Cuerpo");
        });
        
        assertEquals("El reportero no está asignado a este evento.", exception.getMessage());
    }

    @Test
    public void testCP3_ErrorPorReportajeDuplicado() {
        // CP3: Asignado = Verdadero (9903), Entregado Previamente = Verdadero
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            model.validarYEntregarReportaje(9903, 10, "Nuevo Intento", "Sub", "Cuerpo");
        });
        
        assertEquals("El reportero ya ha entregado un reportaje para este evento.", exception.getMessage());
    }
}