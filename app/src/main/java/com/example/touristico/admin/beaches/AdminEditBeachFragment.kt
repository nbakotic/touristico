package com.example.touristico.admin.beaches

import android.annotation.SuppressLint
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
import com.example.touristico.databinding.FragmentAdminEditBeachBinding
import com.example.touristico.utils.DBHelper
import com.example.touristico.utils.InputValidator
import com.google.firebase.database.*

class AdminEditBeachFragment : Fragment() {
    private var _binding: FragmentAdminEditBeachBinding? = null
    private val binding get() = _binding!!
    private lateinit var beachId: String
    private var beachName: String = ""
    private var address: String = ""
    private var distance: String = ""
    private var type: String = ""
    private var extra: String = ""
    private var url: String = ""

    @SuppressLint("Range")
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
        val db = DBHelper(requireContext(), null)
        db.deleteImage(url)
        db.deleteItemWithId(DBHelper.BEACH_TABLE, beachId.toInt())

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

        val db = DBHelper(requireContext(), null)
        db.updateBeach(beachId.toInt(),beachName,address,distance,type,extra)

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

    @SuppressLint("Range")
    private fun setBeachInformation() {
        val db = DBHelper(requireContext(), null)
        val cursor = db.getItemWithId(DBHelper.BEACH_TABLE, beachId.toInt())

        if (cursor!!.moveToFirst()) {
            do {
                beachName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                address = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ADDRESS))
                distance = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DISTANCE))
                type = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TYPE))
                extra = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_EXTRA))
                url = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL))
            } while (cursor.moveToNext())
        }

        setDefaultInformation()
    }

    private fun setDefaultInformation() {
        binding.etEditName.setText(beachName)
        binding.etEditAdd.setText(address)
        binding.etEditDist.setText(distance)
        binding.etEditType.setText(type)
        binding.etEditExtra.setText(extra)

    }
}