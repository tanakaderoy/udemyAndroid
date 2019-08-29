package com.tanaka.snapchat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_create_snap.*
import java.io.IOException
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.*
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.storage.UploadTask
import com.google.android.gms.tasks.OnSuccessListener




class CreateSnapActivity : AppCompatActivity() {
    private val mAuth = FirebaseAuth.getInstance()
    private var mStorageRef: StorageReference? = null
    val imageName = UUID.randomUUID().toString() + ".jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        var createSnapImageView: ImageView? = null
        var messageEditText: EditText? = null


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_snap)
        createSnapImageView = findViewById(R.id.createSnapImageView)
        messageEditText = findViewById(R.id.messageEditText)
        mStorageRef = FirebaseStorage.getInstance().getReference()
    }

    private fun getPhoto() {
        intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }
    fun chooseImageClicked(view: View){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        } else {
            getPhoto()
        }
    }
    fun nextClicked(view: View) {
        createSnapImageView.isDrawingCacheEnabled = true
        createSnapImageView.buildDrawingCache()
        val bitmap = (createSnapImageView.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        val imageRefs = mStorageRef?.child("images/" + imageName)

        if (imageRefs != null) {
            imageRefs.putBytes(data)
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    // Get a URL to the uploaded content

var downloadUrl = ""

                   imageRefs?.getDownloadUrl()?.addOnSuccessListener(OnSuccessListener<Any> { uri ->
                        val url = uri.toString()

                        //Do what you need to do with url
                        downloadUrl = url
                        Log.i("URL", downloadUrl.toString())
                        val intent = Intent(this, ChooseUserActivity::class.java)
                        intent.putExtra("imageURL", downloadUrl.toString())
                        intent.putExtra("imageName", imageName)
                        intent.putExtra("message", messageEditText?.text.toString())
                        startActivity(intent)
                    })



                })
                .addOnFailureListener(OnFailureListener {
                    // Handle unsuccessful uploads
                    // ...
                })
        }
    }










    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getPhoto()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val selectedImage = data!!.data

        try {
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
            createSnapImageView.setImageBitmap(bitmap)
        } catch (e: IOException) {
            e.printStackTrace()
        }


    }



}
