package com.example.touristico.admin.attractions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.touristico.R
import com.example.touristico.databinding.FragmentAdminAttractionsBinding


class AdminAttractionsFragment : Fragment() {
    private var _binding: FragmentAdminAttractionsBinding? = null
    private lateinit var beaches: CardView
    private lateinit var shops: CardView
    private lateinit var attractions: CardView
    private lateinit var restaurants: CardView

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAdminAttractionsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beaches = binding.beaches
        shops = binding.shops
        attractions = binding.attractions
        restaurants = binding.restaurants

        beaches.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAttractionsFragment_to_adminBeachesFragment)
        }

        shops.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAttractionsFragment_to_adminShopsFragment)
        }

        attractions.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAttractionsFragment_to_adminAttractionsDetailFragment)
        }

        restaurants.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_adminAttractionsFragment_to_adminRestaurantsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}