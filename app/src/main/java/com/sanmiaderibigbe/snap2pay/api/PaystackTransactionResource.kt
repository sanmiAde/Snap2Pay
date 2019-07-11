package com.sanmiaderibigbe.snap2pay.api

import co.paystack.android.Transaction


class PaystackTransactionResource(val status: Status, val data: Transaction?, val error: Throwable?) {

    companion object {
        fun success(data: Transaction): PaystackTransactionResource {
            return PaystackTransactionResource(
                Status.SUCCESS,
                data,
                null
            )
        }

        fun beforeValidation(data: Transaction): PaystackTransactionResource {
            return PaystackTransactionResource(
                Status.BEFORE_VALIDATE,
                data,
                null
            )
        }

        fun error(data: Transaction?, error: Throwable): PaystackTransactionResource {
            return PaystackTransactionResource(
                Status.ERROR,
                data,
                error
            )
        }

        fun loading(): PaystackTransactionResource {
            return PaystackTransactionResource(Status.LOADING, null, null)
        }
    }


    enum class Status {
        SUCCESS,
        ERROR,
        BEFORE_VALIDATE,
        LOADING
    }
}