package com.example.greenspot


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.content.FileProvider
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.greenspot.databinding.FragmentPlantDetailBinding
import kotlinx.coroutines.launch
import java.io.File
import java.util.Date


private const val DATE_FORMAT = "EEE, MMM, dd"


class PlantDetailFragment : Fragment() {

    private val args: PlantDetailFragmentArgs by navArgs()
    private val plantDetailViewModel: PlantDetailViewModel by viewModels {
        PlantDetailViewModelFactory(args.plantId)
    }
    private var _binding: FragmentPlantDetailBinding? = null
    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    private val takePhoto = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { didTakePhoto ->
        if (didTakePhoto && photoName != null) {
            plantDetailViewModel.updatePlant { oldPlant ->
                oldPlant.copy(photoFileName = photoName)
            }
        }
    }
    private var photoName: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("PlantDetailFragment", "onCreateView called")
        _binding =
            FragmentPlantDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private val uniqueTag = "PictureDialogFragment_${System.currentTimeMillis()}"
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            plantTitle.doOnTextChanged { text, _, _, _ ->
                plantDetailViewModel.updatePlant { oldPlant ->
                    oldPlant.copy(title = text.toString())
                }
            }

            plantPlace.doOnTextChanged { text, _, _, _ ->
                plantDetailViewModel.updatePlant { oldPlant ->
                    oldPlant.copy(place = text.toString())
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    plantDetailViewModel.plant.collect { plant ->
                        Log.d("PlantDetailFragment", "collect called with plant: $plant")
                        plant?.let { updateUi(it) }
                    }
                }
            }
            setFragmentResultListener(
                DatePickerFragment.REQUEST_KEY_DATE
            ) { _, bundle ->
                val newDate =
                    bundle.getSerializable(DatePickerFragment.BUNDLE_KEY_DATE) as Date
                plantDetailViewModel.updatePlant { it.copy(date = newDate) }
            }

            plantCamera.setOnClickListener {
                    photoName = "IMG_${Date()}.JPG"
                val photoFile = File(requireContext().applicationContext.filesDir,
                    photoName)
                val photoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.greenspot.fileprovider",
                    photoFile
                )
                takePhoto.launch(photoUri)
            }
            val captureImageIntent = takePhoto.contract.createIntent(
                requireContext(),
                Uri.parse("")
            )
            plantCamera.isEnabled = canResolveIntent(captureImageIntent)

            binding.plantPhoto.doOnLayout { updatePhoto(photoName) }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun updateUi(plant: Plant) {
        Log.d("PlantDetailFragment", "updateUI called")
        binding.apply {
            plantDetailViewModel.isFirstTime = false
            if (plantTitle.text.toString() != plant.title) {
                plantTitle.setText(plant.title)
            }

            plantDate.text = plant.date.toString()
            plantDate.setOnClickListener {
                findNavController().navigate(
                    PlantDetailFragmentDirections.selectDate(plant.date)
                )
            }

            if (plantPlace.text.toString() != plant.place) {
                plantPlace.setText(plant.place)
            }

            binding.plantPhoto.doOnLayout { updatePhoto(plant.photoFileName) }

            fun buttonVisibility(v: View) {
                plantDelete.visibility = View.VISIBLE;
            }

            if (!plantDetailViewModel.isFirstTime) {
                buttonVisibility(plantDelete)
                plantDelete.setOnClickListener {
                    plantDetailViewModel.deletePlant()
                    onBackPressedCallback.handleOnBackPressed()
                }
            }

                plantShare.setOnClickListener {
                    val reportIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, getPlantReport(plant))
                        putExtra(
                            Intent.EXTRA_SUBJECT,
                            getString(R.string.plant_report_subject)
                        )
                    }
                    val chooserIntent = Intent.createChooser(
                        reportIntent,
                        getString(R.string.send_report)
                    )
                    startActivity(chooserIntent)
                }

                plantPhoto.setOnClickListener{
                    if (photoName?.isNotBlank() == true) {
                        Log.d("Debug", "photoName contains: $photoName")
                        photoName?.let { it1 ->
                            val dialogFragment = PictureDialogFragment.newInstance(it1)
                            dialogFragment.show(parentFragmentManager, uniqueTag)
                        }
                    }
                }

                updatePhoto(plant.photoFileName)
            if (plantTitle.toString().isBlank()){
                plantDetailViewModel.deletePlant()
            }

            }
        }


    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            findNavController().navigateUp()
        }
    }


    private fun getPlantReport(plant: Plant): String {
        val dateString = DateFormat.format(DATE_FORMAT, plant.date).toString()
        val locationText = if (plant.place.isBlank()) {
            getString(R.string.plant_no_located)
        } else {
            getString(R.string.plant_report_located, plant.place)
        }
        val geoText = if (plant.geolocation.isBlank()){
            getString(R.string.plant_no_geo)
        } else {
            getString(R.string.plant_geo_report, plant.geolocation)
        }

        return getString(
            R.string.plant_report,
            plant.title, dateString, locationText, geoText
        )
    }

    private fun canResolveIntent(intent: Intent): Boolean {
        val packageManager: PackageManager = requireActivity().packageManager
        val resolvedActivity: ResolveInfo? =
            packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
        return resolvedActivity != null
    }
    private fun updatePhoto(photoFileName: String?) {
        if (binding.plantPhoto.tag != photoFileName) {
            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            if (photoFile?.exists() == true) {
                binding.plantPhoto.doOnLayout { measuredView ->
                    val scaledBitmap = getScaledBitmap(
                        photoFile.path,
                        measuredView.width,
                        measuredView.height
                    )
                    binding.plantPhoto.setImageBitmap(scaledBitmap)
                    binding.plantPhoto.tag = photoFileName
                }
            } else {
                binding.plantPhoto.setImageBitmap(null)
                binding.plantPhoto.tag = null
            }
        }
    }
}
