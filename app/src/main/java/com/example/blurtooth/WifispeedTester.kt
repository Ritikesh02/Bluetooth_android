package com.example.blurtooth

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.net.HttpURLConnection
import java.net.URL

class WifispeedTester : AppCompatActivity() {
    private lateinit var testButton: Button
    private lateinit var resultText: TextView

    private val defaultUrl = "https://www.google.com/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wifispeed_tester)

        testButton = findViewById(R.id.testButton)
        resultText = findViewById(R.id.resultText)

        testButton.setOnClickListener {
            testDownloadSpeed(defaultUrl)
        }
    }

    private fun testDownloadSpeed(urlString: String) {
        resultText.text = "Testing..."
        Thread {
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.connect()

                val inputStream = connection.inputStream
                val buffer = ByteArray(1024 * 1024)
                var totalBytes = 0
                val startTime = System.currentTimeMillis()

                while (true) {
                    val bytesRead = inputStream.read(buffer)
                    if (bytesRead == -1) break
                    totalBytes += bytesRead
                }

                val endTime = System.currentTimeMillis()
                val duration = (endTime - startTime) / 1000.0 // in seconds
                val speedMbps = (totalBytes * 8.0) / (duration * 1024 * 1024) // bits to Mbps

                runOnUiThread {
                    resultText.text = String.format("Download Speed: %.2f Mbps", speedMbps)
                }

                inputStream.close()
                connection.disconnect()

            } catch (e: Exception) {
                runOnUiThread {
                    resultText.text = "Error: ${e.message}"
                }
            }
        }.start()
    }
}
