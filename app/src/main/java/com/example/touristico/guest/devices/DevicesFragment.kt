package com.example.touristico.guest.devices

import android.annotation.SuppressLint
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
import com.example.touristico.utils.DBHelper
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
    ): View {

        _binding = FragmentDevicesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        getFirebaseData()
    }

    @SuppressLint("Range")
    private fun getFirebaseData() {
        deviceList.clear()
        val db = DBHelper(requireContext(), null)
        val cursor = db.getDevice()

        if (cursor!!.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                val desc = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESC))
                val url = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL))
                val id = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID))

                val device = Device(name, desc, url, id)
                deviceList.add(device)
            } while (cursor.moveToNext())
        }
    }

    private fun setAdapter() {
        devicesAdapter = GuestDeviceAdapter(deviceList, requireContext())
        recyclerView = binding.rvGuestDevices
        binding.rvGuestDevices.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView?.adapter = devicesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}