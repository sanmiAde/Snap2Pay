package com.sanmiaderibigbe.snap2pay.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanmiaderibigbe.snap2pay.api.Resource
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import io.reactivex.rxkotlin.subscribeBy

class ProfileFragmentViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val userStateLiveData = MutableLiveData<Resource<User>>()

    /***
     * get user data
     */
    fun getUserData() {
        userStateLiveData.value = Resource.loading(null)
        firebaseRepository.getUserData().subscribeBy(
            onSuccess = { it -> updateUserResource(it) },
            onError = { it -> updateUserErrorState(it) }
        )
    }

    private fun updateUserResource(user: User): MutableLiveData<Resource<User>> {

        userStateLiveData.value = Resource.success(user)
        return userStateLiveData
    }

    private fun updateUserErrorState(throwable: Throwable): MutableLiveData<Resource<User>> {
        userStateLiveData.value = Resource.error(throwable.message, null)
        return userStateLiveData

    }

    fun getUserResource(): LiveData<Resource<User>> {
        return userStateLiveData
    }
}