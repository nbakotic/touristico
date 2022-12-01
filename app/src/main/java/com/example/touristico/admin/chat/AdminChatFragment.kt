package com.example.touristico.admin.chat

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.touristico.adapter.MessageAdapter
import com.example.touristico.admin.models.Chat
import com.example.touristico.databinding.FragmentAdminChatBinding
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import com.google.firebase.database.annotations.NotNull


class AdminChatFragment : Fragment() {
    private var _binding: FragmentAdminChatBinding? = null
    private val binding get() = _binding!!
    private val chatList: MutableList<Chat> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readMessage()
        initListeners()
    }

    fun initListeners() {
        binding.sendButton.setOnClickListener {
            val msg: String = binding.textMessage.text.toString()
            if (msg != "") {
                sendMessage(msg)
            }
            binding.textMessage.setText("")
        }

        binding.button4.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Are you sure you want to delete chat history?")
            builder.setPositiveButton("Proceed") { dialog, _ ->
                deleteChat()
                dialog.cancel()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
        }

    }

    private fun deleteChat() {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        val query: Query = myRef.child("Chats")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        issue.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        Toast.makeText(context, "Chat history deleted successfully", Toast.LENGTH_LONG).show()
    }

    private fun sendMessage(message: String) {
        val reference =
            FirebaseDatabase.getInstance(Tools.URL_PATH).reference
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["sender"] = "1"
        hashMap["receiver"] = "0"
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
                        val messageAdapter = MessageAdapter(chatList)
                        binding.recyclerView.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.VERTICAL,
                            false
                        )
                        binding.recyclerView.adapter = messageAdapter
                        binding.recyclerView.scrollToPosition(messageAdapter.itemCount - 1)
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