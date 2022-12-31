package com.example.touristico.guest.restaurants

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.admin.models.Restaurant
import com.example.touristico.databinding.FragmentRestaurantsBinding
import com.example.touristico.guest.adapters.GuestRestaurantAdapter
import com.example.touristico.utils.DBHelper
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
    ): View {

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

    @SuppressLint("Range")
    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch {
        restaurantList.clear()

        val db = DBHelper(requireContext(), null)
        val cursor = db.getRestaurant()

        if (cursor!!.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                val address = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ADDRESS))
                val distance = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DISTANCE))
                val hours = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_HOURS))
                val food = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_FOOD))
                val url = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL))
                val id = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID))

                val restaurant = Restaurant(name, address, distance, hours, food, url, id)
                restaurantList.add(restaurant)
            } while (cursor.moveToNext())
        }
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