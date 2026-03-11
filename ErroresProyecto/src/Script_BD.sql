CREATE DATABASE IF NOT EXISTS errores;

USE errores;

CREATE TABLE errores (
    id INT(11) NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    severidad VARCHAR(20) NOT NULL,
    fase VARCHAR(20) NOT NULL,
    solucion TEXT,
    fecha DATE,
    PRIMARY KEY (id)
);

CREATE TABLE usuarios (
    id INT(11) NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

INSERT INTO usuarios (username, password) VALUES ('admin', 'admin123');
