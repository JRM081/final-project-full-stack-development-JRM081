package com.example.greenspot

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.greenspot.databinding.ListItemPlantBinding
import java.io.File
import java.util.UUID


class PlantHolder(
    private val binding: ListItemPlantBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(plant: Plant, onPlantClicked: (plantId: UUID) -> Unit) {

        binding.plantTitle.text = plant.title
        binding.plantDate.text = plant.date.toString()


        val photoFile = plant.photoFileName?.let {
            File(binding.root.context.applicationContext.filesDir, it)
        }
        binding.plantPlace.text = plant.place
        binding.homePhoto.doOnLayout { measuredView ->
            val scaledBitmap = photoFile?.let {
                getScaledBitmap(
                    it.path,
                    measuredView.width,
                    measuredView.height
                )
            }
            binding.homePhoto.setImageBitmap(scaledBitmap)
            binding.homePhoto.tag = photoFile
        }

        binding.root.setOnClickListener {
            onPlantClicked(plant.id)
        }
    }
}

class PlantListAdapter(
    private val plants: List<Plant>,
    private val onPlantClicked: (plantId: UUID) -> Unit
) : RecyclerView.Adapter<PlantHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) : PlantHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPlantBinding.inflate(inflater, parent, false)
        return PlantHolder(binding)
    }
    override fun onBindViewHolder(holder: PlantHolder, position: Int) {
        val plant = plants[position]
        holder.apply {
            holder.bind(plant, onPlantClicked)
        }
    }
    override fun getItemCount() = plants.size


}