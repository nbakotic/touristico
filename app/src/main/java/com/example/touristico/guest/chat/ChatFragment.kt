package com.example.touristico.guest.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.touristico.adapter.GuestMessageAdapter
import com.example.touristico.admin.models.Chat
import com.example.touristico.databinding.FragmentChatBinding
import com.example.touristico.utils.Tools
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.annotations.NotNull


class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val chatList: MutableList<Chat> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readMessage()
        initListeners()
    }

    fun initListeners() {
        binding.sendButton.setOnClickListener{
            val msg: String = binding.textMessage.text.toString()
            if (msg != "") {
                sendMessage(msg)
            }
            binding.textMessage.setText("")
        }
    }

    private fun sendMessage(message: String) {
        val reference =
            FirebaseDatabase.getInstance(Tools.URL_PATH).reference
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["sender"] = "0"
        hashMap["receiver"] = "1"
        hashMap["message"] = message
        reference.child("Chats").push().setValue(hashMap)

    }

    private fun readMessage() {
        val reference =
            FirebaseDatabase.getInstance(Tools.URL_PATH)
                .getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NotNull dataSnapshot: DataSnapshot) {
                chatList.clear()
                for (snapshot in dataSnapshot.children) {
                    val chat: Chat? = snapshot.getValue(Chat::class.java)
                    if (chat != null) {
                        chatList.add(chat)
                    }

                    if (chatList.isNotEmpty()) {
                        val messageAdapter = GuestMessageAdapter(chatList)
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                        binding.recyclerView.adapter = messageAdapter
                        binding.recyclerView.scrollToPosition(messageAdapter.itemCount-1)
                        messageAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(@NotNull error: DatabaseError) {}
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}