package com.sanmiaderibigbe.snap2pay.ui.utils

import android.text.Editable

fun isPasswordValid(text: Editable?): Boolean {
    return text != null && text.length >= 8
}


fun isEmailValid(text: Editable?) : Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(text?.trim()).matches()
}

fun isTextNotBlank(text: Editable?) : Boolean {
    return !text?.trim()?.isBlank()!!
}

fun isPhoneNumberValid(text: Editable?) : Boolean {
    return text.toString().trim().matches("^[0]\\d{10}\$".toRegex())
}

fun isPasswordVerified(password: Editable?, verifyPassword : Editable? ) : Boolean{
    return password.toString().trim().contentEquals(verifyPassword.toString())
}

fun isBankNumberValid(text: Editable?) : Boolean {
    return text != null && text.trim().length == 10
}

fun isBVNNumberValid(text: Editable?) : Boolean {
    return text != null && text.trim().length == 11
}

fun isCCVValid(text: Editable?): Boolean {
    return text != null && text.trim().length == 4
}

fun isDayValid(text: Editable?): Boolean {
    return text != null && text.trim().length == 2
}

fun isCardNumberValid(text: Editable?): Boolean {
    return text != null && (text.trim().length == 16 || text.trim().length == 19)
}

fun isAmountChargedValid(text: Editable?): Boolean {
    return text != null && text.toString().trim().matches("[0-9]+".toRegex())
}
