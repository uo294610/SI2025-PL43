package diego_asignarReporteros_33602_PA;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import giis.demo.util.ApplicationException;
import giis.demo.util.Database;

/**
 * Pruebas unitarias de la Historia de Usuario: Finalizar Asignación de Reporteros.
 * Verifica la lógica de negocio que exige 1 Responsable y al menos 1 Base.
 */
public class TestFinalizarAsignacion {
	
	private static Database db = new Database();
	private ReporteroModel model;
	
	@BeforeEach
	public void setUp() {
		db.createDatabase(true);
		loadCleanDatabase(db);
		model = new ReporteroModel();
	}
	
	@AfterEach
	public void tearDown(){
	}
	
	public static void loadCleanDatabase(Database db) {
		db.executeBatch(new String[] {
				"delete from Asignacion",
				"delete from Evento",
				"delete from Reportero",
				"delete from AgenciaPrensa",
				"delete from Provincia",
				"delete from Pais",
				// Insertamos las dependencias mínimas para que no fallen las claves foráneas (FK)
				"insert into Pais (id, nombre, coste_manutencion) values (1, 'TestPais', 0)",
				"insert into Provincia (id, nombre, pais_id, coste_alojamiento) values (1, 'TestProv', 1, 0)",
				"insert into AgenciaPrensa (id, nombre) values (1, 'AgenciaTest')",
				
				// Insertamos 1 Evento genérico y 3 Reporteros genéricos
				"insert into Evento (id, nombre, fecha, fecha_fin, precio, agencia_id, provincia_id, asignacion_finalizada) values (999, 'Test Event', '2026-01-01', '2026-01-01', 0, 1, 1, false)",
				"insert into Reportero (id, nombre, tipo, provincia_id) values (1, 'Reportero 1', 'Básico', 1)",
				"insert into Reportero (id, nombre, tipo, provincia_id) values (2, 'Reportero 2', 'Básico', 1)",
				"insert into Reportero (id, nombre, tipo, provincia_id) values (3, 'Reportero 3', 'Básico', 1)"
			});
	}

	// /////////////////////////////////////////////////////////////
	// PRUEBAS DE LA REGLA DE NEGOCIO: 1 Responsable y >=1 Base
	// /////////////////////////////////////////////////////////////
	
	/**
	 * CP1 (Camino Feliz): 1 Responsable y 1 Base.
	 * Cubre CE2 y CE5.
	 * No debe lanzar excepción y debe marcar el evento como finalizado (TRUE).
	 */
	@Test
	public void testFinalizarAsignacionValida() {
		// Asignamos 1 responsable y 1 base
		db.executeBatch(new String[] {
				"insert into Asignacion(evento_id, reportero_id, rol) values (999, 1, 'Responsable')",
				"insert into Asignacion(evento_id, reportero_id, rol) values (999, 2, 'Base')"
		});
		
		// Ejecutamos la lógica de negocio
		model.validarYFinalizarAsignacion(999);
		
		// Comprobamos que el evento ahora está finalizado en BD
		List<Object[]> evento = db.executeQueryArray("SELECT asignacion_finalizada FROM Evento WHERE id = 999");
		assertTrue(evento.get(0)[0].toString().equals("true") || evento.get(0)[0].toString().equals("1"));
	}

	/**
	 * CP2 (Invalido): 0 Responsables (solo bases).
	 * Cubre CE1 y CE5.
	 * Debe lanzar ApplicationException indicando el error.
	 */
	@Test
	public void testFinalizarAsignacionInvalidaCeroResponsables() {
		db.executeBatch(new String[] {
				"insert into Asignacion(evento_id, reportero_id, rol) values (999, 1, 'Base')",
				"insert into Asignacion(evento_id, reportero_id, rol) values (999, 2, 'Base')"
		});
		
		ApplicationException exception = assertThrows(ApplicationException.class, () -> {
			model.validarYFinalizarAsignacion(999);
		});
		assertEquals("Debe haber exactamente 1 Responsable y al menos 1 Base.", exception.getMessage());
	}

	/**
	 * CP3 (Invalido): Exceso de Responsables (R=2, B=1).
	 * Cubre CE3 y CE5.
	 * Debe lanzar ApplicationException indicando el error.
	 */
	@Test
	public void testFinalizarAsignacionInvalidaExcesoResponsables() {
		db.executeBatch(new String[] {
				"insert into Asignacion(evento_id, reportero_id, rol) values (999, 1, 'Responsable')",
				"insert into Asignacion(evento_id, reportero_id, rol) values (999, 2, 'Responsable')",
				"insert into Asignacion(evento_id, reportero_id, rol) values (999, 3, 'Base')"
		});
		
		ApplicationException exception = assertThrows(ApplicationException.class, () -> {
			model.validarYFinalizarAsignacion(999);
		});
		assertEquals("Debe haber exactamente 1 Responsable y al menos 1 Base.", exception.getMessage());
	}

	/**
	 * CP4 (Invalido): Cero Bases (solo el responsable).
	 * Cubre CE2 y CE4.
	 * Debe lanzar ApplicationException indicando el error.
	 */
	@Test
	public void testFinalizarAsignacionInvalidaCeroBases() {
		db.executeBatch(new String[] {
				"insert into Asignacion(evento_id, reportero_id, rol) values (999, 1, 'Responsable')"
		});
		
		ApplicationException exception = assertThrows(ApplicationException.class, () -> {
			model.validarYFinalizarAsignacion(999);
		});
		assertEquals("Debe haber exactamente 1 Responsable y al menos 1 Base.", exception.getMessage());
	}
}