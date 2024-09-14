package com.charlesmuchogo.research.presentation.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class ImagePicker {
    private lateinit var getContent: ActivityResultLauncher<Intent>
    private lateinit var takePicture: ManagedActivityResultLauncher<Void?, Bitmap?>
    private lateinit var context: Context

    @Composable
    fun RegisterPicker(onImagePicked: (ByteArray) -> Unit) {
        val activity = LocalContext.current as ComponentActivity
        context = LocalContext.current

        getContent = rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                data?.data?.let { uri ->
                    activity.contentResolver.openInputStream(uri)?.use {
                        onImagePicked(it.readBytes())
                    }
                }
            }
        }

        takePicture = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
            onResult = { newImage ->
                newImage?.let {
                    val byteArrayOutputStream = ByteArrayOutputStream()
                    it.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                    val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
                    onImagePicked(byteArray)
                }
            }
        )
    }

    fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getContent.launch(intent)
    }


    fun captureImage() {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        )
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            takePicture.launch()
        } else {
            requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.CAMERA),
                123
            )
        }
    }
}