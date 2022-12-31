package com.example.touristico.guest

import android.annotation.SuppressLint
import android.media.DeniedByServerException
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.touristico.admin.models.Beach
import com.example.touristico.admin.models.Info
import com.example.touristico.databinding.FragmentHomeBinding
import com.example.touristico.utils.DBHelper
import com.example.touristico.utils.Tools
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInformation()

    }

    @SuppressLint("Range")
    private fun setInformation() = CoroutineScope(Dispatchers.IO).launch {
        val db = DBHelper(requireContext(), null)
        //val cursor = db.getItemWithId(DBHelper.INFO_TABLE, 1)
        val cursor = db.getInfo()

        if (cursor!!.moveToFirst()) {
            do {
                val guestName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTNAME))
                val guestCountry = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTCOUNTRY))
                val appName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_APPARTMENTNAME))
                val appAdd = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_APPARTMENTADDRESS))
                val wifiName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WIFINAME))
                val wifiPass = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_WIFIPASSWORD))

                val info = Info(guestName, guestCountry, appName, appAdd, wifiName, wifiPass)
                setDefaultInformation(info)

            } while (cursor.moveToNext())
        }
    }

    private fun setDefaultInformation(value: Info?) {
        binding.tvNameSurname.text = value?.guestName
        binding.tvCountryName.text = value?.guestCountry
        binding.tvApartmentName.text = value?.appName
        binding.tvApartmentAddress.text = value?.appAdd
        binding.tvWifi.text = value?.wifiName
        binding.tvPassWifi.text = value?.wifiPass
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}