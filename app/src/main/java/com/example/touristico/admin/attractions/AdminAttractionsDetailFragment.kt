package com.example.touristico.admin.attractions

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
import com.example.touristico.utils.Tools
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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

    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch {
        attractionList.clear()
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.getReference("attractions")

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val value = data.getValue(Attraction::class.java)
                    if (value != null) {
                        attractionList.add(value)
                    }
                }
                if (attractionList.isEmpty()) {
                    //binding.currentList.visibility = View.GONE
                    binding.noBeaches.visibility = View.VISIBLE
                }
                attractionsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
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