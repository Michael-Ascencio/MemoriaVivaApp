package com.example.memoriavivaapp.ui.conf_notificaciones

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.memoriavivaapp.databinding.FragmentConfNotificacionesBinding
import java.util.*

class ConfNotificacionesFragment : Fragment() {

    private var _binding: FragmentConfNotificacionesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentConfNotificacionesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val datePicker: DatePicker = binding.datePicker
        val timePicker: TimePicker = binding.timePicker
        val descripcion: EditText = binding.editTextDescripcion
        val buttonGuardar: Button = binding.buttonGuardarRecordatorio

        // Configuración de la fecha del DatePicker para no seleccionar fechas anteriores a la actual
        val calendar = Calendar.getInstance()
        val today = Calendar.getInstance()
        datePicker.minDate = today.timeInMillis

        // Configuración del botón de guardar
        buttonGuardar.setOnClickListener {
            val selectedDate = Calendar.getInstance()
            selectedDate.set(
                datePicker.year,
                datePicker.month,
                datePicker.dayOfMonth,
                timePicker.hour,
                timePicker.minute,
                0
            )

            if (selectedDate.before(today)) {
                Toast.makeText(context, "No puedes seleccionar una fecha pasada", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear el recordatorio y configurar la notificación
            createReminder(selectedDate, descripcion.text.toString())
        }

        return root
    }

    private fun createReminder(time: Calendar, descripcion: String) {
        // Configurar la notificación con el AlarmManager
        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderBroadcastReceiver::class.java).apply {
            putExtra("description", descripcion)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time.timeInMillis, pendingIntent)

        Toast.makeText(context, "Recordatorio configurado", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
