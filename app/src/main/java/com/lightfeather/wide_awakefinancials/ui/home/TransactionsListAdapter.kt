package com.lightfeather.wide_awakefinancials.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.ListItemTransactionBinding
import com.lightfeather.wide_awakefinancials.domain.model.FinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.TransactionType
import java.text.SimpleDateFormat
import java.util.*

class TransactionsListAdapter(private val items: List<FinancialTransaction>) :
    RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ListItemTransactionBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(financialTransaction: FinancialTransaction) {
            with(binding) {
                val transactionImage = if (financialTransaction.type == TransactionType.INCOME)
                    R.drawable.ic_baseline_arrow_upward_24 else
                    R.drawable.ic_baseline_arrow_downward_24
                val date = Date(financialTransaction.creationTime)
                val format = SimpleDateFormat("yyyy/MM/dd hh:mm aaa", Locale.getDefault())

                transactionType.setImageResource(transactionImage)
                transactionDate.text = format.format(date)
                transactionAmount.text = "${financialTransaction.amount}"
                transactionDescription.text = financialTransaction.description
            }
        }

    }
}