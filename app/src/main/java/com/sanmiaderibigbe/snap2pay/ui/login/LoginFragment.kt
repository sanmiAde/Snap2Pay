package com.sanmiaderibigbe.snap2pay.ui.login

import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.api.Status
import com.sanmiaderibigbe.snap2pay.ui.utils.isEmailValid
import com.sanmiaderibigbe.snap2pay.ui.utils.isPasswordValid
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [LoginFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class LoginFragment : Fragment() {

    private val viewModel by viewModel<LoginViewModel>()
    private lateinit var navController: NavController
    private lateinit var progressBar: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        progressBar = ProgressDialog(activity)

        btn_login.setOnClickListener {

            val email = txt_input_email.editText?.text
            val password = text_input_password.editText?.text

            val validationResult = validateInput(email, password)

            if (validationResult.first && validationResult.second) {
                viewModel.signIn(email.toString(), password.toString())
                observeLoginResource()
            }
        }

        txt_register.setOnClickListener {
            navController.navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationPersonalFragment())
        }

    }

    private fun validateInput(email: Editable?, password: Editable?): Pair<Boolean, Boolean> {
        val isEmailValid = isEmailValid(email)
        val isPasswordValid = isPasswordValid(password)

        when {
            isEmailValid -> txt_input_email.error = null
            else -> txt_input_email.error = getString(R.string.invalid_email)
        }

        when {
            isPasswordValid -> text_input_password.error = null
            else -> text_input_password.error = getString(R.string.invalid_password)
        }

        return Pair(isEmailValid, isPasswordValid)
    }


    private fun observeLoginResource() {
        viewModel.getLoginResource().observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                    initLoadingDialog()
                }
                Status.ERROR -> {
                    stopLoadingDialog()
                    if (it.message != null) {

                        Toasty.error(context!!, it.message, Toast.LENGTH_SHORT, true).show()
                    }
                }
                Status.LOADED -> {
                }
                Status.SUCCESS -> {
                    stopLoadingDialog()
                    if (it.data == LoginViewModel.AuthenticationState.AUTHENTICATED) {
                        navController.navigate(R.id.homeFragment)
                    }
                }
            }
        })
    }

    private fun initLoadingDialog() {

        progressBar.setTitle("Login in...")
        progressBar.show()

    }

    private fun stopLoadingDialog() {
        progressBar.cancel()

    }
}
