package com.sanmiaderibigbe.snap2pay.repo

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.api.RxML
import com.sanmiaderibigbe.snap2pay.api.VisionImage
import io.reactivex.Maybe
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class TextRecognitionRepository(
    private val application: Application, private val visionImage: VisionImage,
    private val detector: FirebaseVisionTextRecognizer
) {

    fun initTextRecognisation(bitmap: Bitmap): Maybe<FirebaseVisionText> {
        val cardImage = visionImage.imageFromBitmap(bitmap)
        return RxML.initProcessImage(detector, cardImage!!)
    }

    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return File.createTempFile(
            imageFileName, /* prefix */
            "", /* suffix */
            storageDir      /* directory */
        )

    }


    private fun initImageRotation(imagePath: String): Float {
        var rotate = 0
        try {
            val imageFile = File(imagePath)
            val exif = ExifInterface(
                imageFile.absolutePath
            )
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return rotate.toFloat()
    }

    fun getFileUri(file: File): Uri {

        return FileProvider.getUriForFile(application, application.getString(R.string.image_provider), file)
    }

    fun getImageBitmap(imagePath: String): Bitmap? {
        val file = File(imagePath)
        return MediaStore.Images.Media.getBitmap(application.contentResolver, Uri.fromFile(file))
    }

    fun getrotatedBitmap(bitmap: Bitmap, imagePath: String): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(initImageRotation(imagePath))

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun deleteImage(fileName: String) {
        val dir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.canonicalPath
        val file = File("$dir/$fileName")
        file.delete()

        val a = 5
    }



}
