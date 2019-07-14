package com.sanmiaderibigbe.snap2pay.ui.updateUserDetails


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
import androidx.navigation.fragment.navArgs
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.api.Status
import com.sanmiaderibigbe.snap2pay.databinding.FragmentUpdateUserDetailsBinding
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.ui.utils.FormValidation
import com.sanmiaderibigbe.snap2pay.ui.utils.getString
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.bank_details_form.*
import kotlinx.android.synthetic.main.fragment_update_user_details.*
import kotlinx.android.synthetic.main.update_personal_detail_form.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 *
 */
class UpdateUserDetailsFragment : Fragment() {
    private lateinit var binding: FragmentUpdateUserDetailsBinding
    private val args: UpdateUserDetailsFragmentArgs by navArgs()
    private val viewModel: UpdateUserDetailsViewModel by inject<UpdateUserDetailsViewModel>()
    private lateinit var progressBar: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_update_user_details, container, false
        )
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.user = args.user
        progressBar = ProgressDialog(activity)


        btn_update_user_details.setOnClickListener {
            val accountName = txt_input_name_on_account
            val accountNumber = txt_input_account_number
            val bvn = txt_input_bvn
            val bankAccount = txt_input_bank_name
            val fullName = txt_input_full_name
            val email = txt_input_email
            val phoneNumber = txt_input_mobile_number

            val validationResult = FormValidation.validatepdateUsersDetailsForm(
                context!!,
                accountName,
                accountNumber,
                bvn,
                bankAccount,
                fullName,
                email,
                phoneNumber

            )

            when {
                validationResult -> {
                    val user = args.user
                    val updatedUser = user?.copy(
                        fullName.getString(),
                        email.getString(),
                        phoneNumber.getString(),
                        accountName.getString(),
                        bvn.getString(),
                        bankAccount.getString(),
                        accountNumber.getString()
                    )
                    initUpdateUserDataViewModel(updatedUser)
                }

            }

        }


    }

    private fun initUpdateUserDataViewModel(user: User?) {
        viewModel.uploadUpdateDetails(user!!)

        viewModel.getUserLiveData().observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    initLoadingDialog()
                }
                Status.ERROR -> {
                    stopLoadingDialog()
                    Toasty.error(context!!, it.message.toString(), Toast.LENGTH_SHORT, true).show()
                }
                Status.LOADED -> {
                }
                Status.SUCCESS -> {
                    if (it.data!!) {
                        stopLoadingDialog()
                        Toasty.success(context!!, "User details updated successfully.", Toast.LENGTH_SHORT, true).show()
                        findNavController().navigateUp()

                    }

                }
            }

        })
    }


    private fun initLoadingDialog() {
        progressBar.setTitle("Signing up...")
        progressBar.show()
    }

    private fun stopLoadingDialog() {
        progressBar.cancel()
    }

}
