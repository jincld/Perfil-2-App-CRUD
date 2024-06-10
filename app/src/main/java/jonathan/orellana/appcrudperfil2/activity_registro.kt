package jonathan.orellana.appcrudperfil2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.util.UUID

class activity_registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Mandar a llamar al boton
        val btnVolver = findViewById<ImageButton>(R.id.btnVolver)
        //Programar al botón
        btnVolver.setOnClickListener {
            val pantallaAnterior = Intent(this, MainActivity::class.java)
            startActivity(pantallaAnterior)
        }

        //llamar elementos
        val txtNombreUsuario = findViewById<TextView>(R.id.txtNombreUsuario)
        val txtContrasena = findViewById<TextView>(R.id.txtContrasena)
        val btnRegistrarme = findViewById<Button>(R.id.btnRegistrarme)

        //programar boton
        btnRegistrarme.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val objConexion = ClaseConexion().cadenaConexion()

                val crearUsuario = objConexion?.prepareStatement("insert into tbUsuario (UUID, NombreUsuario, Contrasena) VALUES(?, ?, ?)")!!
                crearUsuario.setString(1, UUID.randomUUID().toString())
                crearUsuario.setString(2, txtNombreUsuario.text.toString())
                crearUsuario.setString(3, txtContrasena.text.toString())
                crearUsuario.executeUpdate()

                withContext(Dispatchers.Main){
                    //mostrar mensaje y limpiar campos
                    Toast.makeText(this@activity_registro, "Usuario registrado", Toast.LENGTH_SHORT).show()
                    txtNombreUsuario.setText("")
                    txtContrasena.setText("")
                }
            }
        }
    }
}