package com.example.touristico.guest.shops

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.admin.models.Shop
import com.example.touristico.databinding.FragmentShopsBinding
import com.example.touristico.guest.adapters.GuestShopsAdapter
import com.example.touristico.utils.DBHelper


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

    @SuppressLint("Range")
    private fun getFirebaseData(){
        shopList.clear()
        val db = DBHelper(requireContext(), null)
        val cursor = db.getShop()

        if (cursor!!.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                val address = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ADDRESS))
                val distance = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DISTANCE))
                val id = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID))

                val shop = Shop(name, address, distance, id)
                shopList.add(shop)
            } while (cursor.moveToNext())
        }
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