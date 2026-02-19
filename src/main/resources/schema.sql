-- Limpieza de tablas (de detalle a maestro)
DROP TABLE IF EXISTS Ofrecimiento;
DROP TABLE IF EXISTS Asignacion;
DROP TABLE IF EXISTS VersionReportaje;
DROP TABLE IF EXISTS Reportaje;
DROP TABLE IF EXISTS Evento;
DROP TABLE IF EXISTS Reportero;
DROP TABLE IF EXISTS EmpresaComunicacion;
DROP TABLE IF EXISTS AgenciaPrensa;

-- Creación de tablas (siguiendo tu modelo de dbdiagram.io)

CREATE TABLE AgenciaPrensa (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL
);

CREATE TABLE EmpresaComunicacion (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL
);

CREATE TABLE Reportero (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL,
    agencia_id INT NOT NULL,
    FOREIGN KEY (agencia_id) REFERENCES AgenciaPrensa(id)
);

CREATE TABLE Evento (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(128) NOT NULL,
    fecha DATE NOT NULL,
    agencia_id INT NOT NULL,
    FOREIGN KEY (agencia_id) REFERENCES AgenciaPrensa(id)
);

CREATE TABLE Reportaje (
    id INT PRIMARY KEY NOT NULL,
    titulo VARCHAR(128) UNIQUE NOT NULL, -- HU 33603: Título único
    evento_id INT UNIQUE NOT NULL,      -- Relación 1-1 con Evento
    reportero_entrega_id INT NOT NULL,  -- Registro de quién entregó
    FOREIGN KEY (evento_id) REFERENCES Evento(id),
    FOREIGN KEY (reportero_entrega_id) REFERENCES Reportero(id)
);

CREATE TABLE VersionReportaje (
    id INT PRIMARY KEY NOT NULL,
    reportaje_id INT NOT NULL,
    subtitulo VARCHAR(128),
    cuerpo VARCHAR(2000),
    fecha_hora TIMESTAMP NOT NULL,
    que_cambio VARCHAR(255),
    FOREIGN KEY (reportaje_id) REFERENCES Reportaje(id)
);

CREATE TABLE Asignacion (
    evento_id INT NOT NULL,
    reportero_id INT NOT NULL,
    PRIMARY KEY (evento_id, reportero_id),
    FOREIGN KEY (evento_id) REFERENCES Evento(id),
    FOREIGN KEY (reportero_id) REFERENCES Reportero(id)
);

CREATE TABLE Ofrecimiento (
    id INT PRIMARY KEY NOT NULL,
    evento_id INT NOT NULL,
    empresa_id INT NOT NULL,
    decision VARCHAR(16),
    acceso BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (evento_id) REFERENCES Evento(id),
    FOREIGN KEY (empresa_id) REFERENCES EmpresaComunicacion(id)
);