package PA_pruebas_UO297089;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    
    @AfterEach
    public void tearDown() {
    }
    
    public static void loadCleanDatabase(Database db) {
        db.executeBatch(new String[] {
            "DELETE FROM Ofrecimiento",
            "DELETE FROM EventoTematica",
            "DELETE FROM Evento",
            "DELETE FROM EmpresaComunicacion",
            "DELETE FROM Tematica",
            "DELETE FROM AgenciaPrensa",
            "DELETE FROM Provincia",
            "DELETE FROM Pais",
            
            "INSERT INTO Pais(id, nombre, coste_manutencion) VALUES (1, 'España', 60.00)",
            "INSERT INTO Provincia(id, nombre, pais_id, coste_alojamiento) VALUES (1, 'Madrid', 1, 120.00)",
            "INSERT INTO AgenciaPrensa(id, nombre) VALUES (1, 'Agencia EFE')",
            
            "INSERT INTO Tematica(id, nombre) VALUES (1, 'Deportes'), (2, 'Entretenimiento')",
            "INSERT INTO EmpresaComunicacion(id, nombre, acepta_embargos) VALUES (200, 'Atresmedia', TRUE)",
            
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (100, 'Final Champions', '2026-05-30', '2026-05-30', 5000, 1, 1, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (101, 'Gala de los Goya', '2026-02-15', '2026-02-16', 3500, 1, 1, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (104, 'Estreno de Cine', '2026-03-01', '2026-03-01', 800, 1, 1, TRUE)",
            
            "INSERT INTO EventoTematica(evento_id, tematica_id) VALUES (100, 1), (101, 2), (104, 2)",
            
            // Ofrecimiento 500: PENDIENTE y COMPLETO (Temática Deportes - 1)
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (500, 100, 200, NULL, TRUE, 'COMPLETO', 'PENDIENTE')",
            
            // Ofrecimiento 501: PENDIENTE y PARCIAL (Temática Entretenimiento - 2)
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (501, 101, 200, NULL, TRUE, 'PARCIAL', 'PENDIENTE')",
            
            // Ofrecimiento 502: YA DECIDIDO y COMPLETO (Temática Entretenimiento - 2)
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (502, 104, 200, 'ACEPTADO', TRUE, 'COMPLETO', 'PAGADO')"
        });
    }

    @Test
    public void testFlujoCompletoSeleccionYDecision() {
        OfrecimientosModel model = new OfrecimientosModel();
        int idEmpresa = 200;

        List<OfrecimientosDTO> listaPendientes = model.getOfrecimientos(idEmpresa, false, 0, true, null, null, "Todos");
        assertEquals(2, listaPendientes.size(), "Debe haber 2 ofrecimientos pendientes iniciales (500 y 501).");

        model.actualizarDecision(501, "ACEPTADO");

        assertEquals(1, model.getOfrecimientos(idEmpresa, false, 0, true, null, null, "Todos").size(), "Solo debe quedar 1 pendiente.");
        assertEquals(2, model.getOfrecimientos(idEmpresa, false, 0, false, null, null, "Todos").size(), "Debe haber 2 decididos.");
    }

    @Test
    public void testFiltrosDinamicos() {
        OfrecimientosModel model = new OfrecimientosModel();
        int idEmpresa = 200;

        // Filtro por temática Deportes (ID 1) -> Debe salir el Ofrecimiento 500 (Final Champions)
        List<OfrecimientosDTO> filtroDeportes = model.getOfrecimientos(idEmpresa, true, 1, true, null, null, "Todos");
        assertEquals(1, filtroDeportes.size(), "Debería salir 1 evento de Deportes pendiente.");

        // Filtro por Precio Mínimo (ej. > 4000€) -> Debe salir solo la Final Champions (5000€)
        List<OfrecimientosDTO> filtroPrecioMin = model.getOfrecimientos(idEmpresa, false, 0, true, 4000.0, null, "Todos");
        assertAll("Filtro Precio Mínimo",
            () -> assertEquals(1, filtroPrecioMin.size(), "Debería salir solo la Final Champions"),
            () -> assertEquals(5000.0, filtroPrecioMin.get(0).getPrecio(), "El precio debe coincidir")
        );

        // Filtro por Precio Máximo (ej. < 1000€) -> No debe haber pendientes (El evento 104 de 800€ ya está ACEPTADO)
        List<OfrecimientosDTO> filtroPrecioMax = model.getOfrecimientos(idEmpresa, false, 0, true, null, 1000.0, "Todos");
        assertEquals(0, filtroPrecioMax.size(), "No debería haber eventos pendientes por debajo de 1000€.");
        
        // Filtro por Precio Rango (ej. 3000€ - 4000€) -> Debe salir la Gala de los Goya (3500€)
        List<OfrecimientosDTO> filtroRango = model.getOfrecimientos(idEmpresa, false, 0, true, 3000.0, 4000.0, "Todos");
        assertEquals(1, filtroRango.size(), "Debería salir solo la Gala de los Goya.");
    }
}