package com.example.touristico.admin

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
    private lateinit var key: String

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
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild("info")) {
                    val hashMap: HashMap<String, Any> = HashMap()
                    hashMap["guestName"] = ""
                    hashMap["guestCountry"] = ""
                    hashMap["appName"] = ""
                    hashMap["appAdd"] = ""
                    hashMap["wifiName"] = ""
                    hashMap["wifiPass"] = ""

                    myRef.child("info").push().setValue(hashMap)
                } else {
                    setInformation()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun updateWifi() {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        wifiName = binding.etWName.text.toString()
        wifiPass = binding.etWPass.text.toString()

        val query: Query = myRef.child("info")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        key = issue.key.toString()
                        myRef.child("info/").child(key).child("wifiName").setValue(wifiName)
                        myRef.child("info/").child(key).child("wifiPass").setValue(wifiPass)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        Toast.makeText(context, "Wifi information updated successfully", Toast.LENGTH_LONG).show()
    }

    private fun updateApp() {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        appName = binding.etEditAname.text.toString()
        appAdd = binding.etAAdd.text.toString()

        val query: Query = myRef.child("info")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        key = issue.key.toString()
                        myRef.child("info/").child(key).child("appName").setValue(appName)
                        myRef.child("info/").child(key).child("appAdd").setValue(appAdd)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        Toast.makeText(context, "Apartment information updated successfully", Toast.LENGTH_LONG)
            .show()
    }

    private fun updateGuest() {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        guestName = binding.etEditGname.text.toString()
        guestCountry = binding.etEditCountry.text.toString()

        val query: Query = myRef.child("info")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        key = issue.key.toString()
                        myRef.child("info/").child(key).child("guestName").setValue(guestName)
                        myRef.child("info/").child(key).child("guestCountry").setValue(guestCountry)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

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

    private fun setInformation() = CoroutineScope(Dispatchers.IO).launch {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        val query: Query = myRef.child("info")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        val value = issue.getValue(Info::class.java)
                        guestName = value!!.guestName.toString()
                        guestCountry = value.guestCountry.toString()
                        appName = value.appName.toString()
                        appAdd = value.appAdd.toString()
                        wifiName = value.wifiName
                        wifiPass = value.wifiPass
                        setDefaultInformation()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setDefaultInformation() {
        binding.etEditGname.setText(guestName)
        binding.etEditCountry.setText(guestCountry)
        binding.etEditAname.setText(appName)
        binding.etAAdd.setText(appAdd)
        binding.etWName.setText(wifiName)
        binding.etWPass.setText(wifiPass)
    }
}