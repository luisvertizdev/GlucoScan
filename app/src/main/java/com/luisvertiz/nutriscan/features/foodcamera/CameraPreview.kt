package com.luisvertiz.nutriscan.features.foodcamera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.io.File

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    takePhoto: Boolean,
    onPhotoCaptured: (Bitmap) -> Unit
) {

    val context: Context = LocalContext.current

    val previewView = remember {
        PreviewView(context)
    }

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    AndroidView(
        modifier = modifier,
        factory = {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview: Preview = Preview.Builder().build()

            preview.surfaceProvider = previewView.surfaceProvider

            cameraProvider.unbindAll()

            cameraProvider.bindToLifecycle(
                context as androidx.lifecycle.LifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )

            previewView
        }
    )

    LaunchedEffect(takePhoto) {
        if (takePhoto) {
            capturePhoto(
                context = context,
                imageCapture = imageCapture,
                onPhotoCaptured = { bitmap -> onPhotoCaptured(bitmap) },
            )
        }
    }
}

private fun capturePhoto(
    context: Context,
    imageCapture: ImageCapture,
    onPhotoCaptured: (Bitmap) -> Unit
) {

    val file = File(
        context.cacheDir,
        "food_${System.currentTimeMillis()}.jpg"
    )

    val outputOptions =
        ImageCapture.OutputFileOptions.Builder(file).build()

    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(
                outputFileResults: ImageCapture.OutputFileResults
            ) {
                val bitmap =
                    BitmapFactory.decodeFile(file.absolutePath)
                onPhotoCaptured(bitmap)
            }

            override fun onError(
                exception: ImageCaptureException
            ) {
                FirebaseCrashlytics.getInstance().recordException(exception)
            }
        }
    )
}