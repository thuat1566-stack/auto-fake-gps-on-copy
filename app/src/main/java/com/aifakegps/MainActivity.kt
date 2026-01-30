package com.aifakegps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aifakegps.databinding.ActivityMainBinding
import com.aifakegps.services.FakeGPSService

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        checkPermissions()
    }
    
    private fun setupUI() {
        binding.btnStart.setOnClickListener {
            if (checkMockLocationEnabled()) {
                startService()
            } else {
                showMockLocationWarning()
            }
        }
        
        binding.btnStop.setOnClickListener {
            stopService()
        }
        
        binding.btnSettings.setOnClickListener {
            openDeveloperOptions()
        }
    }
    
    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
        }
    }
    
    private fun checkMockLocationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.Secure.getInt(contentResolver, Settings.Secure.ALLOW_MOCK_LOCATION) != 0
        } else {
            true
        }
    }
    
    private fun showMockLocationWarning() {
        Toast.makeText(
            this,
            "Vui lòng bật Mock Location trong Developer Options",
            Toast.LENGTH_LONG
        ).show()
        openDeveloperOptions()
    }
    
    private fun openDeveloperOptions() {
        try {
            startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
        } catch (e: Exception) {
            startActivity(Intent(Settings.ACTION_SETTINGS))
        }
    }
    
    private fun startService() {
        val intent = Intent(this, FakeGPSService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
        Toast.makeText(this, "Đã bắt đầu giả lập GPS", Toast.LENGTH_SHORT).show()
    }
    
    private fun stopService() {
        val intent = Intent(this, FakeGPSService::class.java)
        stopService(intent)
        Toast.makeText(this, "Đã dừng giả lập GPS", Toast.LENGTH_SHORT).show()
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Đã cấp quyền vị trí", Toast.LENGTH_SHORT).show()
            }
        }
    }
}