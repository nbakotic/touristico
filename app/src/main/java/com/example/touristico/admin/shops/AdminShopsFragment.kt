package com.example.touristico.admin.shops

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.R
import com.example.touristico.adapter.ShopsAdapter
import com.example.touristico.admin.models.Shop
import com.example.touristico.databinding.FragmentAdminShopsBinding
import com.example.touristico.utils.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminShopsFragment : Fragment() {
    private var _binding: FragmentAdminShopsBinding? = null
    private lateinit var shopsAdapter: ShopsAdapter
    private var shopList: MutableList<Shop> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminShopsBinding.inflate(inflater, container, false)
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
    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch {
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
        } else {
            binding.noBeaches.visibility = View.VISIBLE
        }
    }

    private fun setAdapter() {
        shopsAdapter = ShopsAdapter(shopList, requireContext())
        binding.rvAdmin.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAdmin.adapter = shopsAdapter
        shopsAdapter.notifyDataSetChanged()
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminShopsFragment_to_adminAddShopFragment)
        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminShopsFragment_to_adminAttractionsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}