package com.sanmiaderibigbe.snap2pay.api

import androidx.fragment.app.FragmentActivity
import co.paystack.android.Paystack
import co.paystack.android.PaystackSdk
import co.paystack.android.Transaction
import co.paystack.android.model.Charge
import io.reactivex.Observable
import java.lang.ref.WeakReference

object RxPaystack {

    fun initPaymentTransaction(
        activityRef: WeakReference<FragmentActivity>,
        charge: Charge
    ): Observable<PaystackTransactionResource> {
        return Observable.create<PaystackTransactionResource> { emitter ->
            PaystackSdk.chargeCard(activityRef.get(), charge, object : Paystack.TransactionCallback {
                override fun onSuccess(transaction: Transaction) {
                    if (!emitter.isDisposed) {
                        emitter.onNext(
                            PaystackTransactionResource.success(
                                transaction
                            )
                        )
                    }
                }

                override fun beforeValidate(transaction: Transaction) {
                    if (!emitter.isDisposed) {
                        emitter.onNext(
                            PaystackTransactionResource.beforeValidation(
                                transaction
                            )
                        )
                    }
                }

                override fun onError(error: Throwable, transaction: Transaction) {
                    if (!emitter.isDisposed) {
                        emitter.onNext(
                            PaystackTransactionResource.error(
                                transaction, error
                            )
                        )
                    }
                }
            })
        }
    }
}