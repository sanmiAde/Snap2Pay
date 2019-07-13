package com.sanmiaderibigbe.snap2pay.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanmiaderibigbe.snap2pay.api.Resource
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.repo.FirebaseRepository
import io.reactivex.rxkotlin.subscribeBy

class RegistrationBankViewModel(private val firebaseRepository: FirebaseRepository) : ViewModel() {

    private val registrationSuccessFull = MutableLiveData<Resource<Boolean>>()

    /***
     * Register user to snap 2 pay.
     * @param user Created user with details from form.
     * @param password user password.
     */
    fun registerUser(user: User, password: String) {
        registrationSuccessFull.value = Resource.loading(false)

        firebaseRepository.registerUser(user.emailAddress, password).subscribeBy(
            onSuccess = {

                firebaseRepository.sendEmailVerification().subscribeBy(
                    onComplete = {
                        firebaseRepository.uploadUserData(user).subscribeBy(
                            onComplete = {

                                registrationSuccessFull.value = Resource.success(true)

                            },
                            onError = { throwable ->
                                registrationSuccessFull.value = Resource.error(throwable.localizedMessage, false)
                            }
                        )
                    })

            },
            onError = { throwable ->
                registrationSuccessFull.value = Resource.error(throwable.localizedMessage, false)
            }
        )

    }

    /***
     * get user registration state
     * @return live data containing if user was registered.
     */
    fun getRegisterResource(): LiveData<Resource<Boolean>> {
        return registrationSuccessFull
    }

}