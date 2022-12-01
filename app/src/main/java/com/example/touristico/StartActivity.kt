package com.example.touristico
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.touristico.admin.AdminLoginActivity
import com.example.touristico.admin.models.Info
import com.example.touristico.databinding.ActivityStartBinding
import com.example.touristico.guest.GuestMainActivity
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber


class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEnter.setOnClickListener {
            val intent = Intent(applicationContext, GuestMainActivity::class.java)
            startActivity(intent)
        }

        binding.tvAdmin.setOnClickListener {
            val intent = Intent(applicationContext, AdminLoginActivity::class.java)
            startActivity(intent)

        }

        setGuestName()
    }

    /** FIREBASE FOR DISPLAYING GUEST NAME - REPLACE WITH INTERNAL DB **/
    private fun setGuestName() = CoroutineScope(Dispatchers.IO).launch {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        val query: Query = myRef.child("info")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        val value = issue.getValue(Info::class.java)
                        if (value != null) {
                            setName(value)
                        }
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Timber.d("Error $databaseError")
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setName(value: Info) {
        binding.tvGuest.text = "WELCOME " + value.guestName + "!"
    }
}

