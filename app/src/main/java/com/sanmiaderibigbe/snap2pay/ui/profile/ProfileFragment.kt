package com.sanmiaderibigbe.snap2pay.ui.profile


import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.api.Status
import com.sanmiaderibigbe.snap2pay.databinding.FragmentProfileBinding
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.ui.login.LoginViewModel
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {
    private val loginViewModel by viewModel<LoginViewModel>()
    private val viewModel by viewModel<ProfileFragmentViewModel>()
    private lateinit var binding: FragmentProfileBinding
    private lateinit var progressBar: ProgressDialog
    private var user: User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        progressBar = ProgressDialog(activity)


        gerUserInfo()

        txt_personal_details.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToUpdateUserDetailsFragment(user))
        }

        txt_transaction_history.setOnClickListener {
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToTrasnsactionsFragment())
        }

        txt_log_out.setOnClickListener {
            loginViewModel.signout()
            navController.navigate(ProfileFragmentDirections.actionProfileFragmentToUnauthenticatedFragment())
        }

    }

    private fun gerUserInfo() {
        viewModel.getUserData()
        viewModel.getUserResource().observe(viewLifecycleOwner, Observer {
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
                    if (it.data != null) {
                        binding.user = it.data
                        user = it.data
                    }
                }
            }
        })
    }

    private fun initLoadingDialog() {

        progressBar.setTitle("Retrieving user data...")
        progressBar.show()

    }

    private fun stopLoadingDialog() {
        progressBar.cancel()

    }
}
