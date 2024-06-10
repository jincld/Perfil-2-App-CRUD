package jonathan.orellana.appcrudperfil2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Mandar a llamar a todos los elementos
        val btnRegistro = findViewById<Button>(R.id.btnPantallaRegistrarme)

        //Programar al botón
        btnRegistro.setOnClickListener {
            //Navegar entre pantallas
            //Ir a la siguiente pantalla
            val pantallaSiguiente = Intent(this, activity_registro::class.java)
            startActivity(pantallaSiguiente)
        }

        //Mandar a llamar
        val btnIniciar = findViewById<Button>(R.id.btnPantallaIniciarSesion)
        //Programar al botón
        btnIniciar.setOnClickListener {
            //Navegar entre pantallas
            //Ir a la siguiente pantalla
            val pantallaSiguiente = Intent(this, activity_iniciarSesion::class.java)
            startActivity(pantallaSiguiente)
        }
    }
}