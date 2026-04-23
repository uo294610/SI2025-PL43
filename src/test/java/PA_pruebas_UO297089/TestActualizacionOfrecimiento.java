package PA_pruebas_UO297089;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import giis.demo.util.Database;
import giis.demo.util.Util;
import alex_GestionarOfrecimientos.OfrecimientosModel;

public class TestActualizacionOfrecimiento {
    
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
            
            "INSERT INTO Pais(id, nombre, coste_manutencion) VALUES (1, 'España', 0)",
            "INSERT INTO Provincia(id, nombre, pais_id, coste_alojamiento) VALUES (1, 'Madrid', 1, 0)",
            "INSERT INTO AgenciaPrensa(id, nombre) VALUES (1, 'Agencia')",
            "INSERT INTO Tematica(id, nombre) VALUES (1, 'General')",
            // Añadido el campo acepta_embargos de tu esquema
            "INSERT INTO EmpresaComunicacion(id, nombre, acepta_embargos) VALUES (200, 'Empresa', TRUE)",
            
            // Añadido el campo asignacion_finalizada de tu esquema
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (100, 'Ev 1', '2026-01-01', '2026-01-01', 100, 1, 1, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (101, 'Ev 2', '2026-01-02', '2026-01-02', 200, 1, 1, TRUE)",
            "INSERT INTO Evento(id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) VALUES (104, 'Ev 3', '2026-01-03', '2026-01-03', 300, 1, 1, TRUE)",
            "INSERT INTO EventoTematica(evento_id, tematica_id) VALUES (100, 1), (101, 1), (104, 1)",
            
            // Los 3 ofrecimientos base
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (500, 100, 200, NULL, 1, 'COMPLETO', 'PENDIENTE')",
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (501, 101, 200, NULL, 1, 'PARCIAL', 'PENDIENTE')",
            "INSERT INTO Ofrecimiento(id, evento_id, empresa_id, decision, acceso, tipo_acceso, estado_pago) VALUES (502, 104, 200, 'ACEPTADO', 1, 'COMPLETO', 'PAGADO')"
        });
    }

    @Test
    public void testActualizacionQuirurgicaPorCSV() {
        OfrecimientosModel model = new OfrecimientosModel();
        
        // Actualizamos solo el ofrecimiento 501
        model.actualizarDecision(501, "ACEPTADO");
        
        List<OfrecimientoEntityTest> tablaCompleta = db.executeQueryPojo(OfrecimientoEntityTest.class, "SELECT * FROM Ofrecimiento ORDER BY id");
        
        // Validamos que el 500 y el 502 siguen exactamente igual, y el 501 ha cambiado
        assertEquals("""
                500,100,200,,true,COMPLETO,PENDIENTE
                501,101,200,ACEPTADO,true,PARCIAL,PENDIENTE
                502,104,200,ACEPTADO,true,COMPLETO,PAGADO
                """, 
                Util.pojosToCsv(tablaCompleta, new String[] {"id", "evento_id", "empresa_id", "decision", "acceso", "tipo_acceso", "estado_pago"}));
    }
    
    @Test
    public void testActualizacionQuirurgicaPorMemoria() {
        // Sacamos la foto inicial
        List<OfrecimientoEntityTest> expected = db.executeQueryPojo(OfrecimientoEntityTest.class, "SELECT * FROM Ofrecimiento ORDER BY id");
        
        // Modificamos manualmente en la lista de Java el ofrecimiento 502 (índice 2) a RECHAZADO
        expected.get(2).setDecision("RECHAZADO");
        
        // Hacemos el cambio real en el sistema
        OfrecimientosModel model = new OfrecimientosModel();
        model.actualizarDecision(502, "RECHAZADO");
        
        // Sacamos la foto final de la BD
        List<OfrecimientoEntityTest> actual = db.executeQueryPojo(OfrecimientoEntityTest.class, "SELECT * FROM Ofrecimiento ORDER BY id");
        
        // Comparamos
        assertEquals(
                Util.pojosToCsv(expected, new String[] {"id", "evento_id", "empresa_id", "decision", "acceso", "tipo_acceso", "estado_pago"}),
                Util.pojosToCsv(actual, new String[] {"id", "evento_id", "empresa_id", "decision", "acceso", "tipo_acceso", "estado_pago"})
        );
    }

    // Entidad interna solo para este test (Mapea exactamente tu tabla)
    public static class OfrecimientoEntityTest {
        private int id;
        private int evento_id;
        private int empresa_id;
        private String decision;
        private boolean acceso;
        private String tipo_acceso;
        private String estado_pago;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public int getEvento_id() { return evento_id; }
        public void setEvento_id(int evento_id) { this.evento_id = evento_id; }
        public int getEmpresa_id() { return empresa_id; }
        public void setEmpresa_id(int empresa_id) { this.empresa_id = empresa_id; }
        public String getDecision() { return decision; }
        public void setDecision(String decision) { this.decision = decision; }
        public boolean isAcceso() { return acceso; }
        public void setAcceso(boolean acceso) { this.acceso = acceso; }
        public String getTipo_acceso() { return tipo_acceso; }
        public void setTipo_acceso(String tipo_acceso) { this.tipo_acceso = tipo_acceso; }
        public String getEstado_pago() { return estado_pago; }
        public void setEstado_pago(String estado_pago) { this.estado_pago = estado_pago; }
    }
}