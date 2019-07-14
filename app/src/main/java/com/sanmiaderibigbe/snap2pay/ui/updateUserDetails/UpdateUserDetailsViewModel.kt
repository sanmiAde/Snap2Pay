package com.sanmiaderibigbe.snap2pay.ui.updateUserDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanmiaderibigbe.snap2pay.api.Resource
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import io.reactivex.rxkotlin.subscribeBy

class UpdateUserDetailsViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val userStateLiveData = MutableLiveData<Resource<Boolean>>()

    fun uploadUpdateDetails(user: User) {
        userStateLiveData.value = Resource.loading(null)
        firebaseRepository.uploadUserData(user).subscribeBy(
            onComplete = { updateUserResource() },
            onError = { throwable -> updateUserError(throwable) }
        )
    }

    fun getUserLiveData(): LiveData<Resource<Boolean>> {
        return userStateLiveData
    }

    private fun updateUserError(error: Throwable) {
        userStateLiveData.value = Resource.error(error.message, false)
    }

    private fun updateUserResource() {
        userStateLiveData.value = Resource.success(true)
    }
}