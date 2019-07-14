package com.sanmiaderibigbe.snap2pay.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthResult
import com.sanmiaderibigbe.snap2pay.api.Resource
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import io.reactivex.rxkotlin.subscribeBy

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

    fun signIn(email: String, password: String) {

        loginResource.value = Resource.loading(AuthenticationState.UNAUTHENTICATED)
        
        firebaseRepository.signIn(email, password).subscribeBy(
            onSuccess = {authResult -> updateAuthenticationState(authResult)  },
            
            onError = {throwable -> updateLoginError(throwable) }
        )
    }

    private fun updateLoginError(throwable: Throwable) {

        loginResource.value = Resource.error(throwable.message, AuthenticationState.UNAUTHENTICATED)
    }

    private fun updateAuthenticationState(authResult: AuthResult?) {
        when {
            authResult?.user != null -> loginResource.value = Resource.success(AuthenticationState.AUTHENTICATED)
            else -> loginResource.value = Resource.error("Failed to Login", AuthenticationState.UNAUTHENTICATED)
        }
    }

    fun signout() {
        firebaseRepository.signOut()
        loginResource.value = Resource.loaded(AuthenticationState.UNAUTHENTICATED)
    }

    fun isEmailVerified(): Boolean? {
        return firebaseRepository.isEmailVerified()
    }

    fun reloadUser() {
        firebaseRepository.getCurrentUser()?.reload()
    }


}