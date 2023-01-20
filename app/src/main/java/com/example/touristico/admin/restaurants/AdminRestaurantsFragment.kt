package com.example.touristico.admin.restaurants

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.R
import com.example.touristico.adapter.RestaurantsAdapter
import com.example.touristico.admin.models.Restaurant
import com.example.touristico.databinding.FragmentAdminRestaurantsBinding
import com.example.touristico.utils.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminRestaurantsFragment : Fragment() {
    private var _binding: FragmentAdminRestaurantsBinding? = null
    private lateinit var restaurantsAdapter: RestaurantsAdapter
    private var restaurantsList: MutableList<Restaurant> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminRestaurantsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scrollView2.isNestedScrollingEnabled = true
        binding.rvAdmin.isNestedScrollingEnabled = false

        setAdapter()
        getFirebaseData()
        initListeners()
    }

    @SuppressLint("Range")
    private fun getFirebaseData() {
        restaurantsList.clear()
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
                restaurantsList.add(restaurant)
            } while (cursor.moveToNext())
        } else {
            binding.noBeaches.visibility = View.VISIBLE
        }
        cursor.close()
        db.close()
    }

    private fun setAdapter() {
        restaurantsAdapter = RestaurantsAdapter(restaurantsList, requireContext())
        binding.rvAdmin.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAdmin.adapter = restaurantsAdapter
        restaurantsAdapter.notifyDataSetChanged()
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminRestaurantsFragment_to_adminAddRestaurantFragment)
        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminRestaurantsFragment_to_adminAttractionsFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}