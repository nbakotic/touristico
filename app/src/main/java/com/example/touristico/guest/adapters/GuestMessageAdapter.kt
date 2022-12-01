package com.example.touristico.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.touristico.admin.models.Chat
import com.example.touristico.databinding.MessageLayoutBinding

class GuestMessageAdapter(private val list: MutableList<Chat>) :
    RecyclerView.Adapter<GuestMessageAdapter.MyViewHolder>() {

    inner class MyViewHolder(val binding: MessageLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = MessageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]
        with(holder) {
            if (item.sender.equals("0")) {
                binding.myMessage.text = "" + item.message
                binding.myMessage.visibility = View.VISIBLE
                binding.othersLayout.visibility = View.GONE
            } else {
                binding.othersMessage.text= "" + item.message
                binding.othersLayout.visibility = View.VISIBLE
                binding.myMessage.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}