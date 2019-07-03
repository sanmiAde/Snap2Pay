package com.sanmiaderibigbe.snap2pay.ui.registration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.ui.utils.*
import kotlinx.android.synthetic.main.fragment_registration_personal.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 *
 */
class RegistrationPersonalFragment : Fragment() {

    private lateinit var navController: NavController

    private val viewModel by inject<RegistrationPersonalViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration_personal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        btn_continue.setOnClickListener {
            val fullName = txt_input_full_name
            val email = txt_input_email
            val phoneNumber = txt_input_mobile_number
            val password = txt_input_password
            val verifyPassword = txt_input_verify_password

            val validationResult = validateInput(fullName, email, phoneNumber, password, verifyPassword)

            when {
                validationResult -> {
                    val user = User(
                        fullName.getString(),
                        email.getString(),
                        phoneNumber.getString()

                    )
                    viewModel.update(
                        user
                    )
                    navController.navigate(
                        RegistrationPersonalFragmentDirections.actionRegistrationPersonalFragmentToRegistrationbankFragment(
                            user,
                            password.getString()
                        )
                    )
                }
            }

        }
    }

    private fun validateInput(
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
            else -> fullName.error = "Name can not be empty."
        }

        when {
            isEmailValid -> {
                email.error = null
            }
            else -> email.error = "Email is not valid."

        }

        when {
            isPhoneNumberValid -> {
                phoneNumber.error = null
            }
            else -> phoneNumber.error = "Phone number is not valid."
        }

        when {
            isPasswordValid -> {
                password.error = null
            }
            else -> password.error = "Password is not valid"
        }

        when {
            isVerifyPasswordValid -> {
                verifyPassword.error = null

                when {
                    isPasswordVerified -> {
                        verifyPassword.error = null
                    }
                    else -> {
                        verifyPassword.error = "Password is not the same."
                    }
                }
            }
            else -> {
                verifyPassword.error = "Password is not valid"
            }
        }

        return isEmailValid && isFullNameValid && isPasswordValid && isVerifyPasswordValid && isPhoneNumberValid && isPasswordVerified
    }


}
