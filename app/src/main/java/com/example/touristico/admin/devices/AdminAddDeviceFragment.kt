package com.example.touristico.admin.devices

import android.app.Activity.RESULT_OK
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
import com.example.touristico.databinding.FragmentAdminAddDeviceBinding
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


class AdminAddDeviceFragment : Fragment() {

    private var _binding: FragmentAdminAddDeviceBinding? = null
    private lateinit var databaseDevices: DatabaseReference
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var idDevice: String = ""
    private var urlPicture: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddDeviceBinding.inflate(inflater, container, false)
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
        idDevice = UUID.randomUUID().toString()
        binding.ibEditPicture.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            resultLauncher.launch(Tools.gallery())
        }

        binding.btnAddDeviceToFirebase.setOnClickListener {
            if (checkInput()) {
                val hashMap: HashMap<String, Any> = HashMap()
                hashMap["name"] = binding.etDevName.text.toString()
                hashMap["desc"] = binding.etDevDescription.text.toString()
                hashMap["id"] = idDevice
                hashMap["url"] = urlPicture
                Timber.d("urlPicture $urlPicture")

                databaseDevices.child("device").push().setValue(hashMap)
                Toast.makeText(context, "Device added successfully", Toast.LENGTH_LONG).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_adminAddDeviceFragment_to_adminDevicesFragment)
            }

        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAddDeviceFragment_to_adminDevicesFragment)
        }
    }

    private fun checkInput(): Boolean {
        val inputValidator = InputValidator(requireContext())

        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etDevName, binding.tilDevName)
        val descNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(
                binding.etDevDescription,
                binding.tilDevDescription
            )

        return nameNotEmptyFlag && descNotEmptyFlag
    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
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
            val ref = storageReference!!.child("images/$idDevice")
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
                        binding.pbLoading.visibility = View.GONE
                        Timber.d("urlPicture $urlPicture")
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

