package com.sanmiaderibigbe.snap2pay.ui.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanmiaderibigbe.snap2pay.model.User

class RegistrationPersonalViewModel() : ViewModel() {

    private val userLiveData = MutableLiveData<User>()

    fun update(user: User){
        userLiveData.value = user
    }

    fun getUserLiveData() : MutableLiveData<User> {
        return userLiveData
    }
}