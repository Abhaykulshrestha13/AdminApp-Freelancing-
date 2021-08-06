package com.a13hay.adminapp

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.a13hay.adminapp.model.Model
import com.google.common.io.Files.getFileExtension
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class UploadImage : AppCompatActivity() {
    lateinit var progressBar:ProgressBar
    lateinit var selectImg:TextView
    lateinit var uploadImg:TextView
    lateinit var root:DatabaseReference
    lateinit var reference: StorageReference
    var imageUri: Uri? = null
    lateinit var imageView:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)
        progressBar = findViewById(R.id.progress_bar)
        selectImg = findViewById(R.id.select_img)
        uploadImg = findViewById(R.id.upload_button)
        imageView = findViewById(R.id.image_view)
        progressBar.visibility = View.INVISIBLE
        root = FirebaseDatabase.getInstance().getReference("Image")
        reference = FirebaseStorage.getInstance().reference

        selectImg.setOnClickListener {
            var galleryIntent: Intent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent,1)
        }
        uploadImg.setOnClickListener {
            if(imageUri != null){
                uploadToFirebase(imageUri!!)
            }
            else{
                Toast.makeText(this,"Please select Image",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadToFirebase(imageUri: Uri) {
        var fileRef:StorageReference = reference.child(System.currentTimeMillis().toString() + "." + getFileExtension(imageUri))
        fileRef
            .putFile(imageUri)
            .addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener {
                var model: Model = Model(it.toString())
                var modelId : String? = root.push().key
                if (modelId != null) {
                    root.child(modelId).setValue(model)
                }
                Toast.makeText(this,"Uploaded Succefully",Toast.LENGTH_SHORT).show()
            }
        }
            .addOnProgressListener {
            progressBar.visibility = View.VISIBLE
        }
            .addOnFailureListener{
                progressBar.visibility = View.INVISIBLE
                Toast.makeText(this,"Upload Failed",Toast.LENGTH_SHORT).show()
            }
    }

    private fun getFileExtension(imageUri: Uri): String? {
        var cr:ContentResolver = contentResolver
        var mime:MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(imageUri))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }
}