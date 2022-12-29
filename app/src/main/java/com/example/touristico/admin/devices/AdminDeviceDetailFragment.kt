package com.example.touristico.admin.devices

import android.annotation.SuppressLint
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
import com.example.touristico.databinding.FragmentAdminDeviceDetailBinding
import com.example.touristico.utils.DBHelper
import com.example.touristico.utils.InputValidator
import com.google.firebase.database.*

class AdminDeviceDetailFragment : Fragment() {
    private var _binding: FragmentAdminDeviceDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var deviceId: String
    private var deviceName: String = ""
    private var description: String = ""
    private var url: String = ""

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
        val db = DBHelper(requireContext(), null)
        db.deleteImage(url)
        db.deleteItemWithId(DBHelper.DEVICE_TABLE, deviceId.toInt())

        Toast.makeText(context, "Device deleted successfully", Toast.LENGTH_LONG).show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminDeviceDetailFragment_to_adminDevicesFragment)
    }

    private fun updateDevice() {
        description = binding.etEditDescription.text.toString()
        deviceName = binding.etEditName.text.toString()

        val db = DBHelper(requireContext(), null)
        db.updateDevice(deviceId.toInt(), deviceName, description)

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

    @SuppressLint("Range")
    private fun setDeviceInformation() {
        val db = DBHelper(requireContext(), null)
        val cursor = db.getItemWithId(DBHelper.DEVICE_TABLE, deviceId.toInt())

        if (cursor!!.moveToFirst()) {
            do {
                description = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESC))
                deviceName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                url = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL))
            } while (cursor.moveToNext())
        }

        setDefaultInformation()
    }

    private fun setDefaultInformation() {
        binding.etEditDescription.setText(description)
        binding.etEditName.setText(deviceName)
    }
}