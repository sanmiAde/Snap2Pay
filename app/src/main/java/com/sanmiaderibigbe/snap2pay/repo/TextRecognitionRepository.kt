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

/***
 * TextRecognitionRepository handles all api transactions with firebase ML kit.
 * @param Application application context is passed here to handle file manipulation
 * @param visionImage Firebase Vision image to create firebase bitmaps.
 * @param detector Firebase Text recogniser object.
 */

class TextRecognitionRepository(
    private val application: Application, private val visionImage: VisionImage,
    private val detector: FirebaseVisionTextRecognizer
) {

    /***
     * Creates bitmap object from image file.
     * @param bitmap Bitmap from camera file.
     * @return Maybe observable containing FirebaseVisionText
     */
    fun initTextRecognisation(bitmap: Bitmap): Maybe<FirebaseVisionText> {
        val cardImage = visionImage.imageFromBitmap(bitmap)
        return RxML.initProcessImage(detector, cardImage!!)
    }

    /***
     * Create Empty image file for camera intent
     * @return Empty image file.
     */
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


    /***
     * Camera intent for some reason returns rotated image.
     * To properly rotate it for the vision text algorithm, we rotate it here.
     *
     * @param path to image file on disk.
     * @return rotated image value.
     */
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

    /***
     * Gets uri of image file on disk
     * @return uri of image file.
     */
    fun getFileUri(file: File): Uri {

        return FileProvider.getUriForFile(application, application.getString(R.string.image_provider), file)
    }

    /***
     * Creates bitmap from image file.
     * @return created bitmap.
     */
    fun getImageBitmap(imagePath: String): Bitmap? {
        val file = File(imagePath)
        return MediaStore.Images.Media.getBitmap(application.contentResolver, Uri.fromFile(file))
    }

    /***
     * Gets rotated bitmap.
     *
     * @param bitmap Bitmap from image file
     * @param imagePath path of image.
     *
     * @return rotated bitmap.
     */
    fun getrotatedBitmap(bitmap: Bitmap, imagePath: String): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(initImageRotation(imagePath))

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    /***
     * Image is deleted immediately after text extraction so to prevent any mishap. Scanned user card are not stored on disk.
     * @param fileName name of picture on disk
     */
    fun deleteImage(fileName: String) {
        val dir = application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.canonicalPath
        val file = File("$dir/$fileName")
        file.delete()
    }


}
