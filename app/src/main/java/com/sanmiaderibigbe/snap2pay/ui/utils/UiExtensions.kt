package com.sanmiaderibigbe.snap2pay.ui.utils

import android.text.Editable
import com.google.android.material.textfield.TextInputLayout

 fun TextInputLayout.getData(): Editable? {
    return this.editText?.text
}

 fun TextInputLayout.getString(): String {
    return this.editText?.text.toString()
}