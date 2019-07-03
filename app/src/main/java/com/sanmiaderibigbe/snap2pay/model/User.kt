package com.sanmiaderibigbe.snap2pay.model

import android.os.Parcel
import android.os.Parcelable



data class User(
    val fullName : String,
    val emailAddress:  String,
    val phoneNumber: String,
    val nameOnAccount : String = "",
    val bvn : String= "",
    val bankAccount : String = "",
    val accountNumber : String = "",
    val accountType : String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(fullName)
        parcel.writeString(emailAddress)
        parcel.writeString(phoneNumber)
        parcel.writeString(nameOnAccount)
        parcel.writeString(bvn)
        parcel.writeString(bankAccount)
        parcel.writeString(accountNumber)
        parcel.writeString(accountType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
