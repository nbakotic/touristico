package com.example.touristico.guest.shops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.admin.models.Shop
import com.example.touristico.databinding.FragmentShopsBinding
import com.example.touristico.guest.adapters.GuestShopsAdapter
import com.example.touristico.utils.Tools
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ShopsFragment : Fragment() {
    private var _binding: FragmentShopsBinding? = null
    private lateinit var shopsAdapter: GuestShopsAdapter
    private var shopList: MutableList<Shop> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scrollView2.isNestedScrollingEnabled = true
        binding.rvGuest.isNestedScrollingEnabled = false

        setAdapter()
        getFirebaseData()
    }

    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch {
        shopList.clear()
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.getReference("shops")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val value = data.getValue(Shop::class.java)
                    if (value != null) {
                        shopList.add(value)
                    }
                }
                if (shopList.isEmpty()) {
                    //binding.currentList.visibility = View.GONE
                    //binding.noBeaches.visibility = View.VISIBLE
                }
                shopsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setAdapter() {
        shopsAdapter = GuestShopsAdapter(shopList, requireContext())
        binding.rvGuest.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvGuest.adapter = shopsAdapter
        shopsAdapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}