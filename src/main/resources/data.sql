DELETE FROM Comentario;
DELETE FROM Revision;
DELETE FROM Imagen;
DELETE FROM Ofrecimiento;
DELETE FROM Asignacion;
DELETE FROM VersionReportaje;
DELETE FROM EvaluacionReportaje; 
DELETE FROM ReportajeTematica;
DELETE FROM Reportaje;
DELETE FROM EventoTematica;
DELETE FROM Evento;
DELETE FROM ReporteroTematica;
DELETE FROM Reportero;
DELETE FROM EmpresaTematica; 
DELETE FROM EmpresaComunicacion;
DELETE FROM AgenciaPrensa;
DELETE FROM Tematica;
DELETE FROM InteresFreelance;

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

INSERT INTO EmpresaComunicacion (id, nombre) VALUES 
    (200, 'Atresmedia'), 
    (201, 'Mediaset'),
    (202, 'RTVE'),
    (203, 'Grupo Prisa'),
    (204, 'Unidad Editorial');

INSERT INTO EmpresaTematica (empresa_id, tematica_id) VALUES
    (200, 2), 
    (200, 3), 
    (201, 2), 
    (202, 1), 
    (202, 3), 
    (202, 4), 
    (203, 3), 
    (204, 1); 

INSERT INTO Reportero (id, nombre, agencia_id, tipo) VALUES 
    (10, 'Diego Abella', 1, 'Básico'), 
    (11, 'Nicolás Villalobos', 1, 'Reportero gráfico'), 
    (12, 'Adrián González', 1, 'Camarógrafo'), 
    (13, 'Lucía Gómez', 1, 'Básico'),
    (14, 'Carlos Sainz', NULL, 'Reportero gráfico'), 
    (15, 'Ana Belén', 1, 'Básico'),
    (16, 'Álex Álvarez', 2, 'Básico'),
    (17, 'Jane Smith', 2, 'Camarógrafo'),
    (18, 'John Doe', NULL, 'Reportero gráfico'), 
    (19, 'Michael Scott', 3, 'Básico'),
    (20, 'Pam Beesly', 3, 'Reportero gráfico'),
    (21, 'Laura Marcos', 4, 'Básico'),
    (22, 'David Bisbal', NULL, 'Camarógrafo'),
    (23, 'Sara Carbonero', 2, 'Básico'), 
    (24, 'Ibai Llanos', NULL, 'Camarógrafo');

INSERT INTO ReporteroTematica (reportero_id, tematica_id) VALUES
    (10, 1),
    (11, 1),
    (11, 2),
    (12, 2),
    (13, 3),
    (14, 1),
    (15, 2),
    (16, 3),
    (17, 4),
    (18, 4),
    (18, 1),
    (19, 1),
    (20, 2),
    (21, 3),
    (22, 2),
    (23, 1),
    (23, 2),
    (24, 4),
    (24, 2);

INSERT INTO Evento (id, nombre, fecha, precio, agencia_id) VALUES 
    (100, 'Final Champions 2026', '2026-05-30', 5000.00, 1),
    (101, 'Gala de los Goya', '2026-02-15', 3500.00, 1),
    (102, 'Rueda de Prensa Gobierno', '2026-02-15', 500.00, 1),
    (103, 'Debate Electoral', '2026-03-01', 1200.00, 1),    
    (104, 'Estreno de Cine', '2026-03-01', 800.00, 1),      
    (105, 'Manifestación Centro', '2026-03-05', 300.00, 1),  
    (106, 'Cumbre de la ONU', '2026-04-10', 4500.00, 2),
    (107, 'Feria Tecnológica MWC', '2026-04-12', 2000.00, 2), 
    (108, 'Super Bowl', '2026-02-08', 8000.00, 3),
    (109, 'Premios Oscars', '2026-03-08', 6500.00, 3),
    (110, 'Congreso de Ciberseguridad', '2026-05-15', 1500.00, 4),
    (111, 'Juegos Olímpicos 2026', '2026-07-20', 10000.00, 1);

INSERT INTO EventoTematica (evento_id, tematica_id) VALUES
    (100, 1),
    (101, 2),
    (102, 3),
    (103, 3),
    (104, 2),
    (105, 3),
    (106, 3),
    (106, 1), 
    (107, 4),
    (108, 1),
    (108, 2), 
    (109, 2),
    (110, 4),
    (111, 1), 
    (111, 2);

INSERT INTO Asignacion (evento_id, reportero_id) VALUES 
    (100, 10), 
    (100, 11),
    (101, 12),
    (102, 13),
    (104, 15),
    (106, 16),
    (106, 17),
    (108, 19),
    (110, 21);

INSERT INTO Reportaje (id, titulo, reportero_entrega_id, estado, revision_solicitada) VALUES 
    (300, 'Crónica de una final épica', 10, 'DESCARGADO', TRUE),
    (301, 'Glamour en la alfombra roja', 12, 'NODESCARGADO', FALSE),
    (302, 'Decisiones clave en la cumbre', 16, 'DESCARGADO', TRUE),
    (303, 'El futuro de la red', 21, 'DESCARGADO', FALSE),
    (304, 'Luces, cámara y taquilla', 15, 'NODESCARGADO', TRUE);

INSERT INTO ReportajeTematica (reportaje_id, tematica_id) VALUES
    (300, 1),
    (301, 2),
    (302, 3),
    (302, 1),
    (303, 4),
    (304, 2);

INSERT INTO Revision (id, reportaje_id, revisor_id, estado) VALUES
    (800, 300, 11, 'PENDIENTE'),  
    (801, 302, 17, 'FINALIZADA'); 

INSERT INTO Comentario (id, revision_id, texto, fecha_hora) VALUES
    (900, 800, 'Mejoraría el título de la segunda sección.', '2026-05-31 10:00:00'),
    (901, 801, 'Todo correcto, buen trabajo con las fuentes.', '2026-04-11 12:30:00');

INSERT INTO EvaluacionReportaje (id, reportaje_id, evento_id, estado) VALUES
    (700, 300, 100, 'ACEPTADO'),
    (701, 301, 101, 'DUDOSO'),
    (702, 302, 106, 'RECHAZADO'),
    (703, 303, 110, 'ACEPTADO'),
    (704, 304, 104, 'ACEPTADO');

INSERT INTO VersionReportaje (id, reportaje_id, subtitulo, cuerpo, fecha_hora, que_cambio) VALUES 
    (400, 300, 'El Madrid levanta la 16ª', 'Contenido del reportaje final...', '2026-05-30 23:30:00', 'Versión inicial'),
    (401, 301, 'Los premiados de la noche', 'Contenido de los Goya...', '2026-02-16 01:00:00', 'Versión inicial'),
    (402, 301, 'Los premiados de la noche (Actualizado)', 'Contenido con la lista completa...', '2026-02-16 02:30:00', 'Se añade el premio a mejor director'),
    (403, 302, 'Acuerdos climáticos', 'Contenido de la ONU...', '2026-04-10 18:00:00', 'Versión inicial'),
    (404, 303, 'Ciberataques en alza', 'Un análisis profundo sobre las nuevas amenazas en la red presentadas en el congreso...', '2026-05-16 09:00:00', 'Versión inicial'),
    (405, 304, 'El cine español rompe récords', 'Entrevistas exclusivas con los directores en la alfombra del estreno...', '2026-03-02 11:30:00', 'Versión inicial');

INSERT INTO Ofrecimiento (id, evento_id, empresa_id, decision, acceso) VALUES 
    (500, 100, 200, 'ACEPTADO', TRUE),
    (501, 100, 201, NULL, FALSE),
    (502, 101, 202, 'ACEPTADO', TRUE),
    (503, 101, 203, 'RECHAZADO', FALSE),
    (504, 106, 204, 'ACEPTADO', TRUE),
    (505, 110, 202, 'ACEPTADO', TRUE),
    (506, 104, 200, 'ACEPTADO', TRUE),
    (507, 104, 201, 'ACEPTADO', TRUE);

INSERT INTO Imagen (id, reportero_id, reportaje_id, ruta_archivo, estado, tipo) VALUES
    (600, 11, 300, '/img/champions_final_01.jpg', 'DEFINITIVA', 'imagen'),
    (601, 11, 300, '/img/champions_final_02_raw.jpg', 'BORRADOR', 'imagen'),
    (602, 14, 300, '/img/sainz_freelance_foto.jpg', 'DEFINITIVA', 'imagen'),
    (603, 20, 301, '/img/oscars_redcarpet.png', 'BORRADOR', 'imagen'),
    (604, 12, 301, '/video/goya_entrevistas.mp4', 'DEFINITIVA', 'video'), 
    (605, 17, 302, '/video/onu_declaraciones_raw.avi', 'BORRADOR', 'video'),
    (606, 21, 303, '/img/hacker_raw.jpg', 'BORRADOR', 'imagen'),
    (607, 21, 303, '/img/hacker_definitiva.jpg', 'DEFINITIVA', 'imagen'),
    (608, 15, 304, '/video/estreno_entrevista_actores.mp4', 'DEFINITIVA', 'video'),
    (609, 15, 304, '/video/estreno_director.mp4', 'DEFINITIVA', 'video');
    
INSERT INTO InteresFreelance (reportero_id, evento_id, estado) VALUES 
    (14, 100, 'Interesado'),
    (14, 108, 'En duda'),
    (24, 110, 'Interesado'),
    (18, 107, 'Interesado'),
    (22, 111, 'En duda');