package com.example.touristico.guest

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.touristico.R
import com.example.touristico.databinding.ActivityAdminMainBinding
import com.example.touristico.databinding.ActivityMainBinding
import com.example.touristico.guest.attractions.AttractionsFragment
import com.example.touristico.guest.devices.DevicesFragment
import com.example.touristico.guest.guestbook.GuestbookFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class GuestMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.main_container)
    }
}