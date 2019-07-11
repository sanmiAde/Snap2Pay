package com.sanmiaderibigbe.snap2pay.ui.transaction


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
import androidx.navigation.fragment.navArgs
import co.paystack.android.model.Card
import co.paystack.android.model.Charge
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.api.PaystackTransactionResource
import com.sanmiaderibigbe.snap2pay.ui.utils.*
import kotlinx.android.synthetic.main.fragment_transaction.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference


/**
 * A simple [Fragment] subclass.
 *
 */
class TransactionFragment : Fragment() {

    private val args: TransactionFragmentArgs by navArgs()
    private lateinit var navController: NavController
    private val viewModel by viewModel<TransactionViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        btn_continue.setOnClickListener {
            val amountCharged = txt_input_amount_charged.editText?.text
            val productDescription = txt_product_description.editText?.text
            val customerEmail = txt_input_customer_email.editText?.text
            val customerCardNumber = txt_input_card_number.editText?.text
            val customerCCV = txt_input_ccv.editText?.text
            val customerCardExpiraryMonth = txt_input_expirary_month.editText?.text
            val customerCardExpiraryYear = txt_input_expirary_day.editText?.text


            val validationResult = validateInput(
                amountCharged,
                productDescription,
                customerEmail,
                customerCardNumber,
                customerCardExpiraryYear,
                customerCardExpiraryMonth,
                customerCCV
            )

            //Create card when user data is valid.
            if (validationResult) {
                val card = Card(
                    customerCardNumber.toString(),
                    customerCardExpiraryMonth.toString().toInt(),
                    customerCardExpiraryYear.toString().toInt(),
                    customerCCV.toString()
                )

                if (card.isValid) {

                    initPayStackTransaction(card, amountCharged, customerEmail, productDescription)

                    viewModel.getTransactionState().observe(viewLifecycleOwner, Observer { transactionResource ->

                        when (transactionResource.status) {
                            PaystackTransactionResource.Status.SUCCESS -> {
                                Toast.makeText(activity, "Transaction successful.", Toast.LENGTH_SHORT).show()
                            }
                            PaystackTransactionResource.Status.ERROR -> {
                                if (transactionResource.error != null) {
                                    Toast.makeText(
                                        activity,
                                        "${transactionResource.error.localizedMessage}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                            PaystackTransactionResource.Status.BEFORE_VALIDATE -> {
                            }
                            PaystackTransactionResource.Status.LOADING -> {
                            }
                        }
                    })

                } else {
                    Toast.makeText(activity, "This  Card is not valid", Toast.LENGTH_SHORT).show()
                }
            }
        }


    }

    private fun initPayStackTransaction(
        card: Card,
        amountCharged: Editable?,
        customerEmail: Editable?,
        productDescription: Editable?
    ) {
        val charge = Charge()
        charge.card = card
        //set amount parameter in kobo.
        charge.amount = amountCharged.toString().toInt() * 100
        charge.email = customerEmail.toString()
        charge.putMetadata("Product Description", productDescription.toString())

        viewModel.performTransaction(WeakReference(requireActivity()), charge)
    }

    private fun validateInput(
        amountCharged: Editable?,
        productDescription: Editable?,
        customerEmail: Editable?,

        customerCardNumber: Editable?,
        customerCardExpiraryDay: Editable?,
        customerCardExpiraryMonth: Editable?,
        customerCCV: Editable?
    ): Boolean {

        val isAmountChargedValid = isAmountChargedValid(amountCharged)
        val isProductDescriptionValid = isTextNotBlank(productDescription)
        val isCustomerEmailValid = isEmailValid(customerEmail)

        val isCardNumberValid = isCardNumberValid(customerCardNumber)
        val isCardExpiraryDayValid = isDayValid(customerCardExpiraryDay)
        val isCardExpirayMonthValid = isDayValid(customerCardExpiraryMonth)
        val isCCVValid = isTextNotBlank(customerCCV)

        when {
            isAmountChargedValid -> txt_input_amount_charged.error = null
            else -> txt_input_amount_charged.error = getString(R.string.amount_error)
        }

        when {
            isProductDescriptionValid -> txt_product_description.error = null
            else -> txt_product_description.error = getString(R.string.product_description_error)
        }

        when {
            isCustomerEmailValid -> txt_input_customer_email.error = null
            else -> txt_input_customer_email.error = getString(R.string.email_error)
        }



        when {
            isCCVValid -> txt_input_ccv.error = null
            else -> txt_input_ccv.error = getString(R.string.ccv_error)
        }
        when {
            isCardExpiraryDayValid -> txt_input_expirary_day.error = null
            else -> txt_input_expirary_day.error = getString(R.string.date_error)
        }

        when {
            isCardExpirayMonthValid -> txt_input_expirary_month.error = null
            else -> txt_input_expirary_month.error = getString(R.string.month_error)
        }

        when {
            isCardNumberValid -> txt_input_card_number.error = null
            else -> txt_input_card_number.error = getString(R.string.card_number_error)
        }

        return isAmountChargedValid && isCustomerEmailValid && isProductDescriptionValid && isCCVValid && isCardExpiraryDayValid && isCardExpirayMonthValid && isCardNumberValid

    }
}
