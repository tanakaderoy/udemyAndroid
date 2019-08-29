package com.tanaka.snapchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.Toast
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnCompleteListener
import android.R.attr.password
import android.util.Log
import androidx.fragment.app.FragmentActivity
import android.R.attr.password
import android.content.Intent
import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {
    var emailEditText: EditText? = null
    var passWordEditText: EditText? = null
    private val mAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        passWordEditText = findViewById(R.id.passwordEditText)
        emailEditText = findViewById(R.id.usernameEditText)
        if (mAuth.currentUser != null) {
            login()
        }
    }

    fun login() {
       val intent = Intent(this, SnapsActivity::class.java)
        startActivity(intent)
    }


    fun goClicked(view: View) {
        var email = emailEditText?.text.toString()
        var password = passWordEditText?.text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    login()

                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                var user = task.result!!.user
                                FirebaseDatabase.getInstance().getReference().child("users").child(user.uid).child("email").setValue(user.email)

                                // Sign in success, update UI with the signed-in user's information
                                login()
                            } else {

                                Toast.makeText(
                                    this@MainActivity, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()

                            }

                        }

                }
            }
    }
}
