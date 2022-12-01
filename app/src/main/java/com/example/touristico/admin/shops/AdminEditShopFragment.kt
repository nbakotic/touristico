package com.example.touristico.admin.shops

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
import com.example.touristico.databinding.FragmentAdminEditShopBinding
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminEditShopFragment : Fragment() {
    private var _binding: FragmentAdminEditShopBinding? = null
    private val binding get() = _binding!!
    private lateinit var shopId: String
    private var shopName: String = ""
    private var address: String = ""
    private var distance: String = ""
    private var key: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            shopId = requireArguments().getString("shop_id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEditShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setShopInformation()

        binding.btnSaveChanges.setOnClickListener {
            if (checkInput()) updateShop()
        }

        binding.imageView6.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Are you sure you want to delete shop?")
            builder.setPositiveButton("Proceed") { dialog, _ ->
                deleteShop()
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
                .navigate(R.id.action_adminEditShopFragment_to_adminShopsFragment)
        }
    }

    private fun deleteShop() {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        val query: Query = myRef.child("shops").orderByChild("id").equalTo(shopId)
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

        Toast.makeText(context, "Shop deleted successfully", Toast.LENGTH_LONG).show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminEditShopFragment_to_adminShopsFragment)
    }

    private fun updateShop() {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        shopName = binding.etEditName.text.toString()
        address = binding.etEditAdd.text.toString()
        distance = binding.etEditDist.text.toString()

        val query: Query = myRef.child("shops").orderByChild("id").equalTo(shopId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        key = issue.key.toString()
                        myRef.child("shops/").child(key).child("name").setValue(shopName)
                        myRef.child("shops/").child(key).child("address").setValue(address)
                        myRef.child("shops/").child(key).child("distance").setValue(distance)
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        Toast.makeText(context, "Shop information updated successfully", Toast.LENGTH_LONG).show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminEditShopFragment_to_adminShopsFragment)
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

    private fun setShopInformation() = CoroutineScope(Dispatchers.IO).launch {
        val database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        val myRef = database.reference

        val query: Query = myRef.child("shops").orderByChild("id").equalTo(shopId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (issue in dataSnapshot.children) {
                        val value = issue.getValue(Beach::class.java)
                        shopName = value!!.name.toString()
                        address = value.address.toString()
                        distance = value.distance.toString()
                        setDefaultInformation()
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun setDefaultInformation() {
        binding.etEditName.setText(shopName)
        binding.etEditAdd.setText(address)
        binding.etEditDist.setText(distance)
    }
}