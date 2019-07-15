package com.sanmiaderibigbe.snap2pay.ui.transactionslist


import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanmiaderibigbe.snap2pay.R
import com.sanmiaderibigbe.snap2pay.api.Status
import com.sanmiaderibigbe.snap2pay.ui.adapter.TransactionsListAdapter
import es.dmoral.toasty.Toasty
import org.koin.androidx.viewmodel.ext.android.viewModel


class TrasnsactionsFragment : Fragment() {

    private val viewModel by viewModel<TransactonsViewModel>()
    private lateinit var progressBar: ProgressDialog
    private lateinit var adapter: TransactionsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trasnsactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = initRecyclerView(view)
        progressBar = ProgressDialog(activity)

        getUserTransactions()
    }

    private fun initRecyclerView(view: View): TransactionsListAdapter {
        val adapter = TransactionsListAdapter(this.context!!)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recy_transactions)
        recyclerView.layoutManager = LinearLayoutManager(this.context!!)
        recyclerView.adapter = adapter

        return adapter
    }

    private fun getUserTransactions() {
        viewModel.getTransactionList()
        viewModel.getTransactionsListLiveData().observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                Status.LOADING -> {
                    initLoadingDialog()
                }
                Status.ERROR -> {
                    stopLoadingDialog()
                    Toasty.error(
                        context!!,
                        it.message.toString(),
                        Toast.LENGTH_SHORT,
                        true
                    ).show()
                }
                Status.LOADED -> {
                }
                Status.SUCCESS -> {
                    stopLoadingDialog()
                    if (it.data?.isEmpty()!!) {
                        Toasty.info(context!!, "You have conducted zero transactions", Toast.LENGTH_SHORT, true).show();
                    } else {
                        adapter.setUserTransactions(it.data)
                    }

                }
            }


        })
    }

    private fun initLoadingDialog() {
        progressBar.setTitle("Processing Transaction...")
        progressBar.show()

    }

    private fun stopLoadingDialog() {
        progressBar.cancel()

    }

}
