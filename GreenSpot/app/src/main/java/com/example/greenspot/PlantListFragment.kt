package com.example.greenspot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.greenspot.databinding.FragmentPlantListBinding

private const val TAG = "PlantListFragment"
class PlantListFragment : Fragment() {

    private var _binding: FragmentPlantListBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val plantListViewModel: PlantListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total plants: ${plantListViewModel.plants.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlantListBinding.inflate(inflater, container, false)

        binding.plantRecyclerView.layoutManager = LinearLayoutManager(context)

        val plants = plantListViewModel.plants
        val adapter = PlantListAdapter(plants)
        binding.plantRecyclerView.adapter = adapter

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}