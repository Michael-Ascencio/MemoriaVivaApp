package com.example.memoriavivaapp.ui.mis_notas

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memoriavivaapp.R

class NotaAdapter(
    private var notas: List<Nota>, // Mantener como List<Nota>
    private val onEditClick: (Nota) -> Unit, // Acción para editar
    private val onDeleteClick: (Nota) -> Unit // Acción para eliminar
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>() {

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.titulo_nota)
        val contenido: TextView = itemView.findViewById(R.id.contenido_nota)
        val editIcon: ImageView = itemView.findViewById(R.id.btn_editar) // Icono para editar
        val deleteIcon: ImageView = itemView.findViewById(R.id.btn_eliminar) // Icono para eliminar

        fun bind(nota: Nota) {
            titulo.text = nota.titulo
            contenido.text = nota.contenido

            // Acción para editar
            editIcon.setOnClickListener {
                onEditClick(nota)
            }

            // Acción para eliminar
            deleteIcon.setOnClickListener {
                onDeleteClick(nota)
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

    // Método para actualizar la lista de notas
    fun setNotas(newNotas: List<Nota>) {
        notas = newNotas
        notifyDataSetChanged()
    }
}
