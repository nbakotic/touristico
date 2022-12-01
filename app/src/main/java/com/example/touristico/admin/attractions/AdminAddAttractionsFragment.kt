package com.example.touristico.admin.attractions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.touristico.R
import com.example.touristico.databinding.FragmentAdminAddAttractionsBinding
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.*

class AdminAddAttractionsFragment : Fragment() {

    private var _binding: FragmentAdminAddAttractionsBinding? = null
    private lateinit var databaseDevices: DatabaseReference
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var id: String = ""
    private var urlPicture: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddAttractionsBinding.inflate(inflater, container, false)
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
        id = UUID.randomUUID().toString()
        binding.ibEditPicture.setOnClickListener {
            resultLauncher.launch(Tools.gallery())
            binding.progressBar2.visibility = View.VISIBLE
        }

        binding.btnAdd.setOnClickListener {
            if (checkInput()) {
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["name"] = binding.etAttName.text.toString()
                hashMap["address"] = binding.etAttAdd.text.toString()
                hashMap["distance"] = binding.etAttDist.text.toString()
                hashMap["hours"] = binding.etAttHours.text.toString()
                hashMap["desc"] = binding.etAttDesc.text.toString()
                hashMap["id"] = id
                hashMap["url"] = urlPicture

                databaseDevices.child("attractions/").push().setValue(hashMap)
                Toast.makeText(context, "Attraction added successfully", Toast.LENGTH_LONG).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_adminAddAttractionsFragment_to_adminAttractionsDetailFragment)
            }
        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAddAttractionsFragment_to_adminAttractionsDetailFragment)
        }
    }

    private fun checkInput(): Boolean {
        val inputValidator = InputValidator(requireContext())
        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etAttName, binding.tilAttName)
        val addNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etAttAdd, binding.tilAttAdd)
        val distNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etAttDist, binding.tilAttDist)
        val hoursNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etAttHours, binding.tilAttHours)
        val descNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etAttDesc, binding.tilAttDesc)

        return nameNotEmptyFlag && addNotEmptyFlag && distNotEmptyFlag && hoursNotEmptyFlag && descNotEmptyFlag
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                try {
                    imageUri = data!!.data
                    binding.ivDevicePhoto.setImageURI(data.data)
                    uploadPictureToFirebase(imageUri!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    private fun uploadPictureToFirebase(imageUri: Uri) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val ref = storageReference!!.child("images/$id")
            val uploadTask = ref.putFile(imageUri)
            uploadTask.continueWith {
                if (!it.isSuccessful) {
                    it.exception?.let { t ->
                        throw t
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result!!.addOnSuccessListener { task ->
                        urlPicture = task.toString()
                        Timber.tag("urlPicture").d(urlPicture)
                        binding.progressBar2.visibility = View.GONE
                    }
                }
            }
        } catch (e: Exception) {
            Timber.tag("uploadPictureError").d(e.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}