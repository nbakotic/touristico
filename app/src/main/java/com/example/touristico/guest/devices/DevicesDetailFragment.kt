package com.example.touristico.guest.devices

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.touristico.R
import com.example.touristico.databinding.FragmentDevicesDetailBinding
import com.example.touristico.guest.models.Device
import com.example.touristico.utils.DBHelper
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DevicesDetailFragment : Fragment() {
    private var _binding: FragmentDevicesDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var deviceId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            deviceId = requireArguments().getString("deviceId").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDevicesDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDeviceInformation()

    }

    @SuppressLint("Range")
    private fun setDeviceInformation() = CoroutineScope(Dispatchers.IO).launch {
        val db = DBHelper(requireContext(), null)
        val cursor = db.getItemWithId(DBHelper.DEVICE_TABLE, deviceId.toInt())

        if (cursor!!.moveToFirst()) {
            do {
                val description = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESC))
                val deviceName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                val url = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL))

                val device = Device(deviceName, description, url)

                setDefaultInformation(device)
            } while (cursor.moveToNext())
        }
    }

    private fun setDefaultInformation(value: Device?) {
        val db = DBHelper(requireContext(), null)
        val imageBitmap = db.getImage(value?.image!!)
        binding.tvDeviceDescription.text = value.name
        binding.tvDeviceName.text = value.description

        Glide.with(binding.ivDeviceDetails.context)
            .asBitmap()
            .load(imageBitmap)
            .placeholder(R.drawable.ic_baseline_device_unknown_24)
            .into(BitmapImageViewTarget(binding.ivDeviceDetails))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}