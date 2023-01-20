package com.example.touristico.admin.guestbook

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.touristico.databinding.FragmentAdminGuestbookBinding
import com.example.touristico.guest.adapters.GuestBookAdapter
import com.example.touristico.guest.models.GuestBook
import com.example.touristico.utils.DBHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AdminGuestbookFragment : Fragment() {

    private var _binding: FragmentAdminGuestbookBinding? = null
    private val binding get() = _binding!!
    private lateinit var guestBookAdapter: GuestBookAdapter

    private var guestReviews: MutableList<GuestBook> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding =  FragmentAdminGuestbookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAdapter()
        getFirebaseData()
    }

    @SuppressLint("Range")
    private fun getFirebaseData() {
        val db = DBHelper(requireContext(), null)
        val cursor = db.getGuestbook()

        if (cursor!!.moveToFirst()) {
            do {
                val negative = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NEGATIVE))
                val positive = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_POSITIVE))
                val guestName = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTNAME))
                val guestCountry =
                    cursor.getString(cursor.getColumnIndex(DBHelper.KEY_GUESTCOUNTRY))
                val stars = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_STARS)).toFloat()
                val time = cursor.getString(cursor.getColumnIndex(DBHelper.KEY_TIME))

                val review = GuestBook(negative, positive, guestName, guestCountry, stars, time)
                guestReviews.add(review)
            } while (cursor.moveToNext())
        }
    }

    private fun setAdapter() {
        guestBookAdapter = GuestBookAdapter(guestReviews)
        binding.rvAdminGuestBook.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvAdminGuestBook.adapter = guestBookAdapter
        guestBookAdapter.notifyDataSetChanged()
    }

}