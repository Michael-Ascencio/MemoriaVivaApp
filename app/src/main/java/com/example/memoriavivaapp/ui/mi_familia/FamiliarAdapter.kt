import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.memoriavivaapp.databinding.ItemFamiliarBinding
import com.example.memoriavivaapp.ui.mi_familia.Familiar
import com.example.memoriavivaapp.R

class FamiliarAdapter(
    private var familiares: List<Familiar>,
    private val onEditClick: (Familiar) -> Unit,
    private val onDeleteClick: (Familiar) -> Unit
) : RecyclerView.Adapter<FamiliarAdapter.FamiliarViewHolder>() {

    // Método para actualizar la lista de familiares
    fun submitList(list: List<Familiar>) {
        familiares = list
        notifyDataSetChanged()  // Notificar que la lista ha cambiado
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FamiliarViewHolder {
        val binding = ItemFamiliarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FamiliarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FamiliarViewHolder, position: Int) {
        val familiar = familiares[position]
        holder.bind(familiar, onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int = familiares.size

    inner class FamiliarViewHolder(private val binding: ItemFamiliarBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(familiar: Familiar, onEditClick: (Familiar) -> Unit, onDeleteClick: (Familiar) -> Unit) {
            // Asignar valores a las vistas del item manualmente
            binding.textViewNombre.text = familiar.nombre
            binding.textViewParentesco.text = familiar.parentesco
            binding.textViewDescripcion.text = familiar.descripcion

            // Cargar la imagen del familiar
            if (!familiar.foto.isNullOrEmpty()) {
                Glide.with(binding.root.context)
                    .load(familiar.foto)
                    .into(binding.imageViewFoto)
            } else {
                binding.imageViewFoto.setImageResource(R.drawable.person_book)
            }

            // Configurar el botón de editar
            binding.buttonEdit.setOnClickListener { onEditClick(familiar) }

            // Configurar el botón de eliminar
            binding.buttonDelete.setOnClickListener { onDeleteClick(familiar) }
        }
    }

}
