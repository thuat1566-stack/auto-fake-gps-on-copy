package com.aifakegps.services

import android.app.*
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.aifakegps.MainActivity
import com.aifakegps.R
import java.util.*
import kotlin.concurrent.timerTask

class FakeGPSService : Service() {
    
    private var timer: Timer? = null
    private lateinit var notificationManager: NotificationManager
    private val channelId = "fake_gps_channel"
    
    private var currentLat = 10.762622
    private var currentLng = 106.660172
    
    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification("Đang khởi động..."))
        startFakeLocationUpdates()
        return START_STICKY
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Fake GPS Service",
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = "Hiển thị thông tin giả lập GPS"
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(content: String): Notification {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("🤖 AI Fake GPS")
            .setContentText(content)
            .setSmallIcon(android.R.drawable.ic_dialog_map)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
    
    private fun startFakeLocationUpdates() {
        timer = Timer()
        timer?.scheduleAtFixedRate(timerTask {
            // AI: Di chuyển ngẫu nhiên
            currentLat += (Random().nextDouble() - 0.5) * 0.001
            currentLng += (Random().nextDouble() - 0.5) * 0.001
            
            // Giới hạn tọa độ Việt Nam
            currentLat = currentLat.coerceIn(8.0, 23.0)
            currentLng = currentLng.coerceIn(102.0, 110.0)
            
            // Cập nhật notification
            val locationText = String.format("Vị trí: %.6f, %.6f", currentLat, currentLng)
            notificationManager.notify(1, createNotification(locationText))
            
            // TODO: Gửi broadcast hoặc set mock location thực tế
        }, 0, 2000) // Cập nhật mỗi 2 giây
    }
    
    override fun onDestroy() {
        timer?.cancel()
        notificationManager.cancel(1)
        super.onDestroy()
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}