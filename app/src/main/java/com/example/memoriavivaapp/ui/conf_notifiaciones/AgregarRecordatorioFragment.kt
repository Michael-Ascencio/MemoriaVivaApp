package com.example.memoriavivaapp.ui.agregar_recordatorio

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.memoriavivaapp.R
import com.example.memoriavivaapp.ui.conf_notifiaciones.ConfNotificacionesViewModel
import com.example.memoriavivaapp.ui.conf_notificaciones.ReminderBroadcastReceiver
import java.util.*

class AgregarRecordatorioFragment : Fragment() {

    private lateinit var editTextDescripcion: EditText
    private lateinit var textViewFecha: TextView
    private lateinit var textViewHora: TextView
    private lateinit var buttonFecha: Button
    private lateinit var buttonHora: Button
    private lateinit var buttonGuardar: Button

    private var selectedDate: Long = 0
    private var selectedTime: Long = 0

    // Usamos activityViewModels para acceder al ViewModel de la actividad
    private val confNotificacionesViewModel: ConfNotificacionesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_agregar_recordatorio, container, false)

        // Inicializar los elementos de la interfaz
        editTextDescripcion = root.findViewById(R.id.editTextDescripcion)
        textViewFecha = root.findViewById(R.id.textViewFecha)
        textViewHora = root.findViewById(R.id.textViewHora)
        buttonFecha = root.findViewById(R.id.buttonFecha)
        buttonHora = root.findViewById(R.id.buttonHora)
        buttonGuardar = root.findViewById(R.id.buttonGuardar)

        // Configurar el botón para seleccionar la fecha
        buttonFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(year, month, dayOfMonth)
                    selectedDate = selectedCalendar.timeInMillis
                    textViewFecha.text = "$dayOfMonth/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        // Configurar el botón para seleccionar la hora
        buttonHora.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePicker = TimePickerDialog(
                requireContext(),
                { _, hourOfDay, minute ->
                    val selectedCalendar = Calendar.getInstance()
                    selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    selectedCalendar.set(Calendar.MINUTE, minute)
                    selectedTime = selectedCalendar.timeInMillis
                    textViewHora.text = "$hourOfDay:$minute"
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePicker.show()
        }

        // Configurar el botón para guardar el recordatorio
        buttonGuardar.setOnClickListener {
            val descripcion = editTextDescripcion.text.toString()
            if (descripcion.isEmpty() || selectedDate == 0L || selectedTime == 0L) {
                Toast.makeText(requireContext(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                // Guardar el recordatorio en el ViewModel
                confNotificacionesViewModel.agregarRecordatorio(descripcion, selectedDate, selectedTime)

                // Programar la notificación
                programarNotificacion(descripcion)
                Toast.makeText(requireContext(), "Recordatorio guardado!", Toast.LENGTH_SHORT).show()
            }
        }

        return root
    }

    private fun programarNotificacion(descripcion: String) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDate
        calendar.set(Calendar.HOUR_OF_DAY, ((selectedTime / (1000 * 60 * 60)) % 24).toInt())
        calendar.set(Calendar.MINUTE, ((selectedTime / (1000 * 60)) % 60).toInt())

        val intent = Intent(requireContext(), ReminderBroadcastReceiver::class.java).apply {
            putExtra("descripcion", descripcion)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
    }
}
