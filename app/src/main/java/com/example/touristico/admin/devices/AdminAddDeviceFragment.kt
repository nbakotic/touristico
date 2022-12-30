package com.example.touristico.admin.devices

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.touristico.R
import com.example.touristico.databinding.FragmentAdminAddDeviceBinding
import com.example.touristico.utils.DBHelper
import com.example.touristico.utils.InputValidator
import com.example.touristico.utils.Tools
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException


class AdminAddDeviceFragment : Fragment() {

    private var _binding: FragmentAdminAddDeviceBinding? = null

    private val binding get() = _binding!!
    private var imageUri: Uri? = null
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

        initListeners()
    }

    private fun initListeners() {
        binding.ibEditPicture.setOnClickListener {
            binding.pbLoading.visibility = View.VISIBLE
            resultLauncher.launch(Tools.gallery())
        }

        binding.btnAddDeviceToFirebase.setOnClickListener {
            if (checkInput()) {
                val db = DBHelper(requireContext(), null)

                val name = binding.etDevName.text.toString()
                val desc = binding.etDevDescription.text.toString()
                val url = urlPicture

                db.addDevice(name, desc, url)

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
            inputValidator.isInputEditTextFilled(binding.etDevDescription, binding.tilDevDescription
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
            val imageUrl = Tools.getFileName(imageUri, requireContext())
            val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, imageUri)

            val db = DBHelper(requireContext(), null)

            if (imageUrl != null) {
                db.addImage(imageUrl, bitmap)
                urlPicture = imageUrl
            }

            Timber.d("urlPicture $urlPicture")
            binding.pbLoading.visibility = View.GONE
        } catch (e: Exception) {
            Timber.tag("uploadPictureError").d(e.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

