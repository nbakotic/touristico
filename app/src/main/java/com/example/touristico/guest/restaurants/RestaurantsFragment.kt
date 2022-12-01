package com.example.touristico.guest.restaurants

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.admin.models.Attraction
import com.example.touristico.admin.models.Restaurant
import com.example.touristico.databinding.FragmentAttractionsDetailBinding
import com.example.touristico.databinding.FragmentRestaurantsBinding
import com.example.touristico.guest.adapters.GuestAttractionAdapter
import com.example.touristico.guest.adapters.GuestRestaurantAdapter
import com.example.touristico.utils.Tools
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RestaurantsFragment : Fragment() {
    private var _binding: FragmentRestaurantsBinding? = null
    private var restaurantList: MutableList<Restaurant> = mutableListOf()
    private lateinit var restaurantAdapter: GuestRestaurantAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRestaurantsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scrollView2.isNestedScrollingEnabled = true
        binding.rvAdmin.isNestedScrollingEnabled = false


        setAdapter()
        getFirebaseData()
    }

    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch {
        restaurantList.clear()
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.getReference("restaurants")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val value = data.getValue(Restaurant::class.java)
                    if (value != null) {
                        restaurantList.add(value)
                    }
                }
                if (restaurantList.isEmpty()) {
                    //binding.currentList.visibility = View.GONE
                    //binding.noBeaches.visibility = View.VISIBLE
                }
                restaurantAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setAdapter() {
        restaurantAdapter = GuestRestaurantAdapter(restaurantList, requireContext())
        binding.rvAdmin.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAdmin.adapter = restaurantAdapter
        restaurantAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}