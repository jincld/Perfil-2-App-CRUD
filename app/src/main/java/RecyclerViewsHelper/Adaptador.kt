package RecyclerViewsHelper

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import jonathan.orellana.appcrudperfil2.MainActivity
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

    fun actualizarPantalla(UUID: String, nuevoTitulo: String, nuevoNumero: Int, nuevaDescripcion: String, nuevoAutor: String, nuevoEmail: String, nuevaFechaCreacion: String, nuevoEstado: String, nuevaFechaFinalizacion: String){
        val index = Datos.indexOfFirst { it.UUID == UUID }
        Datos[index].Numero = nuevoNumero.toString()
        Datos[index].Titulo = nuevoTitulo
        Datos[index].Descripcion = nuevaDescripcion
        Datos[index].Autor = nuevoAutor
        Datos[index].EmailAutor = nuevoEmail
        Datos[index].FechaCreacion = nuevaFechaCreacion
        Datos[index].Estado = nuevoEstado
        Datos[index].FechaFinalizacion = nuevaFechaFinalizacion
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

            val txtNuevoNumero = EditText(context).apply {
                setText(ticket.Numero)
            }

            val txtNuevoTitulo = EditText(context).apply {
                setText(ticket.Titulo)
            }

            val txtNuevaDescripcion = EditText(context).apply {
                setText(ticket.Descripcion)
            }

            val txtNuevoAutor = EditText(context).apply {
                setText(ticket.Autor)
            }

            val txtNuevoEmail = EditText(context).apply {
                setText(ticket.EmailAutor)
            }

            val txtNuevaFechaInicio = EditText(context).apply {
                setText(ticket.FechaCreacion)
            }

            val txtNuevoEstado = EditText(context).apply {
                setText(ticket.Estado)
            }

            val txtNuevaFechaFinalizacion = EditText(context).apply {
                setText(ticket.FechaFinalizacion)
            }

           ///Cuadro de texto
            val layout = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(txtNuevoNumero)
                addView(txtNuevoTitulo)
                addView(txtNuevaDescripcion)
                addView(txtNuevoAutor)
                addView(txtNuevoEmail)
                addView(txtNuevaFechaInicio)
                addView(txtNuevoEstado)
                addView(txtNuevaFechaFinalizacion)
            }
            builder.setView(layout)
            //Botones

            builder.setPositiveButton("Si") { dialog, which ->
                updateTicket(layout.toString(), ticket.UUID, ticket.Numero.toInt(), ticket.Descripcion, ticket.Autor, ticket.EmailAutor, ticket.FechaCreacion, ticket.Estado, ticket.FechaFinalizacion)
            }

            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()

        }

    }
    ////////TODO: EDITAR DATOS
    fun updateTicket(nuevoTitulo: String, UUID: String, nuevoNumero: Int, nuevaDescripcion: String, nuevoAutor: String, nuevoEmail: String, nuevaFechaCreacion: String, nuevoEstado: String, nuevaFechaFinalizacion: String) {
        GlobalScope.launch(Dispatchers.IO) {
            //crear objeto de clase conexion
            val objConexion = ClaseConexion().cadenaConexion()

            //crear variable con prepare statement
            val updateTicket = objConexion?.prepareStatement("UPDATE tbTicket SET Numero = ?, Titulo = ?, Descripcion = ?, Autor = ?, EmailAutor = ?, FechaCreacion = ?, Estado = ?, FechaFinalizacion = ? WHERE UUID = ?")!!
            updateTicket.setString(1, nuevoNumero.toString())
            updateTicket.setString(2, nuevoTitulo)
            updateTicket.setString(3, nuevaDescripcion)
            updateTicket.setString(4, nuevoAutor)
            updateTicket.setString(5, nuevoEmail)
            updateTicket.setString(6, nuevaFechaCreacion)
            updateTicket.setString(7, nuevoEstado)
            updateTicket.setString(8, nuevaFechaFinalizacion)

            updateTicket.setString(9, UUID)
            updateTicket.executeUpdate()

            val commit = objConexion.prepareStatement("commit")!!
            commit.executeUpdate()

            withContext(Dispatchers.Main){
                actualizarPantalla(UUID, nuevoTitulo.toString(), nuevoNumero.toString().toInt(), nuevaDescripcion.toString(), nuevoAutor.toString(), nuevoEmail.toString(), nuevaFechaCreacion.toString(), nuevoEstado.toString(), nuevaFechaFinalizacion.toString())
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
