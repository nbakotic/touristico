package com.example.touristico.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.touristico.databinding.ActivityAdminLoginBinding
import com.example.touristico.utils.Tools


class AdminLoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminLoginBinding
    private var loginFlag: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()

    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener {
            if (checkAdmin()) {
                navigateToMain()
            } else {
                Toast.makeText(this, "Please type correct password and username", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun navigateToMain() {
        val intent = Intent(applicationContext, AdminMainActivity::class.java)
        startActivity(intent)
    }

    private fun checkAdmin(): Boolean {
        val username = binding.etAdminUsername.text.toString()
        val password = binding.etAdminPassword.text.toString()

        if (Tools.ADMIN_PASSWORD == password && Tools.ADMIN_USERNAME == username) {
            loginFlag = true
        }

        return loginFlag
    }
}