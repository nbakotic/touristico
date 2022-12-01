package com.example.touristico.guest

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.touristico.admin.models.Info
import com.example.touristico.databinding.FragmentHomeBinding
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInformation()

    }

    /** FIREBASE FOR DISPLAYING INFO - REPLACE WITH INTERNAL DB **/
    private fun setInformation() = CoroutineScope(Dispatchers.IO).launch {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        val query: Query = myRef.child("info")
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        val value = issue.getValue(Info::class.java)
                        setDefaultInformation(value)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun setDefaultInformation(value: Info?) {
        binding.tvNameSurname.text = value?.guestName
        binding.tvCountryName.text = value?.guestCountry
        binding.tvApartmentName.text = value?.appName
        binding.tvApartmentAddress.text = value?.appAdd
        binding.tvWifi.text = value?.wifiName
        binding.tvPassWifi.text = value?.wifiPass
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}