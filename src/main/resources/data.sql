
DELETE FROM Ofrecimiento;
DELETE FROM Asignacion;
DELETE FROM VersionReportaje;
DELETE FROM Reportaje;
DELETE FROM Evento;
DELETE FROM Reportero;
DELETE FROM EmpresaComunicacion;
DELETE FROM AgenciaPrensa;


INSERT INTO AgenciaPrensa (id, nombre) VALUES 
    (1, 'Agencia EFE'), 
    (2, 'Reuters'),
    (3, 'Associated Press'),
    (4, 'Europa Press');

INSERT INTO Reportero (id, nombre, agencia_id) VALUES 
    (10, 'Diego Abella', 1), 
    (11, 'Nicolás Villalobos', 1), 
    (12, 'Adrián González', 1), 
    (13, 'Lucía Gómez', 1),
    (14, 'Carlos Sainz', 1),
    (15, 'Ana Belén', 1),
    (16, 'Álex Álvarez', 2),
    (17, 'Jane Smith', 2),
    (18, 'John Doe', 2),
    (19, 'Michael Scott', 3),
    (20, 'Pam Beesly', 3),
    (21, 'Laura Marcos', 4),
    (22, 'David Bisbal', 4);


INSERT INTO Evento (id, nombre, fecha, agencia_id) VALUES 
    (100, 'Final Champions 2026', '2026-05-30', 1),
    (101, 'Gala de los Goya', '2026-02-15', 1),
    (102, 'Rueda de Prensa Gobierno', '2026-02-15', 1),
    (103, 'Debate Electoral', '2026-03-01', 1),    
    (104, 'Estreno de Cine', '2026-03-01', 1),      
    (105, 'Manifestación Centro', '2026-03-05', 1),  
    (106, 'Cumbre de la ONU', '2026-04-10', 2),
    (107, 'Feria Tecnológica MWC', '2026-04-12', 2), 
    (108, 'Super Bowl', '2026-02-08', 3),
    (109, 'Premios Oscars', '2026-03-08', 3);        


INSERT INTO Asignacion (evento_id, reportero_id) VALUES 
    (100, 10), 
    (100, 11),
    (101, 12),
    (102, 13),
    (106, 16),
    (106, 17),
    (108, 19);

INSERT INTO EmpresaComunicacion (id, nombre) VALUES 
    (200, 'Atresmedia'), 
    (201, 'Mediaset'),
    (202, 'RTVE'),
    (203, 'Grupo Prisa'),
    (204, 'Unidad Editorial');

INSERT INTO Reportaje (id, titulo, evento_id, reportero_entrega_id) VALUES 
    (300, 'Crónica de una final épica', 100, 10),
    (301, 'Glamour en la alfombra roja', 101, 12),
    (302, 'Decisiones clave en la cumbre', 106, 16);


INSERT INTO VersionReportaje (id, reportaje_id, subtitulo, cuerpo, fecha_hora, que_cambio) VALUES 
    (400, 300, 'El Madrid levanta la 16ª', 'Contenido del reportaje final...', '2026-05-30 23:30:00', 'Versión inicial'),
    (401, 301, 'Los premiados de la noche', 'Contenido de los Goya...', '2026-02-16 01:00:00', 'Versión inicial'),
    (402, 301, 'Los premiados de la noche (Actualizado)', 'Contenido con la lista completa...', '2026-02-16 02:30:00', 'Se añade el premio a mejor director'),
    (403, 302, 'Acuerdos climáticos', 'Contenido de la ONU...', '2026-04-10 18:00:00', 'Versión inicial');


INSERT INTO Ofrecimiento (id, evento_id, empresa_id, decision, acceso) VALUES 
    (500, 100, 200, 'ACEPTADO', TRUE),
    (501, 100, 201, NULL, FALSE),
    (502, 101, 202, 'ACEPTADO', TRUE),
    (503, 101, 203, 'RECHAZADO', FALSE),
    (504, 106, 204, 'ACEPTADO', TRUE);
