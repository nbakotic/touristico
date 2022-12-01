package com.example.touristico.admin.beaches

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.example.touristico.R
import com.example.touristico.adapter.BeachesAdapter
import com.example.touristico.admin.models.Beach
import com.example.touristico.databinding.FragmentAdminBeachesBinding
import com.example.touristico.utils.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminBeachesFragment : Fragment() {
    private var _binding: FragmentAdminBeachesBinding? = null
    private lateinit var beachesAdapter: BeachesAdapter
    private var beachList: MutableList<Beach> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminBeachesBinding.inflate(inflater, container, false)
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

    //not firebase but who knows where this is used, let's just keep it like this
    @SuppressLint("Range")
    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch {
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
        } else {
            binding.noBeaches.visibility= View.VISIBLE
        }
        cursor.close()
        db.close()
    }

    private fun setAdapter() {
        beachesAdapter = BeachesAdapter(beachList, requireContext())
        binding.rvAdmin.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvAdmin.adapter = beachesAdapter
        beachesAdapter.notifyDataSetChanged()
    }

    private fun initListeners() {
        binding.button.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminBeachesFragment_to_adminAddBeachFragment)
        }
        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminBeachesFragment_to_adminAttractionsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}