DROP TABLE IF EXISTS Comentario;
DROP TABLE IF EXISTS Revision;
DROP TABLE IF EXISTS Imagen;
DROP TABLE IF EXISTS Ofrecimiento;
DROP TABLE IF EXISTS Asignacion;
DROP TABLE IF EXISTS VersionReportaje;
DROP TABLE IF EXISTS EvaluacionReportaje; 
DROP TABLE IF EXISTS ReportajeTematica;
DROP TABLE IF EXISTS Reportaje;
DROP TABLE IF EXISTS EventoTematica;
DROP TABLE IF EXISTS InteresFreelance;
DROP TABLE IF EXISTS Evento;
DROP TABLE IF EXISTS ReporteroTematica;
DROP TABLE IF EXISTS Reportero;
DROP TABLE IF EXISTS EmpresaTematica; 
DROP TABLE IF EXISTS TarifaPlana;
DROP TABLE IF EXISTS EmpresaComunicacion;
DROP TABLE IF EXISTS AgenciaPrensa;
DROP TABLE IF EXISTS Tematica;
DROP TABLE IF EXISTS Provincia;
DROP TABLE IF EXISTS Pais;

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
    acepta_embargos BOOLEAN DEFAULT TRUE
);

CREATE TABLE EmpresaTematica (
    empresa_id INT NOT NULL,
    tematica_id INT NOT NULL,
    PRIMARY KEY (empresa_id, tematica_id),
    FOREIGN KEY (empresa_id) REFERENCES EmpresaComunicacion(id),
    FOREIGN KEY (tematica_id) REFERENCES Tematica(id)
);

CREATE TABLE TarifaPlana (
    empresa_id INT NOT NULL,
    agencia_id INT NOT NULL,
    cuota_mensual DECIMAL(10, 2) NOT NULL,
    al_corriente_pago BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (empresa_id, agencia_id),
    FOREIGN KEY (empresa_id) REFERENCES EmpresaComunicacion(id),
    FOREIGN KEY (agencia_id) REFERENCES AgenciaPrensa(id)
);

CREATE TABLE Pais (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL UNIQUE,
    coste_manutencion DECIMAL(10, 2) NOT NULL
);

CREATE TABLE Provincia (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL,
    pais_id INT NOT NULL,
    coste_alojamiento DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (pais_id) REFERENCES Pais(id)
);

CREATE TABLE Reportero (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(64) NOT NULL,
    agencia_id INT, 
    tipo VARCHAR(32) NOT NULL, 
    provincia_id INT NOT NULL, 
    FOREIGN KEY (agencia_id) REFERENCES AgenciaPrensa(id),
    FOREIGN KEY (provincia_id) REFERENCES Provincia(id)
);

CREATE TABLE ReporteroTematica (
    reportero_id INT NOT NULL,
    tematica_id INT NOT NULL,
    PRIMARY KEY (reportero_id, tematica_id),
    FOREIGN KEY (reportero_id) REFERENCES Reportero(id),
    FOREIGN KEY (tematica_id) REFERENCES Tematica(id)
);

CREATE TABLE Evento (
    id INT PRIMARY KEY NOT NULL,
    nombre VARCHAR(128) NOT NULL,
    fecha DATE NOT NULL,        
    fecha_fin DATE NOT NULL,     
    precio DECIMAL(10, 2) NOT NULL,
    agencia_id INT NOT NULL,
    provincia_id INT NOT NULL,
    asignacion_finalizada BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (agencia_id) REFERENCES AgenciaPrensa(id),
    FOREIGN KEY (provincia_id) REFERENCES Provincia(id)
);

CREATE TABLE EventoTematica (
    evento_id INT NOT NULL,
    tematica_id INT NOT NULL,
    PRIMARY KEY (evento_id, tematica_id),
    FOREIGN KEY (evento_id) REFERENCES Evento(id),
    FOREIGN KEY (tematica_id) REFERENCES Tematica(id)
);

CREATE TABLE InteresFreelance (
    reportero_id INT NOT NULL,
    evento_id INT NOT NULL,
    estado VARCHAR(32) NOT NULL, 
    PRIMARY KEY (reportero_id, evento_id),
    FOREIGN KEY (reportero_id) REFERENCES Reportero(id),
    FOREIGN KEY (evento_id) REFERENCES Evento(id)
);

CREATE TABLE Reportaje (
    id INT PRIMARY KEY NOT NULL,
    titulo VARCHAR(128) UNIQUE NOT NULL, 
    reportero_entrega_id INT NOT NULL,  
    estado VARCHAR(32) NOT NULL, 
    revision_solicitada BOOLEAN DEFAULT FALSE,
    fecha_fin_embargo TIMESTAMP,
    FOREIGN KEY (reportero_entrega_id) REFERENCES Reportero(id)
);

CREATE TABLE ReportajeTematica (
    reportaje_id INT NOT NULL,
    tematica_id INT NOT NULL,
    PRIMARY KEY (reportaje_id, tematica_id),
    FOREIGN KEY (reportaje_id) REFERENCES Reportaje(id),
    FOREIGN KEY (tematica_id) REFERENCES Tematica(id)
);

CREATE TABLE Revision (
    id INT PRIMARY KEY NOT NULL,
    reportaje_id INT NOT NULL,
    revisor_id INT NOT NULL, 
    estado VARCHAR(32) NOT NULL, 
    FOREIGN KEY (reportaje_id) REFERENCES Reportaje(id),
    FOREIGN KEY (revisor_id) REFERENCES Reportero(id)
);

CREATE TABLE Comentario (
    id INT PRIMARY KEY NOT NULL,
    revision_id INT NOT NULL,
    texto VARCHAR(1000) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    FOREIGN KEY (revision_id) REFERENCES Revision(id)
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
    rol VARCHAR(32) DEFAULT 'Base',
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
    tipo_acceso VARCHAR(32) DEFAULT 'COMPLETO',
    estado_pago VARCHAR(16) DEFAULT 'PENDIENTE',
    FOREIGN KEY (evento_id) REFERENCES Evento(id),
    FOREIGN KEY (empresa_id) REFERENCES EmpresaComunicacion(id)
);

CREATE TABLE Imagen (
    id INT PRIMARY KEY NOT NULL,
    reportero_id INT NOT NULL,
    reportaje_id INT,
    ruta_archivo VARCHAR(255) NOT NULL,
    estado VARCHAR(16) NOT NULL, 
    tipo VARCHAR(16) NOT NULL, 
    FOREIGN KEY (reportero_id) REFERENCES Reportero(id),
    FOREIGN KEY (reportaje_id) REFERENCES Reportaje(id)
);