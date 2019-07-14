package com.sanmiaderibigbe.snap2pay.ui.registration


import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.api.Status
import com.sanmiaderibigbe.snap2pay.ui.adapter.BankaccountSpinnerAdapter
import com.sanmiaderibigbe.snap2pay.ui.utils.FormValidation
import com.sanmiaderibigbe.snap2pay.ui.utils.getString
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.bank_details_form.*
import kotlinx.android.synthetic.main.fragment_registrationbank.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 *
 */
class RegistrationbankFragment : Fragment() {

    private lateinit var navController: NavController
    private val sharedViewModel by inject<RegistrationPersonalViewModel>()
    private val viewModel by inject<RegistrationBankViewModel>()
    private val args: RegistrationbankFragmentArgs by navArgs()
    private lateinit var progressBar: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrationbank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = initSpinner()
        navController = findNavController()
        progressBar = ProgressDialog(activity)

        btn_register.setOnClickListener {
            val accountName = txt_input_name_on_account
            val accountNumber = txt_input_account_number
            val bvn = txt_input_bvn
            val bankAccount = txt_input_bank_name

            val validatonResult =
                FormValidation.validateBankInput(context!!, accountName, accountNumber, bvn, bankAccount)

            when {
                validatonResult -> {

                    val updatedUser = args.user.copy(
                        nameOnAccount = accountName.getString(),
                        accountNumber = accountNumber.getString(),
                        bvn = bvn.getString(),
                        bankAccount = bankAccount.getString(),
                        accountType = spinner.selectedItem.toString()
                    )

                    viewModel.registerUser(updatedUser, args.password)
                    viewModel.getRegisterResource().observe(viewLifecycleOwner,
                        Observer {

                            when(it.status) {
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
                                    if (it.data == true) {
                                        navController.navigate(R.id.homeFragment)
                                    }
                                }
                            }
                        })


                }
            }
        }
    }

    private fun initSpinner(): Spinner {
        return BankaccountSpinnerAdapter.initAdapter(context!!, spinner_account_type)
    }


    private fun initLoadingDialog() {
        progressBar.setTitle("Signing up...")
        progressBar.show()
    }

    private fun stopLoadingDialog() {
        progressBar.cancel()
    }


}
