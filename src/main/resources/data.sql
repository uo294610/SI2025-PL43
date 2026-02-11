-- Datos para carga inicial de la base de datos

DELETE FROM Ofrecimiento;
DELETE FROM Asignacion;
DELETE FROM VersionReportaje;
DELETE FROM Reportaje;
DELETE FROM Evento;
DELETE FROM Reportero;
DELETE FROM EmpresaComunicacion;
DELETE FROM AgenciaPrensa;

-- 1. Agencias
INSERT INTO AgenciaPrensa (id, nombre) VALUES (1, 'Agencia EFE'), (2, 'Reuters');

-- 2. Reporteros
INSERT INTO Reportero (id, nombre, agencia_id) VALUES 
    (10, 'Diego Abella', 1), 
    (11, 'Nicolás Villalobos', 1), 
    (12, 'Adrián González', 1), 
    (13, 'Álex Álvarez', 2);

-- 3. Eventos
INSERT INTO Evento (id, nombre, fecha, agencia_id) VALUES 
    (100, 'Final Champions 2026', '2026-05-30', 1),
    (101, 'Gala de los Goya', '2026-02-15', 1),
    (102, 'Rueda de Prensa', '2026-02-15', 1);

-- 4. Asignaciones (HU 33602 / 33608)
INSERT INTO Asignacion (evento_id, reportero_id) VALUES (100, 10), (100, 11);

-- 5. Empresas de Comunicación
INSERT INTO EmpresaComunicacion (id, nombre) VALUES (200, 'Atresmedia'), (201, 'Mediaset');

-- 6. Reportajes (HU 33603)
INSERT INTO Reportaje (id, titulo, evento_id, reportero_entrega_id) VALUES 
    (300, 'Crónica de una final épica', 100, 10);

-- 7. Versiones (HU 33610 / 33612)
INSERT INTO VersionReportaje (id, reportaje_id, subtitulo, cuerpo, fecha_hora, que_cambio) VALUES 
    (400, 300, 'El Madrid levanta la 16ª', 'Contenido del reportaje...', '2026-05-30 23:30:00', 'Versión inicial');

-- 8. Ofrecimientos (HU 33605 / 33606)
INSERT INTO Ofrecimiento (id, evento_id, empresa_id, decision, acceso) VALUES 
    (500, 100, 200, 'ACEPTADO', TRUE),
    (501, 100, 201, NULL, FALSE);