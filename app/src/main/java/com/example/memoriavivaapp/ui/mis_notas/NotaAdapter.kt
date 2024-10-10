import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memoriavivaapp.R
import com.example.memoriavivaapp.ui.mis_notas.Nota

class NotaAdapter(
    private val notas: List<Nota>,
    private val onItemClick: (Nota) -> Unit
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.titulo_nota)
        val contenido: TextView = itemView.findViewById(R.id.contenido_nota)

        fun bind(nota: Nota) {
            titulo.text = nota.titulo
            contenido.text = nota.contenido

            // Configura el clic en cada nota
            itemView.setOnClickListener {
                onItemClick(nota)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val nota = notas[position]
        holder.bind(nota)
    }

    override fun getItemCount(): Int = notas.size
}
