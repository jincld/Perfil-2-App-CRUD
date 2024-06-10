package jonathan.orellana.appcrudperfil2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentSanitizer
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion

class activity_iniciarSesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciar_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Mandar a llamar al boton
        val btnVolver = findViewById<ImageButton>(R.id.btnVolverIS)
        //Programar al botón
        btnVolver.setOnClickListener {
            val pantallaAnterior = Intent(this, MainActivity::class.java)
            startActivity(pantallaAnterior)
        }

        //llamar elementos
        val txtNombreUsuarioIS = findViewById<TextView>(R.id.txtUsuarioIS)
        val txtContrasenaIS = findViewById<TextView>(R.id.txtContrasenaIS)
        val btnIniciar = findViewById<Button>(R.id.btnIniciar)

        btnIniciar.setOnClickListener {
            val pantallaPrincipal = Intent(this, activity_crud::class.java)

            GlobalScope.launch(Dispatchers.IO){
                val objConexion = ClaseConexion().cadenaConexion()

                val comprobarUsuario = objConexion?.prepareStatement("SELECT * FROM tbUsuario WHERE NombreUsuario = ? AND Contrasena = ?")!!
                comprobarUsuario.setString(1, txtNombreUsuarioIS.text.toString())
                comprobarUsuario.setString(2, txtContrasenaIS.text.toString())
                val resultado = comprobarUsuario.executeQuery()

                if (resultado.next()){
                    startActivity(pantallaPrincipal)
                }else{
                    withContext(Dispatchers.Main){
                        Toast.makeText(this@activity_iniciarSesion, "Usuario no encontrado, intente nuevamente", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}