package com.example.touristico.guest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.touristico.databinding.AdminGuestbookItemBinding
import com.example.touristico.databinding.DeviceCardBinding
import com.example.touristico.guest.models.GuestBook

class GuestBookAdapter(private val list: MutableList<GuestBook>) :
    RecyclerView.Adapter<GuestBookAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: AdminGuestbookItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = AdminGuestbookItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        with(holder) {
            binding.tvGuestName.text = item.name
            binding.tvCountry.text = item.country
            binding.time.text = item.time
            binding.tvPositive.text = item.positive.toString()
            binding.tvNegative.text = item.negative.toString()
            binding.stars.text = item.stars.toString() + "/5"

        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}


