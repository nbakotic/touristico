package com.example.touristico.guest.devices

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.touristico.admin.models.Device
import com.example.touristico.databinding.FragmentDevicesBinding
import com.example.touristico.guest.adapters.GuestDeviceAdapter
import com.example.touristico.utils.Tools
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DevicesFragment : Fragment() {
    private var _binding: FragmentDevicesBinding? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var devicesAdapter: GuestDeviceAdapter

    private var deviceList: MutableList<Device> = mutableListOf()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDevicesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        getFirebaseData()
    }

    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch {
        deviceList.clear()
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.getReference("device")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val value = data.getValue(Device::class.java)
                    if (value != null) {
                        deviceList.add(value)
                    }
                }
                if (deviceList.isEmpty()) {
                    //binding.tvCurrentList.visibility = View.GONE
                    //binding.tvNoDevices.visibility = View.VISIBLE
                }
                devicesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setAdapter() {
        devicesAdapter = GuestDeviceAdapter(deviceList)
        recyclerView = binding.rvGuestDevices
        binding.rvGuestDevices.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView?.adapter = devicesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}