package com.example.blurtooth

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WebViewExample : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_web_view_example)
        val wv = findViewById<View>(R.id.webview) as WebView
        val customHTML = "<html>" +
                "<body>" +
                "<h1> Welcome to IRCTC </h1>" +
                "<h2> Welcome to IRCTC </h2>" +
                "<h3> Welcome to IRCTC </h3>" +
                "<p> It's a Static Web HTML Content </p>" +
                "</body>"+
                "</html>"

        wv.loadData(customHTML,"text/html","UTF-8")




    }
}