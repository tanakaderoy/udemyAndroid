package com.tanaka.snapchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class SnapsActivity : AppCompatActivity() {
    var snapsListView: ListView? = null
    private val mAuth = FirebaseAuth.getInstance()
    var emails: ArrayList<String> = ArrayList()
    var snaps: ArrayList<DataSnapshot> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snaps)

        snapsListView = findViewById(R.id.snapsListView)
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, emails)
        snapsListView?.adapter = adapter
        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.currentUser?.uid!!).child("snaps")
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {}

                override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

                override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    val email = p0?.child("from").value as String
                    emails.add(email)
                    snaps.add(p0!!)
                    adapter.notifyDataSetChanged()
                }

                override fun onChildRemoved(p0: DataSnapshot) {
                    var index = 0;
                    for(snap: DataSnapshot in snaps){
                        if (snap.key == p0?.key){
                            snaps.removeAt(index)
                            emails.removeAt(index)


                        }
                        index++

                    }
                    adapter.notifyDataSetChanged()
                }
            })
        snapsListView?.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
val snapshot = snaps.get(i)
            val intent = Intent(this, ViewSnapActivity::class.java)
            val imageName = snapshot.child("imageName").value as String
            val imageURL = snapshot.child("imageURL").value as String
            val snapKey = snapshot.key.toString()
            val message = snapshot.child("message").value as String
            intent.putExtra("imageName", imageName)
            intent.putExtra("message", message)
            intent.putExtra("imageURL", imageURL)
            intent.putExtra("snapKey", snapKey)
            startActivity(intent)
        }
        Toast.makeText(this, mAuth.currentUser?.email.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.snaps, menu)


        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        mAuth.signOut()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.createSnap -> {
                val intent = Intent(this, CreateSnapActivity::class.java)
                startActivity(intent)

            }
            R.id.logout -> {
                mAuth.signOut()
                finish()
            }
            else -> { // Note the block
                return false
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
