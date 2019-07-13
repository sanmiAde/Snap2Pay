package com.sanmiaderibigbe.snap2pay.repo

import androidx.fragment.app.FragmentActivity
import co.paystack.android.model.Charge
import com.sanmiaderibigbe.snap2pay.api.PaystackTransactionResource
import com.sanmiaderibigbe.snap2pay.api.RxPaystack
import io.reactivex.Observable
import java.lang.ref.WeakReference

/***
 *  Paystack repository handles all transactions with paystack api.
 *
 */
class PaystackRepository {

    /***
     *
     * Weak reference is passed here because view model are not allowed to hold reference to contexts to avoid memeory leaks.
     * @param activityRef Weak reference  to activity
     * @param charge current transaction charge.
     */
    fun initPaystackTransaction(
        activityRef: WeakReference<FragmentActivity>,
        charge: Charge
    ): Observable<PaystackTransactionResource> {
        return RxPaystack.initPaymentTransaction(activityRef, charge)
    }
}