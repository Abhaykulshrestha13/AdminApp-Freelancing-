package com.a13hay.adminapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class UploadQuote : AppCompatActivity() {
    lateinit var quoteEt:EditText
    lateinit var uploadImg:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_quote)
        quoteEt = findViewById(R.id.quoteet)
        uploadImg = findViewById(R.id.uploadimg)
        uploadImg.setOnClickListener {
            if(quoteEt.text.toString() == ""){
                Toast.makeText(this,"Please enter something",Toast.LENGTH_SHORT).show()
            }
            else{
                var text = quoteEt.text.toString()
                saveFireStore(text)
            }
        }
    }

    private fun saveFireStore(text: String) {
        val db = FirebaseFirestore.getInstance()
        val user:MutableMap<String,Any> = HashMap()
        user["quotes"] = text
        db.collection("users")
            .add(user)
            .addOnSuccessListener {
                Toast.makeText(this@UploadQuote,"Uploaded successfully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(this@UploadQuote,"Upload Failed",Toast.LENGTH_SHORT).show()
            }
    }
}