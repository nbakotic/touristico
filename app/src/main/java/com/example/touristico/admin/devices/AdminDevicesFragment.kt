package com.example.touristico.admin.devices

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.R
import com.example.touristico.adapter.DeviceAdapter
import com.example.touristico.admin.models.Device
import com.example.touristico.databinding.FragmentAdminDevicesBinding
import com.example.touristico.utils.DBHelper

class AdminDevicesFragment : Fragment() {
    private var _binding: FragmentAdminDevicesBinding? = null
    private lateinit var deviceAdapter: DeviceAdapter
    private var deviceList: MutableList<Device> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDevicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getFirebaseData()
        initListeners()
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
        } else {
            binding.tvCurrentList.visibility = View.GONE
            binding.tvNoDevices.visibility = View.VISIBLE
        }
    }

    private fun setAdapter() {
        deviceAdapter = DeviceAdapter(deviceList, requireContext())
        binding.rvAdmin.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAdmin.adapter = deviceAdapter
        deviceAdapter.notifyDataSetChanged()
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminDevicesFragment_to_adminAddDeviceFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}