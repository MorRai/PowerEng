package com.rai.powereng.extensions

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.graphics.drawable.toBitmap
import coil.Coil
import coil.request.ImageRequest
import coil.request.Parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun Uri.compressAndOptimizeImage(context: Context): Uri? {
    // Load the original image using Coil
    val request = ImageRequest.Builder(context)
        .data(this)
        .build()
    val drawable = Coil.imageLoader(context).execute(request).drawable ?: return null
    // Create a new ImageRequest to create a downsized version of the image
    val downsizedRequest = request.newBuilder()
        .size(1280, 1280 * drawable.intrinsicHeight / drawable.intrinsicWidth)
        .scale(coil.size.Scale.FILL)
        .parameters(Parameters.Builder().set("format", "jpeg").build())
        .build()

    // Use Coil to create a downsized version of the image in memory
    val bitmap = Coil.imageLoader(context).execute(downsizedRequest).drawable?.toBitmap() ?: return null

    // Save the downsized image to a temporary file
    val outputFile = withContext(Dispatchers.IO) {
        File.createTempFile("image_", ".jpg", context.cacheDir)
    }
    outputFile.outputStream().use { outputStream ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
    }
    // Return the URI of the downsized image
    return Uri.fromFile(outputFile)
}