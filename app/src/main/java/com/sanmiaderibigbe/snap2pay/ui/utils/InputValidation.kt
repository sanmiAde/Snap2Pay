package com.sanmiaderibigbe.snap2pay.ui.utils

import android.text.Editable

fun isPasswordValid(text: Editable?): Boolean {
    return text != null && text.length >= 8
}


fun isEmailValid(text: Editable?) : Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
}

fun isTextNotBlank(text: Editable?) : Boolean {
    return !text?.isBlank()!!
}

fun isPhoneNumberValid(text: Editable?) : Boolean {
    return text.toString().matches("^[0]\\d{10}\$".toRegex())
}

fun isPasswordVerified(password: Editable?, verifyPassword : Editable? ) : Boolean{
    return password.toString().contentEquals(verifyPassword.toString())
}

fun isBankNumberValid(text: Editable?) : Boolean {
    return text != null && text.length == 10
}

fun isBVNNumberValid(text: Editable?) : Boolean {
    return text != null && text.length == 11
}