package com.example.memoriavivaapp.ui.mis_contactos

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memoriavivaapp.R
import androidx.appcompat.app.AlertDialog

class ContactoAdapter(
    private val contactos: List<Contacto>,
    private val onEditClick: (Contacto) -> Unit,
    private val onDeleteClick: (Contacto) -> Unit
) : RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>() {

    inner class ContactoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombre_contacto)
        val telefono: TextView = itemView.findViewById(R.id.telefono_contacto)
        private val btnEditar: View = itemView.findViewById(R.id.btn_editar)
        private val btnEliminar: View = itemView.findViewById(R.id.btn_eliminar)

        fun bind(contacto: Contacto) {
            nombre.text = contacto.nombre
            telefono.text = contacto.telefono

            telefono.setOnClickListener {
                realizarLlamada(contacto.telefono)
            }

            nombre.setOnClickListener {
                realizarLlamada(contacto.telefono)
            }

            btnEditar.setOnClickListener {
                onEditClick(contacto)
            }

            btnEliminar.setOnClickListener {
                mostrarDialogoConfirmacionEliminar(contacto)
            }
        }

        private fun mostrarDialogoConfirmacionEliminar(contacto: Contacto) {
            val builder = AlertDialog.Builder(itemView.context)
            builder.setMessage("¿Estás seguro de que deseas eliminar a ${contacto.nombre}?")
                .setCancelable(false)
                .setPositiveButton("Sí") { dialog, id ->
                    // Si el usuario confirma, se procede con la eliminación
                    onDeleteClick(contacto)
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.cancel() // Si el usuario cancela, se cierra el diálogo
                }
            val alert = builder.create()
            alert.show()
        }

        private fun realizarLlamada(numero: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$numero")
            itemView.context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contacto, parent, false)
        return ContactoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactoViewHolder, position: Int) {
        val contacto = contactos[position]
        holder.bind(contacto)
    }

    override fun getItemCount(): Int = contactos.size
}
