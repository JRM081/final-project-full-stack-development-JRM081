package com.example.greenspot

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.view.doOnLayout
import androidx.fragment.app.DialogFragment
import java.io.File

class PictureDialogFragment: DialogFragment() {

    companion object {
        private const val ARG_IMAGE = "ARG_IMAGE"

        fun newInstance(photoFile: String?): PictureDialogFragment {
            val args = Bundle().apply { putString(ARG_IMAGE, photoFile) }
            return PictureDialogFragment().apply { arguments = args }
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_photo, null)
            
            val photoFileName = arguments?.getString(ARG_IMAGE)
            Log.d("debug", "$photoFileName retrieved photofilename")

            val photoFile = photoFileName?.let {
                File(requireContext().applicationContext.filesDir, it)
            }
            Log.d("debug", "$photoFile retrieved photoFile")

            val imageView = view.findViewById(R.id.dialog_photo_viewer) as ImageView
            Log.d("debug", "$imageView retrieved imageview")

            updatePhotoForDialog(photoFile, imageView)
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }


private fun updatePhotoForDialog(photoFile: File?, imageView: ImageView) {
    Log.d("debug", "updatePhotoForDialog called $imageView, $photoFile")
    if (photoFile != null && photoFile.exists()) {
        Log.d("debug", "photoFile called inside updatePhotoForDialog: $photoFile")
            imageView.doOnLayout {
                Log.d("debug", "doOnLayout: $photoFile")
                val scaledBitmap = getScaledBitmap(
                    photoFile.path,
                    imageView.width,
                    imageView.height
                )
                imageView.setImageBitmap(scaledBitmap)
                Log.d("debug", "setImageBitmap error")
            }
        } else {
            imageView.setImageBitmap(null)
            Log.d("debug", "updatePhotoForDialog error")
        }
    }
}


















//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        return activity?.let { it ->
//
//
//            // Use the Builder class for convenient dialog construction
//            val builder = AlertDialog.Builder(it)
//
//            //get the layout inflater
//            val inflater = requireActivity().layoutInflater
//
//            //get a dialog picture view reference
//            // Pass null as the parent view because its going in the dialog layout
//            val view = inflater.inflate(R.layout.dialog_photo, null)
//
//            // Inflate and set the layout for the dialog
//            builder.setView(view)
//
//            //get reference to crimePicture image view
//            val plantPicture = view.findViewById(R.id.dialog_photo_viewer) as ImageView
//
//            //get the image file path argument
//            val photoFile = arguments?.getString(ARG_IMAGE)
//            val photoK = File(requireContext().applicationContext.filesDir, photoFile)
//            val photoUri = FileProvider.getUriForFile(
//                requireContext(),
//                "com.example.greenspot.fileprovider",
//                photoK
//            )
//            Log.d("Debug", "Received photoFile path: $photoFile")
//
//            //get the scaled image
//            val plantDetailFragment =
//                requireActivity().supportFragmentManager.findFragmentById(R.id.dialog_photo_viewer)
//            val destWidth = plantDetailFragment?.view?.width ?: 0
//            val destHeight = plantDetailFragment?.view?.height ?: 0
//            Log.d("Debug", "Received photoFile path for scaled image: $photoFile")
//            val bitmap = photoFile?.let { getScaledBitmap(it, destWidth, destHeight) }
//            Log.d("Debug", "Bitmap set successfully. $bitmap")
//
//            //set the picture in the crimePicture view
//            plantPicture.setImageBitmap(bitmap)
//
//
//            //set the dialog characteristics
//            builder.setTitle(R.string.plant_title_label)
//                .setNegativeButton(R.string.dismiss) { _, _ -> dialog?.cancel() }
//
//
//            // Create the AlertDialog object and return it
//            builder.create()
//
//
//        } ?: throw IllegalStateException("Acitivity cannot be null")
//
//    }
//}
//    companion object {
//        fun newInstance(photoFile: String): PictureDialogFragment {
//            val args = Bundle().apply { putSerializable(ARG_IMAGE, photoFile) }
//
//            return PictureDialogFragment().apply { arguments = args }
//
//        }
//    }
//
//}