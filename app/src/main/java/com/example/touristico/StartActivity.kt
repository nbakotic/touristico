package com.example.touristico
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.touristico.admin.AdminLoginActivity
import com.example.touristico.databinding.ActivityStartBinding
import com.example.touristico.guest.GuestMainActivity
import com.example.touristico.utils.DBHelper
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
    @SuppressLint("Range")
    private fun setGuestName() = CoroutineScope(Dispatchers.IO).launch {
        val db = DBHelper(this@StartActivity, null)
        val cursor = db.getInfo()

        if (cursor!!.moveToFirst()) {
              val guestName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTNAME))
            setName(guestName)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setName(value: String) {
        binding.tvGuest.text = "WELCOME $value!"
    }
}

