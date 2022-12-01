package com.example.touristico.admin.beaches

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
import com.example.touristico.databinding.FragmentAdminAddBeachBinding
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import com.example.touristico.utils.DBHelper
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

class AdminAddBeachFragment : Fragment() {

    private var _binding: FragmentAdminAddBeachBinding? = null
    private lateinit var databaseDevices: DatabaseReference
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null

    private val binding get() = _binding!!
    private var imageUri: Uri? = null
    private var idBeach: String = ""
    private var urlPicture: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAddBeachBinding.inflate(inflater, container, false)
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
        idBeach = UUID.randomUUID().toString()
        binding.ibEditPicture.setOnClickListener {
            resultLauncher.launch(Tools.gallery())
            binding.progressBar.visibility = View.VISIBLE
        }

        //FIREBASE IS NOT USED BUT NAME IS KEPT
        binding.btnAddDeviceToFirebase.setOnClickListener {
            if (checkInput()) {
                val db = DBHelper(requireContext(), null)

                val name = binding.etBeachName.text.toString()
                val address = binding.etBeachAdd.text.toString()
                val distance = binding.etBeachDist.text.toString()
                val type = binding.etBeachType.text.toString()
                val extra = binding.etBeachBonus.text.toString()
                val url = urlPicture

                db.addBeach(name,address,distance,type,extra, url)

                Toast.makeText(context, "Beach added successfully", Toast.LENGTH_LONG).show()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_adminAddBeachFragment_to_adminBeachesFragment)
            }
        }

        binding.imageView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAddBeachFragment_to_adminBeachesFragment)
        }
    }

    private fun checkInput(): Boolean {
        val inputValidator = InputValidator(requireContext())
        val nameNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etBeachName, binding.tilBeachName)
        val addNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etBeachAdd, binding.tilBeachAdd)
        val distNotEmptyFlag: Boolean =
            inputValidator.isInputEditTextFilled(binding.etBeachDist, binding.tilBeachDist)

        return nameNotEmptyFlag && addNotEmptyFlag && distNotEmptyFlag
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
            val ref = storageReference!!.child("images/$idBeach")
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
                        binding.progressBar.visibility = View.GONE
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