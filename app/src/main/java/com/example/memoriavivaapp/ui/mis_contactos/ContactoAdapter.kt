package com.example.memoriavivaapp.ui.mis_contactos

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.memoriavivaapp.R

class ContactoAdapter(
    private val contactos: List<Contacto>,
    private val onItemClick: (Contacto) -> Unit
) : RecyclerView.Adapter<ContactoAdapter.ContactoViewHolder>() {

    inner class ContactoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.nombre_contacto)
        val telefono: TextView = itemView.findViewById(R.id.telefono_contacto)

        fun bind(contacto: Contacto) {
            nombre.text = contacto.nombre
            telefono.text = contacto.telefono

            itemView.setOnClickListener {
                onItemClick(contacto)
            }
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