package com.sanmiaderibigbe.snap2pay.ui.utils

import android.content.Context
import com.google.android.material.textfield.TextInputLayout
import com.sanmiaderibigbe.snap2pay.R

object FormValidation {
    fun validateBankInput(
        context: Context,
        nameOnAccount: TextInputLayout,
        accountNumber: TextInputLayout,
        BVN: TextInputLayout,
        bankAccount: TextInputLayout
    ): Boolean {

        val isNameValid = isTextNotBlank(nameOnAccount.getData())
        val isAccountNumberValid = isBankNumberValid(accountNumber.getData())
        val isBVNvalid = isBVNNumberValid(BVN.getData())
        val isBankAccount = isTextNotBlank(bankAccount.getData())



        when {
            isNameValid -> {
                nameOnAccount.error = null
            }
            else -> {
                nameOnAccount.error = context.getString(R.string.name_error)
            }
        }

        when {
            isAccountNumberValid -> {
                accountNumber.error = null
            }
            else -> {
                accountNumber.error = context.getString(R.string.account_number_error)
            }
        }

        when {
            isBVNvalid -> {
            }
            else -> {
                BVN.error = context.getString(R.string.bvn_error)
            }
        }

        when {
            isBankAccount -> {
                bankAccount.error = null
            }
            else -> {
                bankAccount.error = context.getString(R.string.bank_account_error)
            }
        }

        return isAccountNumberValid && isBVNvalid && isBankAccount && isNameValid
    }

    fun validatePersonalFormInput(
        context: Context,
        fullName: TextInputLayout,
        email: TextInputLayout,
        phoneNumber: TextInputLayout,
        password: TextInputLayout,
        verifyPassword: TextInputLayout
    ): Boolean {

        val isFullNameValid = isTextNotBlank(fullName.getData())
        val isEmailValid = isEmailValid(email.getData())
        val isPhoneNumberValid = isPhoneNumberValid(phoneNumber.getData())
        val isPasswordValid = isPasswordValid(password.getData())
        val isVerifyPasswordValid = isPasswordValid(verifyPassword.getData())
        val isPasswordVerified = isPasswordVerified(password.getData(), verifyPassword.getData())



        when {
            isFullNameValid -> {
                fullName.error = null
            }
            else -> fullName.error = context.getString(R.string.name_error)
        }

        when {
            isEmailValid -> {
                email.error = null
            }
            else -> email.error = context.getString(R.string.email_error)

        }

        when {
            isPhoneNumberValid -> {
                phoneNumber.error = null
            }
            else -> phoneNumber.error = context.getString(R.string.phone_number_error)

        }

        when {
            isPasswordValid -> {
                password.error = null
            }
            else -> password.error = context.getString(R.string.password_error)
        }

        when {
            isVerifyPasswordValid -> {
                verifyPassword.error = null

                when {
                    isPasswordVerified -> {
                        verifyPassword.error = null
                    }
                    else -> {
                        verifyPassword.error = context.getString(R.string.password_not_the_same_error)
                    }
                }
            }
            else -> {
                verifyPassword.error = context.getString(R.string.password_error)
            }
        }

        return isEmailValid && isFullNameValid && isPasswordValid && isVerifyPasswordValid && isPhoneNumberValid && isPasswordVerified
    }


}