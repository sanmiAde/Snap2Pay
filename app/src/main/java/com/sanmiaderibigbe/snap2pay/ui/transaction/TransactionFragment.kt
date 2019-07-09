package com.sanmiaderibigbe.snap2pay.ui.transaction


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.sanmiaderibigbe.snap2pay.R
import kotlinx.android.synthetic.main.fragment_transaction.*


/**
 * A simple [Fragment] subclass.
 *
 */
class TransactionFragment : Fragment() {

    private val args: TransactionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txt_cardNumber.setText(args.atmCardNumber)

    }
}
