package com.a13hay.adminapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    lateinit var upTxt:TextView
    lateinit var upImg:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        upImg = findViewById(R.id.upload_img)
        upTxt = findViewById(R.id.upload_quote)
        upImg.setOnClickListener {
            startActivity(Intent(this,UploadImage::class.java))
        }
        upTxt.setOnClickListener {
            startActivity(Intent(this,UploadQuote::class.java))
        }
    }
}