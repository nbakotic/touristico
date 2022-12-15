package com.example.touristico.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.touristico.R
import com.example.touristico.admin.beaches.AdminBeachesFragmentDirections
import com.example.touristico.admin.models.Beach
import com.example.touristico.databinding.BeachCardBinding
import com.example.touristico.utils.DBHelper


class BeachesAdapter(private val list: MutableList<Beach>, private val context: Context) :
    RecyclerView.Adapter<BeachesAdapter.MyViewHolder>() {
    inner class MyViewHolder(val binding: BeachCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = BeachCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = DBHelper(context, null)
        val item = list[position]
        val imageBitmap = db.getImage(item.url!!)

        with(holder) {
            binding.name.text = item.name
            binding.distance.text = item.distance
            binding.address.text = item.address
            binding.tvExtraActivities.text = " " + item.extra
            binding.type.text = " " + item.type

            Glide.with(binding.image.context)
                .asBitmap()
                .load(imageBitmap)
                .placeholder(R.drawable.ic_baseline_device_unknown_24)
                .into(binding.image)

            itemView.setOnClickListener { view ->
                view.findNavController().navigate(
                    AdminBeachesFragmentDirections.actionAdminBeachesFragmentToAdminEditBeachFragment(
                        item.id
                    )
                )
            }

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