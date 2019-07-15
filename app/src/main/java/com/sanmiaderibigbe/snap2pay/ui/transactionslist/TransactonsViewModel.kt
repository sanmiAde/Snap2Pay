package com.sanmiaderibigbe.snap2pay.ui.transactionslist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanmiaderibigbe.snap2pay.api.Resource
import com.sanmiaderibigbe.snap2pay.model.Transaction
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import io.reactivex.rxkotlin.subscribeBy

class TransactonsViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val transactionsListLiveData = MutableLiveData<Resource<List<Transaction>>>()
    private val TAG = "TransactionList"

    fun getTransactionList() {
        transactionsListLiveData.value = Resource.loading(null)
        firebaseRepository.getUserTransactions().subscribeBy(
            onComplete = { updateTransactionList(emptyList<Transaction>().toMutableList()) },
            onSuccess = { transactions -> updateTransactionList(transactions) },
            onError = { throwable -> updateTransactionLisError(throwable) }
        )
    }

    private fun updateTransactionLisError(throwable: Throwable) {
        transactionsListLiveData.value = Resource.error(throwable.message, null)
        Log.d(TAG, throwable.message)
    }

    private fun updateTransactionList(it: MutableList<Transaction>) {
        transactionsListLiveData.value = Resource.success(it)
        Log.d(TAG, it.size.toString())
    }

    fun getTransactionsListLiveData(): LiveData<Resource<List<Transaction>>> {
        return transactionsListLiveData
    }
}