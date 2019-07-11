package com.sanmiaderibigbe.snap2pay.ui.registration


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
import com.google.android.material.textfield.TextInputLayout
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.api.Status
import com.sanmiaderibigbe.snap2pay.ui.adapter.BankaccountSpinnerAdapter
import com.sanmiaderibigbe.snap2pay.ui.utils.*
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


        btn_register.setOnClickListener {
            val accountName = txt_input_name_on_account
            val accountNumber = txt_input_account_number
            val bvn = txt_input_bvn
            val bankAccount = txt_input_bank_name

            val validatonResult = validateInput(accountName, accountNumber, bvn, bankAccount)

            when {
                validatonResult -> {

                    val updatedUser = args.user.copy(nameOnAccount = accountName.getString(), accountNumber = accountNumber.getString(), bvn = bvn.getString(), accountType = spinner.selectedItem.toString())

                    viewModel.registerUser(updatedUser, args.password)
                    viewModel.getRegisterResource().observe(viewLifecycleOwner,
                        Observer {

                            when(it.status) {
                                Status.LOADING -> Toast.makeText(activity, "Loading", Toast.LENGTH_SHORT).show()
                                Status.ERROR -> {
                                    if (it.message != null) {
                                        Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                Status.LOADED -> {
                                }
                                Status.SUCCESS -> {
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

    private fun validateInput(
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
                nameOnAccount.error = getString(R.string.name_error)
            }
        }

        when {
            isAccountNumberValid -> {
                accountNumber.error = null
            }
            else -> {
                accountNumber.error = getString(R.string.account_number_error)
            }
        }

        when {
            isBVNvalid -> {
            }
            else -> {
                BVN.error = getString(R.string.bvn_error)
            }
        }

        when {
            isBankAccount -> {
                bankAccount.error = null
            }
            else -> {
                bankAccount.error = getString(R.string.bank_account_error)
            }
        }

        return isAccountNumberValid && isBVNvalid && isBankAccount && isNameValid
    }


}
