package com.example.touristico.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.touristico.R
import com.example.touristico.admin.devices.AdminDevicesFragmentDirections
import com.example.touristico.admin.models.Device
import com.example.touristico.databinding.DeviceCardBinding

class DeviceAdapter(private val list: MutableList<Device>) :
    RecyclerView.Adapter<DeviceAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: DeviceCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DeviceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        with(holder) {
            binding.tvDevName.text = item.name
            Glide.with(binding.ivDevice.context)
                .load(item.url)
                .placeholder(R.drawable.ic_baseline_device_unknown_24)
                .into(binding.ivDevice)

            itemView.setOnClickListener {
                it.findNavController().navigate(
                    AdminDevicesFragmentDirections.actionAdminDevicesFragmentToAdminDeviceDetailFragment(
                        item.id
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}


