package com.sanmiaderibigbe.snap2pay.ui.registration


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.ui.adapter.BankaccountSpinnerAdapter
import kotlinx.android.synthetic.main.fragment_registrationbank.*
import android.widget.Spinner
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.sanmiaderibigbe.snap2pay.model.User
import com.sanmiaderibigbe.snap2pay.repo.Status
import com.sanmiaderibigbe.snap2pay.ui.login.LoginViewModel
import com.sanmiaderibigbe.snap2pay.ui.utils.*
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
                    //TODO pass user via navigation components. it not worth the cost.
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
                                Status.LOADED -> TODO()
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
        //        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//
//                Toast.makeText(
//                    activity,
//                    "Selected : " + BankaccountSpinnerAdapter.accountType.get(position),
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//
//            }
//        }

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

//TODO extract text to string resources.

        when {
            isNameValid -> {
                nameOnAccount.error = null
            }
            else -> {
                nameOnAccount.error = "Name can not be empty"
            }
        }

        when {
            isAccountNumberValid -> {
                accountNumber.error = null
            }
            else -> {
                accountNumber.error = "Account number is invalid"
            }
        }

        when {
            isBVNvalid -> {
                BVN.error = null
            }
            else -> {
                BVN.error = "BVN is invalid"
            }
        }

        when {
            isBankAccount -> {
                bankAccount.error = null
            }
            else -> {
                bankAccount.error = "Bank account is invalid"
            }
        }

        return isAccountNumberValid && isBVNvalid && isBankAccount && isNameValid
    }


}
