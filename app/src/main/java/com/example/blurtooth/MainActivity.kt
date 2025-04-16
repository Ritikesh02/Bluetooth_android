package com.example.blurtooth

import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var listView: ListView
    private var aAdapter: ArrayAdapter<*>? = null
    private val bAdapter = BluetoothAdapter.getDefaultAdapter()

    companion object {
        private const val REQUEST_CODE_BLUETOOTH_CONNECT = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn = findViewById<Button>(R.id.pairedDeiviesBtn)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                REQUEST_CODE_BLUETOOTH_CONNECT
            )
        }

        btn.setOnClickListener {
            if (bAdapter == null) {
                Toast.makeText(applicationContext, "Bluetooth not supported", Toast.LENGTH_SHORT)
                    .show()
            } else {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    val pairedDevices = bAdapter.bondedDevices
                    val list = ArrayList<String>()
                    if (pairedDevices.isNotEmpty()) {
                        for (device in pairedDevices) {
                            val deviceName = device.name
                            val macAddress = device.address
                            list.add("Name: $deviceName \n MAC Address: $macAddress")

                        }
                        listView = findViewById(R.id.pairedDeivies)
                        aAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
                        listView.adapter = aAdapter
                    } else {
                        Toast.makeText(this, "No paired devices found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Bluetooth permission is required", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}