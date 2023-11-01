package com.example.greenspot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import com.example.greenspot.databinding.FragmentPlantDetailBinding
import java.util.Date
import java.util.UUID

class PlantDetailFragment : Fragment() {

    private lateinit var plant: Plant
    private var _binding: FragmentPlantDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        plant = Plant(
            id = UUID.randomUUID(),
            title = "",
            date = Date(),
            place = ""
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            FragmentPlantDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            plantTitle.doOnTextChanged { text, _, _, _ ->
                plant = plant.copy(title = text.toString())
            }
            plantDate.apply {
                text = plant.date.toString()
                isEnabled = false
            }
            plantPlace.doOnTextChanged { text, _, _, _ ->
                plant = plant.copy(place = text.toString())
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}