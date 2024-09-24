package com.charlesmuchogo.research.presentation.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import java.io.ByteArrayOutputStream
import java.io.File

class ImagePicker(private val context: Context, private val activity: ComponentActivity) {

    private lateinit var takePicture: ManagedActivityResultLauncher<Void?, Bitmap?>
    private lateinit var cropImageLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
    private lateinit var permissionLauncher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>

    @Composable
    fun RegisterPicker(onImagePicked: (ByteArray) -> Unit) {
        permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == true) {
                takePicture.launch()
            } else {
                Toast.makeText(context, "Allow Camera permissions to continue", Toast.LENGTH_LONG).show()
            }
        }


        takePicture = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
            onResult = { newImage ->
                newImage?.let {
                    // Save the captured image temporarily
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()

                    // Proceed to crop the image
                    val imageUri = getImageUriFromByteArray(byteArray)
                    startCrop(imageUri)
                }
            }
        )

        // Initialize cropImageLauncher for handling cropped images
        cropImageLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { intent ->
                    val croppedUri = UCrop.getOutput(intent)
                    croppedUri?.let {
                        // Convert cropped image to ByteArray and pass it back
                        val inputStream = context.contentResolver.openInputStream(it)
                        val byteArray = inputStream?.readBytes()
                        byteArray?.let { croppedBytes ->
                            onImagePicked(croppedBytes)
                        }
                    }
                }
            }
        }
    }



    fun captureImage() {
        if (allPermissionsGranted()) {
            takePicture.launch()
        } else {
            // Request permissions
            permissionLauncher.launch(arrayOf(
                Manifest.permission.CAMERA
            ))
        }
    }

    private fun allPermissionsGranted(): Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val storagePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return cameraPermission == PackageManager.PERMISSION_GRANTED &&
                storagePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun startCrop(sourceUri: Uri) {
        // Define destination URI where the cropped image will be saved
        val destinationUri = Uri.fromFile(File(context.cacheDir, "croppedImage.png"))

        val options = UCrop.Options()
        options.setCompressionQuality(100)
        options.setMaxBitmapSize(10000)


        options.setCompressionFormat(Bitmap.CompressFormat.PNG)

        // Start the UCrop activity
        val uCrop = UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(4f, 3f)
            .withOptions(options)
            .getIntent(context)

        cropImageLauncher.launch(uCrop)
    }

    // Helper function to convert ByteArray to Uri
    private fun getImageUriFromByteArray(byteArray: ByteArray): Uri {
        val tempFile = File.createTempFile("capturedImage", ".png", context.cacheDir)
        tempFile.writeBytes(byteArray)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            tempFile
        )
    }
}

