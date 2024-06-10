package jonathan.orellana.appcrudperfil2

import RecyclerViewsHelper.Adaptador
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.DataClassTicket
import java.util.UUID

class activity_crud : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crud)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //llamar elementos
        val txtNumero = findViewById<TextView>(R.id.txtNumero)
        val txtTitulo = findViewById<TextView>(R.id.txtTitulo)
        val txtDescripcion = findViewById<TextView>(R.id.txtDescripcion)
        val txtAutor = findViewById<TextView>(R.id.txtAutor)
        val txtEmail = findViewById<TextView>(R.id.txtEmail)
        val txtFechaCreacion = findViewById<TextView>(R.id.txtFechaCreacion)
        val txtEstado = findViewById<TextView>(R.id.txtEstado)
        val txtFechaFinalizacion = findViewById<TextView>(R.id.txtFechaFinalizacion)
        val btnAgregar = findViewById<Button>(R.id.btnAgregar)
        val rcvTickets = findViewById<RecyclerView>(R.id.rcvTickets)

        //Primer paso para mostrar datos, asignar layout al recyclerview
        rcvTickets.layoutManager = LinearLayoutManager(this)

        fun obtenerDatos(): List<DataClassTicket>{

            //crear objeto conexion

            val objConexion = ClaseConexion().cadenaConexion()

            //crear statement

            val statement = objConexion?.createStatement()
            val resulSet = statement?.executeQuery("select * from tbTicket")!!
            val tickets = mutableListOf<DataClassTicket>()

            //recorro todos los registos de la base de datos

            while(resulSet.next()){
                val UUID = resulSet.getString("UUID")
                val Numero = resulSet.getInt("Numero").toString()
                val Titulo = resulSet.getString("Titulo")
                val Descripcion = resulSet.getString("Descripcion")
                val Autor = resulSet.getString("Autor")
                val EmailAutor = resulSet.getString("EmailAutor")
                val FechaCreacion = resulSet.getString("FechaCreacion")
                val Estado = resulSet.getString("Estado")
                val FechaFinalizacionCard = resulSet.getString("FechaFinalizacion")

                val ValoresJuntos = DataClassTicket(UUID, Numero, Titulo, Descripcion, Autor, EmailAutor, FechaCreacion, Estado, FechaFinalizacionCard)
                tickets.add(ValoresJuntos)
            }
            return tickets
        }

        //Asignar adaptador a RecyclerView
        CoroutineScope(Dispatchers.IO).launch {
            val TicketsDB = obtenerDatos()
            withContext(Dispatchers.Main){
                val adapter = Adaptador(TicketsDB)
                rcvTickets.adapter= adapter
            }
        }

        //programar boton agregar ticket
        btnAgregar.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()

                val agregarTicket =
                    objConexion?.prepareStatement("insert into tbTicket (UUID, Numero, Titulo, Descripcion, Autor, EmailAutor, FechaCreacion, Estado, FechaFinalizacion) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)")!!
                agregarTicket.setString(1, UUID.randomUUID().toString())
                agregarTicket.setInt(2, txtNumero.text.toString().toInt())
                agregarTicket.setString(3, txtTitulo.text.toString())
                agregarTicket.setString(4, txtDescripcion.text.toString())
                agregarTicket.setString(5, txtAutor.text.toString())
                agregarTicket.setString(6, txtEmail.text.toString())
                agregarTicket.setString(7, txtFechaCreacion.text.toString())
                agregarTicket.setString(8, txtEstado.text.toString())
                agregarTicket.setString(9, txtFechaFinalizacion.text.toString())
                agregarTicket.executeUpdate()

                //Refresca la tabla
                val nuevosTickets = obtenerDatos()
                withContext(Dispatchers.Main){
                    (rcvTickets.adapter as? Adaptador)?.ActualizarLista(nuevosTickets)
                }

                withContext(Dispatchers.Main) {
                    //mostrar mensaje y limpiar campos
                    Toast.makeText(this@activity_crud, "Ticket registrado", Toast.LENGTH_SHORT)
                        .show()
                    txtNumero.setText("")
                    txtTitulo.setText("")
                    txtDescripcion.setText("")
                    txtAutor.setText("")
                    txtEmail.setText("")
                    txtFechaCreacion.setText("")
                    txtEstado.setText("")
                    txtFechaFinalizacion.setText("")

                    }
            }
        }
    }
}