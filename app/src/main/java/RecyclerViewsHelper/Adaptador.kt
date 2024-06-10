package RecyclerViewsHelper

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import jonathan.orellana.appcrudperfil2.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataClassTicket

class Adaptador(var Datos: List<DataClassTicket>): RecyclerView.Adapter<ViewHolder>() {

    fun ActualizarLista(nuevaLista: List<DataClassTicket>) {
        Datos = nuevaLista
        notifyDataSetChanged()//notifica al recycle que hay datos nuevos
    }

    fun actualizarPantalla(UUID: String, nuevoNumero: Int){
        val index = Datos.indexOfFirst { it.UUID == UUID }
        Datos[index].Numero = nuevoNumero.toString()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //unir card con rcv
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    //devolver cantidad de datos mostrados
    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = Datos[position]
        holder.txtNumeroCard.text = ticket.Numero.toString()
        holder.txtTituloCard.text = ticket.Titulo
        holder.txtDescripcionCard.text = ticket.Descripcion
        holder.txtAutorCard.text = ticket.Autor
        holder.txtEmailCard.text = ticket.EmailAutor
        holder.txtFechaInicioCard.text = ticket.FechaCreacion
        holder.txtEstadoCard.text = ticket.Estado
        holder.txtFechaFinalizacionCard.text = ticket.FechaFinalizacion

        //todo: click icono eliminar
        holder.btnBorrarCard.setOnClickListener() {

            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Quiere eliminar el ticket?")

            //Botones

            builder.setPositiveButton("si") { dialog, which ->
                eliminarDatos(ticket.Titulo, position)
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

        //todo: click icono editar
        holder.btnEditarCard.setOnClickListener() {

            val context = holder.itemView.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Editar")
            builder.setMessage("¿Desea editar el ticket?")

            //Cuadro de texto
            val cuadroTexto = EditText(context)
            cuadroTexto.setHint(ticket.Numero)
            cuadroTexto.setHint(ticket.Titulo)
            builder.setView(cuadroTexto)

            //Botones

            builder.setPositiveButton("si") { dialog, which ->
                updateTicket(cuadroTexto.text.toString(), ticket.UUID.toInt(), ticket.Titulo, ticket.Descripcion, ticket.Autor, ticket.EmailAutor, ticket.FechaCreacion, ticket.Estado, ticket.FechaFinalizacion)
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }

    }
    ////////TODO: EDITAR DATOS
    fun updateTicket(UUID: String, nuevoNumero: Int, nuevoTitulo: String, nuevaDescripcion: String, nuevoAutor: String, nuevoEmail: String, nuevaFechaCreacion: String, nuevoEstado: String, nuevaFechaFinalizacion: String) {
        GlobalScope.launch(Dispatchers.IO) {
            //crear objeto de clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //crear variable con prepare statement
            val updateTicket = objConexion?.prepareStatement("update tbTicket set Numero = ? Titulo = ? where UUID = ?")!!
            updateTicket.setInt(1, nuevoNumero)
            updateTicket.setString(2, nuevoTitulo)
            updateTicket.setString(3, UUID)
            updateTicket.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarPantalla(UUID, nuevoNumero.toInt())
            }
        }
    }
    //////// TODO: Eliminar datos
    fun eliminarDatos(Titulo: String, position: Int) {

        //Actualizar lista de datos y notificar al adaptador
        val listaDatos = Datos.toMutableList()
        listaDatos.removeAt(position)

        GlobalScope.launch(Dispatchers.IO) {
            //crear objeto clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //crear variable con prepare statement
            val deleteTicket = objConexion?.prepareStatement("delete from tbTicket where Titulo = ?")!!
            deleteTicket.setString(1, Titulo)
            deleteTicket.executeUpdate()

            val commit = objConexion?.prepareStatement("commit")!!
            commit.executeUpdate()
        }

        Datos = listaDatos.toList()
        notifyItemRemoved(position)
        notifyDataSetChanged()

    }

}
