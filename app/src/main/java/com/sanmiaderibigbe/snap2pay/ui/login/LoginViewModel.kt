package com.sanmiaderibigbe.snap2pay.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import com.sanmiaderibigbe.snap2pay.repo.Resource

class LoginViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {


    enum class AuthenticationState {
        AUTHENTICATED,
        UNAUTHENTICATED,
    }

    private val loginResource = MutableLiveData<Resource<AuthenticationState>>()

    init {
        when {
            firebaseRepository.getCurrentUser() != null -> loginResource.value = Resource.loaded(AuthenticationState.AUTHENTICATED)
            else -> loginResource.value = Resource.loaded(AuthenticationState.UNAUTHENTICATED)
        }
    }

    fun getLoginResource(): LiveData<Resource<AuthenticationState>>{
        return  loginResource
    }
}