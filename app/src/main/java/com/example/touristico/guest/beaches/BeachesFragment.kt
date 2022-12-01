package com.example.touristico.guest.beaches

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.touristico.adapter.BeachesAdapter
import com.example.touristico.admin.models.Beach
import com.example.touristico.databinding.FragmentBeachesBinding
import com.example.touristico.guest.adapters.GuestBeachAdapter
import com.example.touristico.utils.Tools
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BeachesFragment : Fragment() {
    private var _binding: FragmentBeachesBinding? = null
    private lateinit var beachesAdapter: GuestBeachAdapter
    private var beachList: MutableList<Beach> = mutableListOf()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

    private fun getFirebaseData() = CoroutineScope(Dispatchers.IO).launch{
        beachList.clear()
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.getReference("beach")


        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children){
                    val value = data.getValue(Beach::class.java)
                    if (value != null) {
                        beachList.add(value)
                    }
                }
                if (beachList.isEmpty()){
                    //binding.currentList.visibility = View.GONE
                    //binding.noBeaches.visibility = View.VISIBLE
                }
                beachesAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
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