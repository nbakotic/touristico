package com.example.touristico.admin.shops

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.touristico.R
import com.example.touristico.databinding.FragmentAdminAddShopBinding
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.*

class AdminAddShopFragment : Fragment() {

    private var _binding: FragmentAdminAddShopBinding? = null
    private lateinit var databaseDevices: DatabaseReference
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private val binding get() = _binding!!
    private var idShop: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddShopBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseDevices = FirebaseDatabase.getInstance(Tools.URL_PATH).reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        initListeners()
    }

    private fun initListeners() {
        idShop = UUID.randomUUID().toString()

        binding.btnAddShopToFirebase.setOnClickListener {
            if (checkInput()) {
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["name"] = binding.etShopName.text.toString()
                hashMap["address"] = binding.etShopAdd.text.toString()
                hashMap["distance"] = binding.etShopDist.text.toString()
                hashMap["id"] = idShop

                databaseDevices.child("shops/").push().setValue(hashMap)
                Toast.makeText(context, "Shop added successfully", Toast.LENGTH_LONG).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_adminAddShopFragment_to_adminShopsFragment)
            }
        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAddShopFragment_to_adminShopsFragment)
        }
    }

    private fun checkInput(): Boolean {
        val inputValidator = InputValidator(requireContext())
        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etShopName, binding.tilShopName)
        val addNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etShopAdd, binding.tilShopAdd)
        val distNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etShopDist, binding.tilShopDist)
        return nameNotEmptyFlag && addNotEmptyFlag && distNotEmptyFlag
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}