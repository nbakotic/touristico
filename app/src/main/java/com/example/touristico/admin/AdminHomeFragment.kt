package com.example.touristico.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.touristico.StartActivity
import com.example.touristico.admin.models.Info
import com.example.touristico.databinding.FragmentAdminHomeBinding
import com.example.touristico.utils.DBHelper
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminHomeFragment : Fragment() {
    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var guestName: String
    private lateinit var guestCountry: String
    private lateinit var appName: String
    private lateinit var appAdd: String
    private lateinit var wifiName: String
    private lateinit var wifiPass: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkInfo()

        binding.btnSaveGuest.setOnClickListener {
            if (checkInputG()) updateGuest()
        }

        binding.btnSaveApp.setOnClickListener {
            if (checkInputA()) updateApp()
        }

        binding.btnSaveWifi.setOnClickListener {
            if (checkInputW()) updateWifi()
        }

        binding.button3.setOnClickListener {
            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkInfo() {
        val db = DBHelper(requireContext(), null)
        val cursor = db.getItemWithId(DBHelper.INFO_TABLE, 1)

        if (cursor!!.moveToFirst()) {
            setInformation()
        } else {
            db.initInfo()
        }
    }

    private fun updateWifi() {
        wifiName = binding.etWName.text.toString()
        wifiPass = binding.etWPass.text.toString()

        val db = DBHelper(requireContext(), null)
        db.updateInfo(DBHelper.KEY_WIFINAME, wifiName, DBHelper.KEY_WIFIPASSWORD, wifiPass)

        Toast.makeText(context, "Wifi information updated successfully", Toast.LENGTH_LONG).show()
    }

    private fun updateApp() {
        appName = binding.etEditAname.text.toString()
        appAdd = binding.etAAdd.text.toString()

        val db = DBHelper(requireContext(), null)
        db.updateInfo(DBHelper.KEY_APPARTMENTNAME, appName, DBHelper.KEY_APPARTMENTADDRESS, appAdd)

        Toast.makeText(context, "Apartment information updated successfully", Toast.LENGTH_LONG)
            .show()
    }

    private fun updateGuest() {
        guestName = binding.etEditGname.text.toString()
        guestCountry = binding.etEditCountry.text.toString()

        val db = DBHelper(requireContext(), null)
        db.updateInfo(DBHelper.KEY_GUESTNAME, guestName, DBHelper.KEY_GUESTCOUNTRY, guestCountry)

        Toast.makeText(context, "Guest information updated successfully", Toast.LENGTH_LONG).show()
    }

    private fun checkInputW(): Boolean {
        val inputValidator = InputValidator(requireContext())

        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etWName, binding.tilWName)
        val addNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etWPass, binding.tilWPass)

        return nameNotEmptyFlag && addNotEmptyFlag
    }

    private fun checkInputA(): Boolean {
        val inputValidator = InputValidator(requireContext())

        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditAname, binding.tilEditAname)
        val addNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etAAdd, binding.tilAAdd)

        return nameNotEmptyFlag && addNotEmptyFlag

    }

    private fun checkInputG(): Boolean {
        val inputValidator = InputValidator(requireContext())
        return inputValidator.isInputEditTextFilled(
            binding.etEditGname,
            binding.tilEditGname
        ) && inputValidator.isInputEditTextFilled(binding.etEditCountry, binding.tilEditCountry)
    }

    @SuppressLint("Range")
    private fun setInformation() {
        val db = DBHelper(requireContext(), null)
        val cursor = db.getItemWithId(DBHelper.INFO_TABLE, 1)

        if (cursor!!.moveToFirst()) {
            do {
                guestName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTNAME))
                guestCountry = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTCOUNTRY))
                appName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_APPARTMENTNAME))
                appAdd = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_APPARTMENTADDRESS))
                wifiName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WIFINAME))
                wifiPass = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WIFIPASSWORD))
            } while (cursor.moveToNext())
        }

        setDefaultInformation()
    }

    private fun setDefaultInformation() {
        binding.etEditGname.setText(guestName)
        binding.etEditCountry.setText(guestCountry)
        binding.etEditAname.setText(appName)
        binding.etAAdd.setText(appAdd)
        binding.etWName.setText(wifiName)
        binding.etWPass.setText(wifiPass)
    }
}