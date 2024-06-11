CREATE TABLE tbUsuario(
UUID VARCHAR2(50),
NombreUsuario VARCHAR2(50),
Contrasena VARCHAR2(50)
)

CREATE TABLE tbTicket(
UUID VARCHAR2(50),
Numero INT,
Titulo VARCHAR2(50),
Descripcion VARCHAR2(100),
Autor VARCHAR2(50),
EmailAutor VARCHAR2(50),
FechaCreacion VARCHAR2(50),
Estado VARCHAR2(50),
FechaFinalizacion VARCHAR2(50)
)

SELECT * FROM tbUsuario
SELECT * FROM tbTicket

UPDATE tbTicket SET Numero = ?, Titulo = ?, Descripcion = ?, Autor = ?, EmailAutor = ?, FechaCreacion = ?, Estado = ?, FechaFinalizacion = ? WHERE UUID = ?
