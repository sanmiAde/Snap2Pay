package com.sanmiaderibigbe.snap2pay.ui.utils

import android.text.Editable

fun isPasswordValid(text: Editable?): Boolean {
    return text != null && text.length >= 8
}


fun isEmailValid(text: Editable?) : Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()
}
