package com.sanmiaderibigbe.snap2pay.api

import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionText
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer
import durdinapps.rxfirebase2.RxHandler
import io.reactivex.Maybe
import io.reactivex.MaybeEmitter


object RxML {

    fun initProcessImage(
        detector: FirebaseVisionTextRecognizer,
        firebaseVisionImage: FirebaseVisionImage
    ): Maybe<FirebaseVisionText> {
        return Maybe.create { emitter: MaybeEmitter<FirebaseVisionText> ->
            RxHandler.assignOnTask(emitter, detector.processImage(firebaseVisionImage))
        }
    }


}
