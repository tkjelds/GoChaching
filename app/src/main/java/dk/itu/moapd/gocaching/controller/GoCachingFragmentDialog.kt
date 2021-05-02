package dk.itu.moapd.gocaching.controller

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import dk.itu.moapd.gocaching.GeoCache
import dk.itu.moapd.gocaching.R
import dk.itu.moapd.gocaching.controller.GoCachingFragment.Companion.adapter
import dk.itu.moapd.gocaching.controller.LoginFragment.Companion.geoCacheVM
import java.io.File

class GoCachingFragmentDialog(geocache: GeoCache, pos: Int, lon: Double, lat: Double) : DialogFragment() {
    val geocache = geocache
    val pos = pos
    val lat = lat
    val lon = lon
    private lateinit var photoFile: File
    private lateinit var photoURI: Uri
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        photoFile = geoCacheVM.getPhotoFile(geocache)
        photoURI = FileProvider.getUriForFile(requireActivity(),
                "dk.itu.moapd.gocaching.fileprovider",
                photoFile)
        return activity?.let {
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setMessage(R.string.geocache_menu)
                    .setPositiveButton(R.string.capture,
                            DialogInterface.OnClickListener { dialog, id ->
                                val packageManager: PackageManager = requireActivity().packageManager

                                val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                val resolvedActivity: ResolveInfo? =
                                        packageManager.resolveActivity(captureImage,
                                                PackageManager.MATCH_DEFAULT_ONLY)
                                if (resolvedActivity == null) {
                                    dialog.dismiss()
                                }
                                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

                                val cameraActivities: List<ResolveInfo> =
                                        packageManager.queryIntentActivities(captureImage,
                                                PackageManager.MATCH_DEFAULT_ONLY)
                                for (cameraActivity in cameraActivities) {
                                    requireActivity().grantUriPermission(
                                            cameraActivity.activityInfo.packageName,
                                            photoURI,
                                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                                    )
                                }
                                startActivityForResult(captureImage, 2)
                            })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        adapter.notifyDataSetChanged()
        super.onDestroyView()
    }
}