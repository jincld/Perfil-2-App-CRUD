package RecyclerViewsHelper

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jonathan.orellana.appcrudperfil2.R

class ViewHolder(view:View): RecyclerView.ViewHolder(view) {
    var txtNumeroCard = view.findViewById<TextView>(R.id.txtNumeroCard)
    var txtTituloCard = view.findViewById<TextView>(R.id.txtTituloCard)
    var txtDescripcionCard = view.findViewById<TextView>(R.id.txtDescripcionCard)
    var txtAutorCard = view.findViewById<TextView>(R.id.txtAutorCard)
    var txtEmailCard = view.findViewById<TextView>(R.id.txtEmailCard)
    var txtFechaInicioCard = view.findViewById<TextView>(R.id.txtFechaInicioCard)
    var txtEstadoCard = view.findViewById<TextView>(R.id.txtEstadoCard)
    var txtFechaFinalizacionCard = view.findViewById<TextView>(R.id.txtFechaFinalizacionCard)
    val btnBorrarCard: ImageButton = view.findViewById(R.id.btnBorrarCard)
    val btnEditarCard: ImageButton = view.findViewById(R.id.btnEditarCard)

}