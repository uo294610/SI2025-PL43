DELETE FROM Imagen;
DELETE FROM Ofrecimiento;
DELETE FROM Asignacion;
DELETE FROM VersionReportaje;
DELETE FROM EvaluacionReportaje; 
DELETE FROM Reportaje;
DELETE FROM Evento;
DELETE FROM Reportero;
DELETE FROM EmpresaComunicacion;
DELETE FROM AgenciaPrensa;
DELETE FROM Tematica;

INSERT INTO Tematica (id, nombre) VALUES 
    (1, 'Deportes'),
    (2, 'Entretenimiento'),
    (3, 'Política'),
    (4, 'Tecnología');

INSERT INTO AgenciaPrensa (id, nombre) VALUES 
    (1, 'Agencia EFE'), 
    (2, 'Reuters'),
    (3, 'Associated Press'),
    (4, 'Europa Press');

INSERT INTO Reportero (id, nombre, agencia_id, tipo, tematica_id) VALUES 
    (10, 'Diego Abella', 1, 'Básico', 1), 
    (11, 'Nicolás Villalobos', 1, 'Reportero gráfico', 1), 
    (12, 'Adrián González', 1, 'Camarógrafo', 2), 
    (13, 'Lucía Gómez', 1, 'Básico', 3),
    (14, 'Carlos Sainz', NULL, 'Reportero gráfico', 1), 
    (15, 'Ana Belén', 1, 'Básico', 2),
    (16, 'Álex Álvarez', 2, 'Básico', 3),
    (17, 'Jane Smith', 2, 'Camarógrafo', 4),
    (18, 'John Doe', NULL, 'Reportero gráfico', 4), 
    (19, 'Michael Scott', 3, 'Básico', 1),
    (20, 'Pam Beesly', 3, 'Reportero gráfico', 2),
    (21, 'Laura Marcos', 4, 'Básico', 3),
    (22, 'David Bisbal', NULL, 'Camarógrafo', 2); 

INSERT INTO Evento (id, nombre, fecha, precio, agencia_id, tematica_id) VALUES 
    (100, 'Final Champions 2026', '2026-05-30', 5000.00, 1, 1),
    (101, 'Gala de los Goya', '2026-02-15', 3500.00, 1, 2),
    (102, 'Rueda de Prensa Gobierno', '2026-02-15', 500.00, 1, 3),
    (103, 'Debate Electoral', '2026-03-01', 1200.00, 1, 3),    
    (104, 'Estreno de Cine', '2026-03-01', 800.00, 1, 2),      
    (105, 'Manifestación Centro', '2026-03-05', 300.00, 1, 3),  
    (106, 'Cumbre de la ONU', '2026-04-10', 4500.00, 2, 3),
    (107, 'Feria Tecnológica MWC', '2026-04-12', 2000.00, 2, 4), 
    (108, 'Super Bowl', '2026-02-08', 8000.00, 3, 1),
    (109, 'Premios Oscars', '2026-03-08', 6500.00, 3, 2);        

INSERT INTO Asignacion (evento_id, reportero_id) VALUES 
    (100, 10), 
    (100, 11),
    (101, 12),
    (102, 13),
    (106, 16),
    (106, 17),
    (108, 19);

INSERT INTO EmpresaComunicacion (id, nombre, tematica_id) VALUES 
    (200, 'Atresmedia', 2), 
    (201, 'Mediaset', 2),
    (202, 'RTVE', 3),
    (203, 'Grupo Prisa', 3),
    (204, 'Unidad Editorial', 1);

INSERT INTO Reportaje (id, titulo, reportero_entrega_id) VALUES 
    (300, 'Crónica de una final épica', 10),
    (301, 'Glamour en la alfombra roja', 12),
    (302, 'Decisiones clave en la cumbre', 16);

INSERT INTO EvaluacionReportaje (id, reportaje_id, evento_id, estado) VALUES
    (700, 300, 100, 'aceptado'),
    (701, 301, 101, 'dudoso'),
    (702, 302, 106, 'rechazado');

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

INSERT INTO Imagen (id, reportero_id, ruta_archivo, estado, tipo) VALUES
    (600, 11, '/img/champions_final_01.jpg', 'DEFINITIVA', 'imagen'),
    (601, 11, '/img/champions_final_02_raw.jpg', 'BORRADOR', 'imagen'),
    (602, 14, '/img/sainz_freelance_foto.jpg', 'DEFINITIVA', 'imagen'),
    (603, 20, '/img/oscars_redcarpet.png', 'BORRADOR', 'imagen'),
    (604, 12, '/video/goya_entrevistas.mp4', 'DEFINITIVA', 'video'), 
    (605, 17, '/video/onu_declaraciones_raw.avi', 'BORRADOR', 'video'); 