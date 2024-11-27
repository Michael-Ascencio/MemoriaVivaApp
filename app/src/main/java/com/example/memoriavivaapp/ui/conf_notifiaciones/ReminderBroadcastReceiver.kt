package com.example.memoriavivaapp.ui.conf_notificaciones

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.memoriavivaapp.R

class ReminderBroadcastReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "recordatorio_channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        // Obtener la descripción del recordatorio desde el intent
        val descripcion = intent.getStringExtra("descripcion") ?: "Recordatorio sin descripción"

        // Crear el NotificationManager para emitir la notificación
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear el canal de notificación (Requiere API 26 o superior)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para notificaciones de recordatorios"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Crear la notificación
        val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.edit_notifications) // Aquí deberías poner el ícono de tu app
            .setContentTitle("¡Es hora de tu recordatorio!")
            .setContentText(descripcion)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // La notificación se descarta al tocarla
            .build()

        // Mostrar la notificación
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
