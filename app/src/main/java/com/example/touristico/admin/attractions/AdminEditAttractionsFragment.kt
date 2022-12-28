package com.example.touristico.admin.attractions

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
import com.example.touristico.admin.models.Attraction
import com.example.touristico.databinding.FragmentAdminEditAttractionsBinding
import com.example.touristico.utils.DBHelper
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminEditAttractionsFragment : Fragment() {
    private var _binding: FragmentAdminEditAttractionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var id: String
    private var name: String = ""
    private var address: String = ""
    private var distance: String = ""
    private var hours: String = ""
    private var desc: String = ""
    private var key: String = ""
    private var url: String = ""

    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            id = requireArguments().getString("att_id").toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminEditAttractionsBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance(Tools.URL_PATH)
        myRef = database.reference


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAttInformation()

        binding.btnSaveChanges.setOnClickListener {
            if (checkInput()) updateAtt()
        }

        binding.imageView6.setOnClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setMessage("Are you sure you want to delete attraction?")
            builder.setPositiveButton("Proceed") { dialog, _ ->
                deleteAtt()
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
                .navigate(R.id.action_adminEditAttractionsFragment_to_adminAttractionsDetailFragment)
        }
    }

    private fun deleteAtt() {
        val db = DBHelper(requireContext(), null)
        db.deleteImage(url)
        db.deleteItemWithId(DBHelper.ATTRACTION_TABLE, id.toInt())

        Toast.makeText(context, "Attraction deleted successfully", Toast.LENGTH_LONG).show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminEditAttractionsFragment_to_adminAttractionsDetailFragment)
    }

    private fun updateAtt() {
        name = binding.etEditName.text.toString()
        address = binding.etEditAdd.text.toString()
        distance = binding.etEditDist.text.toString()
        hours = binding.etEditHours.text.toString()
        desc = binding.etEditDesc.text.toString()

        val db = DBHelper(requireContext(), null)
        db.updateAttraction(id.toInt(), name, address, distance, hours, desc)

        Toast.makeText(context, "Attraction information updated successfully", Toast.LENGTH_LONG)
            .show()
        Navigation.findNavController(requireView())
            .navigate(R.id.action_adminEditAttractionsFragment_to_adminAttractionsDetailFragment)
    }

    private fun checkInput(): Boolean {
        val inputValidator = InputValidator(requireContext())

        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditName, binding.tilEditName)
        val addNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditAdd, binding.tilEditAdd)
        val distNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditDist, binding.tilEditDist)
        val hoursNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditHours, binding.tilEditHours)
        val descNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etEditDesc, binding.tilEditDesc)

        return nameNotEmptyFlag && addNotEmptyFlag && distNotEmptyFlag && hoursNotEmptyFlag && descNotEmptyFlag

    }

    @SuppressLint("Range")
    private fun setAttInformation()  {
        val db = DBHelper(requireContext(), null)
        val cursor = db.getItemWithId(DBHelper.ATTRACTION_TABLE, id.toInt())

        if (cursor!!.moveToFirst()) {
            do {
                name = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NAME))
                address = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_ADDRESS))
                distance = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DISTANCE))
                hours = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_HOURS))
                desc = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DESC))
                url = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_URL))
            } while (cursor.moveToNext())
        }

        setDefaultInformation()
    }

    fun setDefaultInformation() {
        binding.etEditName.setText(name)
        binding.etEditAdd.setText(address)
        binding.etEditDist.setText(distance)
        binding.etEditHours.setText(hours)
        binding.etEditDesc.setText(desc)
    }
}