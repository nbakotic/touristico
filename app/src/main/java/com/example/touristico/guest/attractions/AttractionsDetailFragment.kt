package com.example.touristico.guest.attractions

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.admin.models.Attraction
import com.example.touristico.databinding.FragmentAttractionsDetailBinding
import com.example.touristico.guest.adapters.GuestAttractionAdapter
import com.example.touristico.utils.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AttractionsDetailFragment : Fragment() {
    private var _binding: FragmentAttractionsDetailBinding? = null
    private var attractionList: MutableList<Attraction> = mutableListOf()
    private lateinit var attractionsAdapter: GuestAttractionAdapter
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAttractionsDetailBinding.inflate(inflater, container, false)
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
        attractionList.clear()

        val db = DBHelper(requireContext(), null)
        val cursor = db.getAttraction()

        if (cursor!!.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                val address = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ADDRESS))
                val distance = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DISTANCE))
                val hours = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_HOURS))
                val desc = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESC))
                val url = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL))
                val id = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID))

                val attraction = Attraction(name, address, distance, hours, desc, url, id)
                attractionList.add(attraction)
            } while (cursor.moveToNext())
        }
    }

    private fun setAdapter() {
        attractionsAdapter = GuestAttractionAdapter(attractionList, requireContext())
        binding.rvAdmin.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAdmin.adapter = attractionsAdapter
        attractionsAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}