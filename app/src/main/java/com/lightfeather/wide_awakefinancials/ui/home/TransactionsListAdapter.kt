package com.lightfeather.wide_awakefinancials.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.ListItemTransactionBinding
import com.lightfeather.wide_awakefinancials.domain.model.ColoredFinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.FinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.TransactionType
import java.text.SimpleDateFormat
import java.util.*


class TransactionsListAdapter(private val items: List<ColoredFinancialTransaction>) :
    RecyclerView.Adapter<TransactionsListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ListItemTransactionBinding.inflate(LayoutInflater.from(parent.context))
        val params: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            MATCH_PARENT, WRAP_CONTENT
        )
        binding.root.layoutParams = params
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    class ViewHolder(private val binding: ListItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(financialTransaction: ColoredFinancialTransaction) {
            with(binding) {
                val transactionImage = if (financialTransaction.type == TransactionType.INCOME)
                    R.drawable.ic_baseline_arrow_upward_24 else
                    R.drawable.ic_baseline_arrow_downward_24
                val date = Date(financialTransaction.creationTime)
                val format = SimpleDateFormat("yyyy/MM/dd hh:mm aaa", Locale.getDefault())

                transactionType.setImageResource(transactionImage)
                transactionDate.text = format.format(date)
                transactionAmount.text = "${financialTransaction.amount} $"
                transactionDescription.text = financialTransaction.description
                transactionContainer.setCardBackgroundColor(android.graphics.Color.parseColor(financialTransaction.categoryColor))
            }
        }

    }
}