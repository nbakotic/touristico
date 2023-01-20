package com.example.touristico.guest.beaches

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.admin.models.Beach
import com.example.touristico.databinding.FragmentBeachesBinding
import com.example.touristico.guest.adapters.GuestBeachAdapter
import com.example.touristico.utils.DBHelper

class BeachesFragment : Fragment() {
    private var _binding: FragmentBeachesBinding? = null
    private lateinit var beachesAdapter: GuestBeachAdapter
    private var beachList: MutableList<Beach> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentBeachesBinding.inflate(inflater, container, false)
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
    private fun getFirebaseData() {
        beachList.clear()

        val db = DBHelper(requireContext(), null)
        val cursor = db.getBeach()

        if (cursor!!.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                val address = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ADDRESS))
                val distance = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DISTANCE))
                val type = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TYPE))
                val extra = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EXTRA))
                val url = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL))
                val id = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ID))

                val beach = Beach(name, address, distance, type, extra, url, id)
                beachList.add(beach)
            } while (cursor.moveToNext())
        }
    }

    private fun setAdapter(){
        beachesAdapter = GuestBeachAdapter(beachList, requireContext())
        binding.rvAdmin.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAdmin.adapter = beachesAdapter
        beachesAdapter.notifyDataSetChanged()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}