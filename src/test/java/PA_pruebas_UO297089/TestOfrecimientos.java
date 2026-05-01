package PA_pruebas_UO297089;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import giis.demo.util.Database;
import alex_GestionarOfrecimientos.OfrecimientosModel;
import alex_GestionarOfrecimientos.OfrecimientosDTO;

public class TestOfrecimientos {
    
    private static Database db = new Database();
    
    @BeforeEach
    public void setUp() {
        db.createDatabase(true);
        loadCleanDatabase(db); 
    }
    
    public static void loadCleanDatabase(Database db) {
        db.executeBatch(new String[] {
            "DELETE FROM EvaluacionReportaje",
            "DELETE FROM Reportaje",
            "DELETE FROM Ofrecimiento",
            "DELETE FROM EmpresaTematica",
            "DELETE FROM EventoTematica",
            "DELETE FROM Evento",
            "DELETE FROM EmpresaComunicacion",
            "DELETE FROM Tematica",
            "DELETE FROM AgenciaPrensa",
            "DELETE FROM Provincia",
            "DELETE FROM Pais",
            
            // Catálogos base
            "INSERT INTO Pais(id, nombre, coste_manutencion) VALUES (1, 'España', 60.00)",
            "INSERT INTO Provincia(id, nombre, pais_id, coste_alojamiento) VALUES (1, 'Madrid', 1, 120.00)",
            "INSERT INTO AgenciaPrensa(id, nombre) VALUES (1, 'Agencia EFE')",
            "INSERT INTO Tematica(id, nombre) VALUES (1, 'Deportes'), (2, 'Entretenimiento')",
            
            // Empresa Atresmedia (Suscrita SOLO a Deportes)
            "INSERT INTO EmpresaComunicacion(id, nombre, acepta_embargos) VALUES (200, 'Atresmedia', TRUE)",
            "INSERT INTO EmpresaTematica(empresa_id, tematica_id) VALUES (200, 1)",
            
            // Eventos
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (100, 'Final Champions', '2026-05-30', '2026-05-30', 5000, 1, 1, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (101, 'Gala de los Goya', '2026-02-15', '2026-02-16', 3500, 1, 1, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (104, 'Estreno de Cine', '2026-03-01', '2026-03-01', 800, 1, 1, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (105, 'Mundial Basket', '2026-08-01', '2026-08-15', 6000, 1, 1, TRUE)",
            
            "INSERT INTO EventoTematica(evento_id, tematica_id) VALUES (100, 1), (101, 2), (104, 2), (105, 1)",
            
            // Reportaje embargado asociado al Evento 100
            "INSERT INTO Reportaje(id, titulo, reportero_entrega_id, estado, revision_solicitada, fecha_fin_embargo) VALUES (300, 'Crónica', 10, 'FINALIZADO', FALSE, '2026-12-31 23:59:59')",
            "INSERT INTO EvaluacionReportaje(id, reportaje_id, evento_id, estado) VALUES (700, 300, 100, 'ACEPTADO')",
            
            // --- OFRECIMIENTOS PARA PROBAR LOS 3 ESTADOS ---
            // 500: PENDIENTE, Deportes, 5000€, EMBARGADO (PARCIAL)
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (500, 100, 200, NULL, TRUE, 'PARCIAL', 'PENDIENTE')",
            // 501: PENDIENTE, Entretenimiento, 3500€, SIN EMBARGO (COMPLETO)
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (501, 101, 200, NULL, TRUE, 'COMPLETO', 'PENDIENTE')",
            // 502: ACEPTADO, Entretenimiento, 800€, SIN EMBARGO (COMPLETO)
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (502, 104, 200, 'ACEPTADO', TRUE, 'COMPLETO', 'PAGADO')",
            // 503: RECHAZADO, Deportes, 6000€, SIN EMBARGO (COMPLETO)
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (503, 105, 200, 'RECHAZADO', TRUE, 'COMPLETO', 'PENDIENTE')"
        });
    }

    /**
     * PRUEBA MAESTRA PARAMETRIZADA:
     * Verifica la devolución de datos e inspecciona internamente que los DTOs
     * cumplen con todas las restricciones matemáticas y lógicas solicitadas.
     */
    @ParameterizedTest(name = "Filtros -> SoloEmp:{0}, Tem:{1}, Pend:{2}, Min:{3}, Max:{4}, Embargo:{5} => Exp: {6} filas")
    @CsvSource({
        // SOLO_EMP, TEMA, PEND,  MIN,    MAX,    EMBARGO,       EXP_COUNT, EXP_ID
        "  false,    0,    true,  null,   null,   Todos,         2,         500 ", // 1. Base Pendientes (500, 501)
        "  false,    0,    false, null,   null,   Todos,         2,         502 ", // 2. Base Decididos (502, 503)
        "  false,    1,    true,  null,   null,   Todos,         1,         500 ", // 3. Temática Deportes Pendiente
        "  false,    2,    true,  null,   null,   Todos,         1,         501 ", // 4. Temática Entretenimiento Pendiente
        "  false,    0,    true,  4000.0, null,   Todos,         1,         500 ", // 5. Precio Mínimo > 4000
        "  false,    0,    true,  null,   4000.0, Todos,         1,         501 ", // 6. Precio Máximo < 4000
        "  false,    0,    true,  7000.0, 9000.0, Todos,         0,          -1 ", // 7. Rango sin coincidencias
        "  false,    0,    true,  null,   null,   Embargados,    1,         500 ", // 8. Solo embargados (Acceso Parcial)
        "  false,    0,    true,  null,   null,   Sin embargo,   1,         501 ", // 9. Solo libres (Acceso Completo)
        "  true,     0,    true,  null,   null,   Todos,         1,         500 "  // 10. Checkbox "Solo empresa" (Deportes)
    })
    public void testFiltrosConValidacionExhaustiva(boolean soloEmpresa, int idTematica, boolean soloPendientes, 
                                                   String minPrecioStr, String maxPrecioStr, String filtroEmbargo, 
                                                   int expectedCount, int expectedFirstId) {
        
        Double minPrecio = "null".equals(minPrecioStr.trim()) ? null : Double.valueOf(minPrecioStr.trim());
        Double maxPrecio = "null".equals(maxPrecioStr.trim()) ? null : Double.valueOf(maxPrecioStr.trim());

        OfrecimientosModel model = new OfrecimientosModel();
        
        List<OfrecimientosDTO> resultados = model.getOfrecimientos(
            200, soloEmpresa, idTematica, soloPendientes, minPrecio, maxPrecio, filtroEmbargo
        );

        // 1. Verificamos que traiga la cantidad correcta de elementos
        assertEquals(expectedCount, resultados.size(), 
            "El número de ofrecimientos devueltos no coincide.");

        if (expectedCount > 0) {
            boolean idEncontrado = false;
            for (OfrecimientosDTO dto : resultados) {
                if (dto.getId() == expectedFirstId) {
                    idEncontrado = true;
                    break;
                }
            }
            assertTrue(idEncontrado, "El ID esperado (" + expectedFirstId + ") no se encontró en la lista de resultados.");
        }

        // --- VALIDACIÓN EXHAUSTIVA DE POSTCONDICIONES ---
        for (OfrecimientosDTO dto : resultados) {
            
            // A. Validación del Estado (Pendiente vs Decidido)
            if (soloPendientes) {
                // Si la DB mapea null a String vacío, cambia esto a equals("") o isBlank()
                assertNull(dto.getDecision(), "Filtro roto: Evento decidido en pestaña pendientes. ID: " + dto.getId());
            } else {
                assertNotNull(dto.getDecision(), "Filtro roto: Evento pendiente en pestaña decididos. ID: " + dto.getId());
                boolean estadoValido = dto.getDecision().equals("ACEPTADO") || dto.getDecision().equals("RECHAZADO");
                assertTrue(estadoValido, "Estado de decisión anómalo en ID: " + dto.getId());
            }

            // B. Validación del Precio
            if (minPrecio != null) {
                assertTrue(dto.getPrecio() >= minPrecio, "Filtro roto: Precio menor que el mínimo exigido.");
            }
            if (maxPrecio != null) {
                assertTrue(dto.getPrecio() <= maxPrecio, "Filtro roto: Precio mayor que el máximo permitido.");
            }

            // C. Validación de Embargos y Nivel de Acceso
            if ("Embargados".equals(filtroEmbargo)) {
                assertEquals("PARCIAL", dto.getTipoAcceso(), "Filtro roto: Debe tener acceso PARCIAL.");
            } else if ("Sin embargo".equals(filtroEmbargo)) {
                assertEquals("COMPLETO", dto.getTipoAcceso(), "Filtro roto: Debe tener acceso COMPLETO.");
            }
        }

    }
}
   