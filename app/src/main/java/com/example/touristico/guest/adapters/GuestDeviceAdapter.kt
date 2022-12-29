package com.example.touristico.guest.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.touristico.R
import com.example.touristico.admin.devices.AdminDevicesFragmentDirections
import com.example.touristico.admin.models.Device
import com.example.touristico.databinding.DeviceCardBinding
import com.example.touristico.guest.devices.DevicesFragmentDirections
import com.example.touristico.utils.DBHelper

class GuestDeviceAdapter(private val list: MutableList<Device>, private val context: Context) :
    RecyclerView.Adapter<GuestDeviceAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: DeviceCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = DeviceCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = DBHelper(context, null)
        val item = list[position]
        val imageBitmap = db.getImage(item.url!!)

        with(holder) {
            binding.tvDevName.text = item.name
            Glide.with(binding.ivDevice.context)
                .asBitmap()
                .load(imageBitmap)
                .placeholder(R.drawable.ic_baseline_device_unknown_24)
                .into(binding.ivDevice)

            itemView.setOnClickListener {
                it.findNavController().navigate(
                    DevicesFragmentDirections.actionDevicesFragmentToDevicesDetailFragment(
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


