package com.example.touristico.admin.shops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.touristico.R
import com.example.touristico.databinding.FragmentAdminAddShopBinding
import com.example.touristico.utils.DBHelper
import com.example.touristico.utils.InputValidator
import java.util.*

class AdminAddShopFragment : Fragment() {

    private var _binding: FragmentAdminAddShopBinding? = null

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

        initListeners()
    }

    private fun initListeners() {
        idShop = UUID.randomUUID().toString()

        binding.btnAddShopToFirebase.setOnClickListener {
            if (checkInput()) {
                val db = DBHelper(requireContext(), null)

                val name = binding.etShopName.text.toString()
                val address = binding.etShopAdd.text.toString()
                val distance = binding.etShopDist.text.toString()

                db.addShop(name, address, distance)

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