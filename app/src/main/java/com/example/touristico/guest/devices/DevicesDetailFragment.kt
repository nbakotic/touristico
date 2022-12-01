package com.example.touristico.guest.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.touristico.R
import com.example.touristico.admin.models.Device
import com.example.touristico.databinding.FragmentDevicesDetailBinding
import com.example.touristico.utils.Tools
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

    private fun setDeviceInformation() = CoroutineScope(Dispatchers.IO).launch {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        val query: Query = myRef.child("device").orderByChild("id").equalTo(deviceId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        val value = issue.getValue(Device::class.java)
                        setDefaultInformation(value)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setDefaultInformation(value: Device?) {
        binding.tvDeviceDescription.text = value?.desc
        binding.tvDeviceName.text = value?.name
        Glide.with(binding.ivDeviceDetails.context)
            .load(value?.url)
            .placeholder(R.drawable.ic_baseline_device_unknown_24)
            .into(binding.ivDeviceDetails)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}