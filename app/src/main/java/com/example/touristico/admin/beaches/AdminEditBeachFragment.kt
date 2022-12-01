package com.example.touristico.admin.beaches

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.touristico.R
import com.example.touristico.admin.models.Beach
import com.example.touristico.databinding.FragmentAdminEditBeachBinding
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminEditBeachFragment : Fragment() {
    private var _binding: FragmentAdminEditBeachBinding? = null
    private val binding get() = _binding!!
    private lateinit var beachId: String
    private var beachName: String = ""
    private var address: String = ""
    private var distance: String = ""
    private var type: String = ""
    private var extra: String = ""
    private var key: String = ""

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            beachId = requireArguments().getString("id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEditBeachBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        myRef = database.reference

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBeachInformation()

        binding.btnSaveChanges.setOnClickListener {
            if (checkInput()) updateBeach()
        }

        binding.imageView6.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Are you sure you want to delete beach?")
            builder.setPositiveButton("Proceed") { dialog, _ ->
                deleteBeach()
                dialog.cancel()
            }
            builder.setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY)
        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminEditBeachFragment_to_adminBeachesFragment)
        }

    }

    private fun deleteBeach() {
        val query: Query = myRef.child("beach").orderByChild("id").equalTo(beachId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        issue.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        Toast.makeText(context, "Beach deleted successfully", Toast.LENGTH_LONG).show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminEditBeachFragment_to_adminBeachesFragment)
    }

    private fun updateBeach() {
        beachName = binding.etEditName.text.toString()
        address = binding.etEditAdd.text.toString()
        distance = binding.etEditDist.text.toString()
        type = binding.etEditType.text.toString()
        extra = binding.etEditExtra.text.toString()

        val query: Query = myRef.child("beach").orderByChild("id").equalTo(beachId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        key = issue.key.toString()
                        myRef.child("beach/").child(key).child("name").setValue(beachName)
                        myRef.child("beach/").child(key).child("address").setValue(address)
                        myRef.child("beach/").child(key).child("distance").setValue(distance)
                        myRef.child("beach/").child(key).child("type").setValue(type)
                        myRef.child("beach/").child(key).child("extra").setValue(extra)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        Toast.makeText(context, "Beach information updated successfully", Toast.LENGTH_LONG).show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminEditBeachFragment_to_adminBeachesFragment)
    }

    private fun checkInput(): Boolean {
        val inputValidator = InputValidator(requireContext())

        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditName, binding.tilEditName)
        val addNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditAdd, binding.tilEditAdd)
        val distNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditDist, binding.tilEditDist)

        return nameNotEmptyFlag && addNotEmptyFlag && distNotEmptyFlag

    }

    private fun setBeachInformation() = CoroutineScope(Dispatchers.IO).launch {
        val query: Query = myRef.child("beach").orderByChild("id").equalTo(beachId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        val value = issue.getValue(Beach::class.java)
                        beachName = value!!.name.toString()
                        address = value.address.toString()
                        distance = value.distance.toString()
                        type = value.type
                        extra = value.extra
                        setDefaultInformation()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setDefaultInformation() {
        binding.etEditName.setText(beachName)
        binding.etEditAdd.setText(address)
        binding.etEditDist.setText(distance)
        binding.etEditType.setText(type)
        binding.etEditExtra.setText(extra)
    }
}