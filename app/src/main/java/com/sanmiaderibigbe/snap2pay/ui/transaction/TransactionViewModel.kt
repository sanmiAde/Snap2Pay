package com.sanmiaderibigbe.snap2pay.ui.transaction

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import co.paystack.android.model.Charge
import com.sanmiaderibigbe.snap2pay.api.PaystackTransactionResource
import com.sanmiaderibigbe.snap2pay.api.Resource
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import com.sanmiaderibigbe.snap2pay.repo.PaystackRepository
import io.reactivex.rxkotlin.subscribeBy
import java.lang.ref.WeakReference

class TransactionViewModel(
    private val paystackRepository: PaystackRepository,
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val transactionStateLiveData = MutableLiveData<PaystackTransactionResource>()
    private val userStateLiveData = MutableLiveData<Resource<User>>()

    fun performTransaction(activityRef: WeakReference<FragmentActivity>, charge: Charge) {
        transactionStateLiveData.value = PaystackTransactionResource.loading()
        paystackRepository.initPaystackTransaction(activityRef, charge).subscribeBy(
            onNext = { it -> updateTransactionState(it) },
            onError = { it -> updateErrorState(it) })
    }

    private fun updateErrorState(it: Throwable) {
        transactionStateLiveData.value = PaystackTransactionResource.error(null, it)
    }


    private fun updateTransactionState(it: PaystackTransactionResource) {
        transactionStateLiveData.value = it
    }

    fun getUserData() {
        firebaseRepository.getUserData().subscribeBy(
            onSuccess = { it -> updateUserResource(it) },
            onError = { it -> updateUserErrorState(it) }
        )
    }

    private fun updateUserErrorState(throwable: Throwable): MutableLiveData<Resource<User>> {
        userStateLiveData.value = Resource.error(throwable.message, null)
        return userStateLiveData

    }

    fun getTransactionState(): LiveData<PaystackTransactionResource> {
        return transactionStateLiveData
    }

    private fun updateUserResource(user: User): MutableLiveData<Resource<User>> {
        userStateLiveData.value = Resource.success(user)
        return userStateLiveData
    }

}