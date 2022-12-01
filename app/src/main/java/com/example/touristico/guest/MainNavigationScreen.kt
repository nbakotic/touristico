package com.example.touristico.guest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.touristico.databinding.FragmentMainNavigationScreenBinding


class MainNavigationScreen : Fragment() {
    private var _binding: FragmentMainNavigationScreenBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainNavigationScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

    }

    private fun initListeners() {
        binding.guestHome.setOnClickListener {
            val action = MainNavigationScreenDirections.actionMainNavigationScreenToHomeFragment()
            findNavController().navigate(action)
        }

        binding.guestAttractions.setOnClickListener {
            val action =
                MainNavigationScreenDirections.actionMainNavigationScreenToAttractionsFragment()
            findNavController().navigate(action)
        }

        binding.guestGuestbook.setOnClickListener {
            val action =
                MainNavigationScreenDirections.actionMainNavigationScreenToGuestbookFragment()
            findNavController().navigate(action)
        }

        binding.guestDevice.setOnClickListener {
            val action =
                MainNavigationScreenDirections.actionMainNavigationScreenToDevicesFragment()
            findNavController().navigate(action)
        }

        binding.guestChat.setOnClickListener {
            val action = MainNavigationScreenDirections.actionMainNavigationScreenToChatFragment()
            findNavController().navigate(action)
        }
    }
}