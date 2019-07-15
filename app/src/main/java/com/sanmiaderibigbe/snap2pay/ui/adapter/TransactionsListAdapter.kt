package com.sanmiaderibigbe.snap2pay.ui.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.sanmiaderibigbe.snap2pay.databinding.TransactionItemListBinding
import com.sanmiaderibigbe.snap2pay.model.Transaction

class TransactionsListAdapter(val context: Context) : RecyclerView.Adapter<TrasnactionslistViewHolder>() {

    private var transactionList: List<Transaction>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrasnactionslistViewHolder {
        val binding = TransactionItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return TrasnactionslistViewHolder(binding)
    }

    override fun getItemCount(): Int = transactionList?.size ?: 0


    override fun onBindViewHolder(holder: TrasnactionslistViewHolder, position: Int) {
        val transaction = transactionList?.get(position)
        holder.binding.transaction = transaction
        Toast.makeText(context, "${transaction?.Date}", Toast.LENGTH_SHORT).show()
    }

    fun setUserTransactions(data: List<Transaction>?) {
        transactionList = data
        notifyDataSetChanged()

    }


}