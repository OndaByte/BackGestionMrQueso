DROP TABLE IF EXISTS RolPermiso ;
DROP TABLE IF EXISTS UsuarioRol;
DROP TABLE IF EXISTS Permiso;
DROP TABLE IF EXISTS Rol;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Caja;
DROP TABLE IF EXISTS Producto;

--Contra larga xq va encriptada
CREATE TABLE Usuario(
    id INT AUTO_INCREMENT PRIMARY KEY,
    creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultMod TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado ENUM("ACTIVO","INACTIVO") DEFAULT "ACTIVO",

    usuario VARCHAR(50) UNIQUE,
    contra VARCHAR(500)
);

CREATE TABLE Producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultMod TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado ENUM("ACTIVO","INACTIVO") DEFAULT "ACTIVO",

    nombre VARCHAR(45) NOT NULL,
    precio_costo FLOAT NOT NULL DEFAULT 0,
    precio_venta FLOAT NOT NULL DEFAULT 0,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_frizado INT NOT NULL DEFAULT 0,
    ingredientes_receta varchar(255) NOT NULL
);

  
CREATE TABLE Caja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    creado TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ultMod TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    estado ENUM("ACTIVO","INACTIVO") DEFAULT "ACTIVO",

    dinero_inicial FLOAT NOT NULL,
    dinero_total FLOAT NOT NULL,
    fecha_cierre TIMESTAMP NULL
);

CREATE TABLE Rol(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE
);

CREATE TABLE Permiso(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) UNIQUE
);

CREATE TABLE UsuarioRol (
    usuario_id INT,
    rol_id INT,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES Rol(id) ON DELETE CASCADE
);

CREATE TABLE RolPermiso (
    rol_id INT,
    permiso_id INT,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id) REFERENCES Rol(id) ON DELETE CASCADE,
    FOREIGN KEY (permiso_id) REFERENCES Permiso(id) ON DELETE CASCADE
);
