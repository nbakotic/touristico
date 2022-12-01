package com.example.touristico.guest.attractions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import com.example.touristico.databinding.FragmentAttractionsBinding

class AttractionsFragment : Fragment() {
    private var _binding: FragmentAttractionsBinding? = null
    private lateinit var beaches: CardView
    private lateinit var shops: CardView
    private lateinit var attractions: CardView
    private lateinit var restaurants: CardView

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentAttractionsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        beaches = binding.beaches
        shops = binding.shops
        attractions = binding.attractions
        restaurants = binding.restaurants

        beaches.setOnClickListener{
            view.findNavController().navigate(AttractionsFragmentDirections.actionAttractionsFragmentToBeachesFragment())
        }

        shops.setOnClickListener{
            view.findNavController().navigate(AttractionsFragmentDirections.actionAttractionsFragmentToShopsFragment())
        }

        attractions.setOnClickListener{
            view.findNavController().navigate(AttractionsFragmentDirections.actionAttractionsFragmentToAttractionsDetailFragment())
        }

        restaurants.setOnClickListener{
            view.findNavController().navigate(AttractionsFragmentDirections.actionAttractionsFragmentToRestaurantsFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}