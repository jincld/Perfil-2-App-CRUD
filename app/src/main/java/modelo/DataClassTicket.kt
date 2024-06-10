package modelo

data class DataClassTicket(
    var UUID: String,
    var Numero: String,
    var Titulo: String,
    var Descripcion: String,
    var Autor: String,
    var EmailAutor: String,
    var FechaCreacion: String,
    var Estado: String,
    var FechaFinalizacion: String
)
