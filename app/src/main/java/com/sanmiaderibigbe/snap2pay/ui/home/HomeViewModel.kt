package com.sanmiaderibigbe.snap2pay.ui.home

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.repo.Resource
import com.sanmiaderibigbe.snap2pay.repo.TextRecognitionRepository
import io.reactivex.rxkotlin.subscribeBy
import java.io.File


class HomeViewModel(private val app: Application, private val textRecognitionRepository: TextRecognitionRepository) :
    AndroidViewModel(app) {

    private val textRecognitionResource = MutableLiveData<Resource<String?>>()

    fun createImageFile(): File? {

        return textRecognitionRepository.createImageFile()
    }

    fun getImageFileUri(file: File): Uri {

        return textRecognitionRepository.getFileUri(file)
    }

    fun getRotatedBitmap(imagePath: String): Bitmap? {

        val imageBitmap = textRecognitionRepository.getImageBitmap(imagePath)
        return textRecognitionRepository.getrotatedBitmap(imageBitmap!!, imagePath)

    }

    fun getTextFromCard(bitmap: Bitmap) {
        textRecognitionResource.value = Resource.loading("")
        textRecognitionRepository.initTextRecognisation(bitmap).subscribeBy(
            onSuccess = { firbaseVisionText: FirebaseVisionText ->
                processTextRecognition(firbaseVisionText)
            },
            onError = { throwable: Throwable ->
                updateError(throwable)

            }

        )
    }

    private fun updateError(throwable: Throwable) {
        textRecognitionResource.value = Resource.error(throwable.localizedMessage, "")
    }

    private fun processTextRecognition(texts: FirebaseVisionText) {
        var atmCardNumber: String? = ""
        if (texts.textBlocks.isEmpty()) {
            //TODO use error resource
            textRecognitionResource.value =
                Resource.error(app.getString(R.string.text_extraction_error), atmCardNumber)

        }
        texts.textBlocks.forEach { textBlock ->
            run {
                textBlock.lines.forEach { line ->
                    //Sometimes card numbers are lumped into group of fours. Thats why we check to see if there is a line with four elements here. Optimise later.
                    run {
                        if (line.elements.size == 4) {
                            atmCardNumber = line.elements.joinToString("")
                        }
                    }

                    line.elements.forEach { element ->
                        run {
                            if (element.text.length == 19)
                                atmCardNumber = element.text.toString()
                        }

                    }
                }
            }

        }

        //TODO fix bug. Text recognition extract not atm  picturs and shows to the screen
        if (atmCardNumber.isNullOrEmpty()) {
            textRecognitionResource.value =
                Resource.error(app.getString(R.string.text_extraction_error), atmCardNumber)
        } else {
            textRecognitionResource.value = Resource.success(atmCardNumber)
        }
        Log.d("textRecognition", "$atmCardNumber")

    }

    fun getProcessedTextResource(): LiveData<Resource<String?>> {
        return textRecognitionResource
    }

    fun cleanUpImage(fileName: String) {
        textRecognitionRepository.deleteImage(fileName)
    }

}