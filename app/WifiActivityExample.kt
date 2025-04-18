package com.example.blurtooth

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class WifiActivityExample : AppCompatActivity() {

    private lateinit var lstview: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var wifiManager: WifiManager

    companion object {
        private const val REQUEST_CODE_LOCATION_PERMISSION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi_example)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val btn = findViewById<Button>(R.id.button)
        lstview = findViewById(R.id.lst)

        // Request location permission if not granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        }

        btn.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                if (!wifiManager.isWifiEnabled) {
                    Toast.makeText(this, "Enabling Wifi...", Toast.LENGTH_SHORT).show()
                    wifiManager.isWifiEnabled = true
                }

                wifiManager.startScan()
                val scanResults = wifiManager.scanResults
                displayWifiNetworks(scanResults)
            } else {
                Toast.makeText(this, "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayWifiNetworks(scanResults: List<ScanResult>) {
        val list = ArrayList<String>()
        if (scanResults.isNotEmpty()) {
            for (result in scanResults) {
                list.add("SSID: ${result.SSID}\nMAC Address: ${result.BSSID}")
            }
            adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
            lstview.adapter = adapter  // Set the adapter to the ListView
        } else {
            Toast.makeText(this, "No WiFi networks found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted. Tap the button to scan WiFi.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied. Cannot scan WiFi.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
