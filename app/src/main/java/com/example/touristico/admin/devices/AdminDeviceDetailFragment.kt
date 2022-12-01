package com.example.touristico.admin.devices

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.touristico.R
import com.example.touristico.admin.models.Device
import com.example.touristico.databinding.FragmentAdminDeviceDetailBinding
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminDeviceDetailFragment : Fragment() {
    private var _binding: FragmentAdminDeviceDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var deviceId: String
    private var deviceName: String = ""
    private var description: String = ""
    private var key: String = ""

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            deviceId = requireArguments().getString("device_id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDeviceDetailBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        myRef = database.reference

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDeviceInformation()

        binding.btnSaveChanges.setOnClickListener {
            if (checkInput()) updateDevice()
        }

        binding.imageView6.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Are you sure you want to delete device?")
            builder.setPositiveButton("Proceed") { dialog, _ ->
                deleteDevice()
                dialog.cancel()

            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminDeviceDetailFragment_to_adminDevicesFragment)
        }
    }

    private fun deleteDevice() {
        val query: Query = myRef.child("device").orderByChild("id").equalTo(deviceId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        issue.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        Toast.makeText(context, "Device deleted successfully", Toast.LENGTH_LONG).show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminDeviceDetailFragment_to_adminDevicesFragment)
    }

    private fun updateDevice() {
        val query: Query = myRef.child("device").orderByChild("id").equalTo(deviceId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        key = issue.key.toString()
                        myRef.child("device/").child(key).child("name").setValue(deviceName)
                        myRef.child("device/").child(key).child("desc").setValue(description)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        description = binding.etEditDescription.text.toString()
        deviceName = binding.etEditName.text.toString()

        Toast.makeText(context, "Device updated successfully", Toast.LENGTH_LONG).show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminDeviceDetailFragment_to_adminDevicesFragment)
    }

    private fun checkInput(): Boolean {
        val inputValidator = InputValidator(requireContext())

        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditName, binding.tilEditName)
        val descNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(
                binding.etEditDescription,
                binding.tilEditDescription
            )

        return nameNotEmptyFlag && descNotEmptyFlag

    }

    private fun setDeviceInformation() = CoroutineScope(Dispatchers.IO).launch {
        val query: Query = myRef.child("device").orderByChild("id").equalTo(deviceId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        val value = issue.getValue(Device::class.java)
                        description = value!!.desc.toString()
                        deviceName = value.name.toString()
                        setDefaultInformation()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setDefaultInformation() {
        binding.etEditDescription.setText(description)
        binding.etEditName.setText(deviceName)
    }
}