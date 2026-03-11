
DROP TABLE IF EXISTS Imagen;
DROP TABLE IF EXISTS Ofrecimiento;
DROP TABLE IF EXISTS Asignacion;
DROP TABLE IF EXISTS VersionReportaje;
DROP TABLE IF EXISTS EvaluacionReportaje; 
DROP TABLE IF EXISTS Reportaje;
DROP TABLE IF EXISTS Evento;
DROP TABLE IF EXISTS Reportero;
DROP TABLE IF EXISTS EmpresaComunicacion;
DROP TABLE IF EXISTS AgenciaPrensa;
DROP TABLE IF EXISTS Tematica;

CREATE TABLE Tematica (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL
);

CREATE TABLE AgenciaPrensa (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL
);

CREATE TABLE EmpresaComunicacion (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL,
    tematica_id INT NOT NULL,
    FOREIGN KEY (tematica_id) REFERENCES Tematica(id)
);

CREATE TABLE Reportero (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL,
    agencia_id INT, 
    tipo VARCHAR(32) NOT NULL, 
    tematica_id INT NOT NULL,
    FOREIGN KEY (agencia_id) REFERENCES AgenciaPrensa(id),
    FOREIGN KEY (tematica_id) REFERENCES Tematica(id)
);

CREATE TABLE Evento (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(128) NOT NULL,
    fecha DATE NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    agencia_id INT NOT NULL,
    tematica_id INT NOT NULL,
    FOREIGN KEY (agencia_id) REFERENCES AgenciaPrensa(id),
    FOREIGN KEY (tematica_id) REFERENCES Tematica(id)
);


CREATE TABLE Reportaje (
    id INT PRIMARY KEY NOT NULL,
    titulo VARCHAR(128) UNIQUE NOT NULL, 
    reportero_entrega_id INT NOT NULL,  
    estado VARCHAR(32) NOT NULL,
    FOREIGN KEY (reportero_entrega_id) REFERENCES Reportero(id)
);

CREATE TABLE EvaluacionReportaje (
    id INT PRIMARY KEY NOT NULL,
    reportaje_id INT NOT NULL,
    evento_id INT NOT NULL,
    estado VARCHAR(16) NOT NULL, 
    FOREIGN KEY (reportaje_id) REFERENCES Reportaje(id),
    FOREIGN KEY (evento_id) REFERENCES Evento(id)
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

CREATE TABLE Imagen (
    id INT PRIMARY KEY NOT NULL,
    reportero_id INT NOT NULL,
    ruta_archivo VARCHAR(255) NOT NULL,
    estado VARCHAR(16) NOT NULL, 
    tipo VARCHAR(16) NOT NULL, 
    FOREIGN KEY (reportero_id) REFERENCES Reportero(id)
);