package com.example.touristico.admin.guestbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.touristico.databinding.FragmentAdminGuestbookBinding
import com.example.touristico.guest.adapters.GuestBookAdapter
import com.example.touristico.guest.models.GuestBook
import com.example.touristico.utils.Tools
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminGuestbookFragment : Fragment() {

    private var _binding: FragmentAdminGuestbookBinding? = null
    private val binding get() = _binding!!
    private lateinit var guestBookAdapter: GuestBookAdapter

    private var guestReviews: MutableList<GuestBook> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding =  FragmentAdminGuestbookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getFirebaseData()
    }

    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.getReference("guestbook")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val value = data.getValue(GuestBook::class.java)
                    if (value != null) {
                        guestReviews.add(value)
                    }
                }
                guestBookAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setAdapter() {
        guestBookAdapter = GuestBookAdapter(guestReviews)
        binding.rvAdminGuestBook.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAdminGuestBook.adapter = guestBookAdapter
        guestBookAdapter.notifyDataSetChanged()
    }

}