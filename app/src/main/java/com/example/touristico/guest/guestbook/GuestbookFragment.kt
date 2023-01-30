package com.example.touristico.guest.guestbook

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.touristico.databinding.FragmentGuestbookBinding
import com.example.touristico.utils.DBHelper
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class GuestbookFragment : Fragment() {
    private var _binding: FragmentGuestbookBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseGuestBook: DatabaseReference
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var guestName: String? = null
    private var guestCountry: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{

        _binding = FragmentGuestbookBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseGuestBook = FirebaseDatabase.getInstance(Tools.URL_PATH).reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference

        initializeListeners()

    }

    private fun initializeListeners(){
        binding.btnSubmit.setOnClickListener{
            checkInput()
        }
    }

    @SuppressLint("Range")
    private fun checkInput(){
        var positiveText = binding.etEditPositive.text.toString()
        var negativeText = binding.etEditNegative.text.toString()

        if(positiveText == ""){
            positiveText = "Guest didn't post positive opinion"
        }
        if(negativeText == ""){
            negativeText = "Guest didn't post negative opinion"
        }

        val starsValue = binding.rbStars.rating
        Timber.d("stars: $starsValue")

        val db = DBHelper(requireContext(), null)
        val cursor = db.getInfo()

        if (cursor!!.moveToFirst()) {
            do {
                guestName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTNAME))
                guestCountry = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTCOUNTRY))
            } while (cursor.moveToNext())
        }

        sendReviewToFirebase(positiveText, negativeText, starsValue,
            guestName!!, guestCountry!!
        )
    }

    private fun sendReviewToFirebase(positiveText: String, negativeText: String, starsValue: Float, guestName: String, guestCountry: String) {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.UK)
        val currentDate = sdf.format(Date())

        val db = DBHelper(requireContext(), null)
        db.addGuestbook(positiveText, negativeText, guestName, guestCountry, starsValue.toString(), currentDate)

        Toast.makeText(context, "Thank you for your opinion!", Toast.LENGTH_LONG).show()

        cleanInputs()
    }

    private fun cleanInputs() {
        binding.etEditNegative.setText("")
        binding.etEditPositive.setText("")

        //binding.rbStars.numStars = 0
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}