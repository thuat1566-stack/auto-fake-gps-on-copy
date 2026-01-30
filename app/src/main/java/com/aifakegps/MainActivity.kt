package com.aifakegps

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var tvStatus: TextView
    private lateinit var tvLocation: TextView
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        tvStatus = findViewById(R.id.tvStatus)
        tvLocation = findViewById(R.id.tvLocation)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        
        setupUI()
    }
    
    private fun setupUI() {
        btnStart.setOnClickListener {
            tvStatus.text = "Đang chạy... ⚡"
            tvLocation.text = "Vị trí: 10.762622, 106.660172"
        }
        
        btnStop.setOnClickListener {
            tvStatus.text = "Đã dừng ⏹️"
        }
    }
}