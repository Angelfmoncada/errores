CREATE DATABASE IF NOT EXISTS errores;

USE errores;

CREATE TABLE errores (
    id INT(11) NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    severidad VARCHAR(20) NOT NULL,
    fase VARCHAR(20) NOT NULL,
    solucion TEXT,
    fecha DATE DEFAULT (CURRENT_DATE),
    PRIMARY KEY (id)
);

CREATE TABLE usuarios (
    id INT(11) NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(64) NOT NULL,
    rol VARCHAR(20) NOT NULL DEFAULT 'admin',
    PRIMARY KEY (id)
);

INSERT INTO usuarios (username, password, rol) VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin');
INSERT INTO usuarios (username, password, rol) VALUES ('kimberly', 'e3a389af866e078766ced912ed722c067d11a15940c9905c7304487aec6318a6', 'admin');
INSERT INTO usuarios (username, password, rol) VALUES ('visual', '2b627fca21406f02e065ad7987bc7641a6a9a61b3902f852f67b733e2792727b', 'visual');
