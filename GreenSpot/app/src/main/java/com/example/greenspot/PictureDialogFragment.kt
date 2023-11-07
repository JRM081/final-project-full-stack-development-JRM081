package com.example.greenspot

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import java.io.File

const val ARG_IMAGE = "ARG_IMAGE"

class PictureDialogFragment: DialogFragment() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {


            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)

            //get the layout inflater
            val inflater = requireActivity().layoutInflater

            //get a dialog picture view reference
            // Pass null as the parent view because its going in the dialog layout
            val view = inflater.inflate(R.layout.dialog_photo, null)

            // Inflate and set the layout for the dialog
            builder.setView(view)

            //get reference to crimePicture image view
            val plantPicture = view.findViewById(R.id.dialog_photo_viewer) as ImageView

            //get the image file path argument
            val photoFile = arguments?.getSerializable(ARG_IMAGE, File::class.java)
            Log.d("Debug", "Received photoFile path: $photoFile")

            //get the scaled image
            val plantDetailFragment = requireActivity().supportFragmentManager.findFragmentById(R.id.dialog_photo_viewer)
            val destWidth = plantDetailFragment?.view?.width ?: 0
            val destHeight = plantDetailFragment?.view?.height ?: 0
            val bitmap = photoFile?.let { it1 -> getScaledBitmap(it1.path, destWidth, destHeight) }
            Log.d("Debug", "Bitmap set successfully. $bitmap")

            //set the picture in the crimePicture view
            plantPicture.setImageBitmap(bitmap)


            //set the dialog characteristics
            builder.setTitle(R.string.plant_title_label)
                .setNegativeButton(R.string.dismiss) { _, _ -> dialog?.cancel() }


            // Create the AlertDialog object and return it
            builder.create()



        } ?: throw IllegalStateException("Acitivity cannot be null")

    }



    companion object {
        fun newInstance(photoFile: String): PictureDialogFragment {
            val args = Bundle().apply { putSerializable(ARG_IMAGE, photoFile) }

            return PictureDialogFragment().apply { arguments = args }

        }
    }

}