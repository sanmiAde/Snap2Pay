package com.sanmiaderibigbe.snap2pay.repo

import androidx.fragment.app.FragmentActivity
import co.paystack.android.model.Charge
import com.sanmiaderibigbe.snap2pay.api.PaystackTransactionResource
import com.sanmiaderibigbe.snap2pay.api.RxPaystack
import io.reactivex.Observable
import java.lang.ref.WeakReference

class PaystackRepository {

    fun initPaystackTransaction(
        activityRef: WeakReference<FragmentActivity>,
        charge: Charge
    ): Observable<PaystackTransactionResource> {
        return RxPaystack.initPaymentTransaction(activityRef, charge)
    }
}