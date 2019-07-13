package com.sanmiaderibigbe.snap2pay.ui.registration


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.ui.utils.FormValidation
import com.sanmiaderibigbe.snap2pay.ui.utils.getString
import kotlinx.android.synthetic.main.fragment_registration_personal.*
import kotlinx.android.synthetic.main.personal_detail_form.*
import kotlinx.android.synthetic.main.update_personal_detail_form.*
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

            val validationResult = FormValidation.validatePersonalFormInput(
                context!!,
                fullName,
                email,
                phoneNumber,
                password,
                verifyPassword
            )

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

}
