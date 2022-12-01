package com.example.touristico.guest.adapters

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.touristico.admin.models.Shop
import com.example.touristico.databinding.ShopCardBinding


class GuestShopsAdapter(private val list: MutableList<Shop>, private val context: Context) :
    RecyclerView.Adapter<GuestShopsAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: ShopCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ShopCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        with(holder) {
            binding.name.text = item.name
            binding.distance.text = item.distance
            binding.address.text = item.address


            binding.maps.setOnClickListener {
                val map = "http://maps.google.co.in/maps?q=${item.address}"
                val i = Intent(Intent.ACTION_VIEW, Uri.parse(map))
                context.startActivity(i)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}