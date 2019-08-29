package com.tanaka.snapchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ViewSnapActivity : AppCompatActivity() {
    var messageTextView: TextView? = null
    var snapImageView: ImageView? = null
    private val mAuth = FirebaseAuth.getInstance()
    var snapKey = ""
    var imageName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_snap)
        messageTextView = findViewById(R.id.messageTextView)
        snapImageView = findViewById(R.id.snapImageView)
        print(intent.getStringExtra("message"))
        var message: String = ""
        message = intent.getStringExtra("imageName")
        messageTextView?.text = intent.getStringExtra("message")
        downloadImage(intent.getStringExtra("imageURL"))
        snapKey = intent.getStringExtra("snapKey")
        imageName = intent.getStringExtra("imageName")


    }

    fun downloadImage(url: String) {
        print(url)
        val downloader = ImageDownloader()
        try {
            val downloadedImage =
                downloader.execute(url).get()
            snapImageView?.setImageBitmap(downloadedImage)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid.toString()).child("snaps").child(snapKey).removeValue()
        FirebaseStorage.getInstance().getReference().child("images").child(imageName).delete()
    }
}
