package giis.demo.tkrun.ut;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import giis.demo.util.Database;
import alex_DietasReportero.DietasModel;
import alex_DietasReportero.DietaDTO;

public class TestDietasParametrizado {
    
    private static Database db = new Database();
    
    @BeforeEach
    public void setUp() {
        db.createDatabase(true);
        loadCleanDatabase(db); 
    }
    
    public static void loadCleanDatabase(Database db) {
        db.executeBatch(new String[] {
            "DELETE FROM Asignacion",
            "DELETE FROM Evento",
            "DELETE FROM Reportero",
            "DELETE FROM Provincia",
            "DELETE FROM Pais",
            "DELETE FROM AgenciaPrensa",
            
            "INSERT INTO AgenciaPrensa(id, nombre) VALUES (1, 'Agencia EFE')",
            
            "INSERT INTO Pais(id, nombre, coste_manutencion) VALUES (1, 'España', 60.00)",
            "INSERT INTO Pais(id, nombre, coste_manutencion) VALUES (2, 'Francia', 100.00)",
            "INSERT INTO Pais(id, nombre, coste_manutencion) VALUES (4, 'Filipinas', 115.00)",
            
            "INSERT INTO Provincia(id, nombre, pais_id, coste_alojamiento) VALUES (1, 'Asturias', 1, 80.00)",
            "INSERT INTO Provincia(id, nombre, pais_id, coste_alojamiento) VALUES (2, 'Madrid', 1, 120.00)",
            "INSERT INTO Provincia(id, nombre, pais_id, coste_alojamiento) VALUES (6, 'Paris', 2, 200.00)",
            "INSERT INTO Provincia(id, nombre, pais_id, coste_alojamiento) VALUES (11, 'Asturias (Filipinas)', 4, 190.00)",
            
            "INSERT INTO Reportero(id, nombre, agencia_id, tipo, provincia_id) VALUES (10, 'Diego Abella', 1, 'Básico', 1)",
            
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (200, 'Evento Local', '2026-05-01', '2026-05-01', 0, 1, 1, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (201, 'Viaje Express', '2026-05-02', '2026-05-02', 0, 1, 2, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (202, 'Cobertura Nacional', '2026-06-10', '2026-06-12', 0, 1, 2, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (203, 'Cobertura Internacional', '2026-07-20', '2026-07-24', 0, 1, 6, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (204, 'Salto de Mes', '2026-08-30', '2026-09-02', 0, 1, 2, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (205, 'Bisiesto', '2024-02-28', '2024-03-01', 0, 1, 2, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (206, 'Asturias Filipinas', '2026-10-10', '2026-10-10', 0, 1, 11, TRUE)",

            "INSERT INTO Asignacion(evento_id, reportero_id, rol) VALUES (200, 10, 'Responsable'), (201, 10, 'Responsable'), (202, 10, 'Responsable'), (203, 10, 'Responsable'), (204, 10, 'Responsable'), (205, 10, 'Responsable'), (206, 10, 'Responsable')"
        });
    }

    @ParameterizedTest
    @CsvSource({ 
        "200, 1, false,  60.0,    0.0,   60.0",
        "201, 1, true,   60.0,  120.0,  180.0",
        "202, 3, true,  180.0,  360.0,  540.0",
        "203, 5, true,  500.0, 1000.0, 1500.0",
        "204, 4, true,  240.0,  480.0,  720.0",
        "205, 3, true,  180.0,  360.0,  540.0",
        "206, 1, true,  115.0,  190.0,  305.0" 
    })
    public void testMatematicasYGeografiaDietas(int idEventoTarget, int expDias, boolean expAplicaAloj, 
                                                double expManutencion, double expAlojamiento, double expTotal) {
        
        DietasModel model = new DietasModel();
        List<DietaDTO> listaDietas = model.getDietasEventos(10, 1);
        
        DietaDTO dtoPrueba = null;
        for (DietaDTO dto : listaDietas) {
            if (dto.getEventoId() == idEventoTarget) {
                dtoPrueba = dto;
                break;
            }
        }
        
        assertNotNull(dtoPrueba, "El evento " + idEventoTarget + " no se encontro.");
        
        final DietaDTO resultado = dtoPrueba; 
        assertAll("Calculos del Evento " + idEventoTarget,
            () -> assertEquals(expDias, resultado.getDias(), "Fallo en el calculo de dias"),
            () -> assertEquals(expAplicaAloj, resultado.isAplicaAlojamiento(), "Fallo en condicional de alojamiento"),
            () -> assertEquals(expManutencion, resultado.getTotalManutencion(), "Fallo en Manutencion"),
            () -> assertEquals(expAlojamiento, resultado.getTotalAlojamiento(), "Fallo en Alojamiento"),
            () -> assertEquals(expTotal, resultado.getTotalApercibir(), "Fallo en el Total")
        );
    }
}