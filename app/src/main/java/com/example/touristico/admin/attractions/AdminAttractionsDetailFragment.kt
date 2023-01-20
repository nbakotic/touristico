package com.example.touristico.admin.attractions

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.R
import com.example.touristico.adapter.AttractionsAdapter
import com.example.touristico.admin.models.Attraction
import com.example.touristico.databinding.FragmentAdminAttractionsDetailBinding
import com.example.touristico.utils.DBHelper

class AdminAttractionsDetailFragment : Fragment() {
    private var _binding: FragmentAdminAttractionsDetailBinding? = null
    private lateinit var attractionsAdapter: AttractionsAdapter
    private var attractionList: MutableList<Attraction> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAttractionsDetailBinding.inflate(inflater, container, false)
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
        } else {
            binding.noBeaches.visibility = View.VISIBLE
        }
        cursor.close()
        db.close()
    }

    private fun setAdapter() {
        attractionsAdapter = AttractionsAdapter(attractionList, requireContext())
        binding.rvAdmin.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAdmin.adapter = attractionsAdapter
        attractionsAdapter.notifyDataSetChanged()
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAttractionsDetailFragment_to_adminAddAttractionsFragment)
        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAttractionsDetailFragment_to_adminAttractionsFragment)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}