package com.lightfeather.wide_awakefinancials.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.ListItemDateBinding
import com.lightfeather.wide_awakefinancials.databinding.ListItemTransactionBinding
import com.lightfeather.wide_awakefinancials.domain.model.ColoredFinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.DateListItem
import com.lightfeather.wide_awakefinancials.domain.model.TransactionType
import com.lightfeather.wide_awakefinancials.domain.model.TransactionsListModel
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_TYPE = 1
private const val TRANSACTION_TYPE = 2

class TransactionsListAdapter(val items: MutableList<TransactionsListModel>) :
    RecyclerView.Adapter<TransactionsListAdapter.ParentViewHolder>() {


    override fun getItemViewType(position: Int): Int {
        return if (items[position] is ColoredFinancialTransaction) TRANSACTION_TYPE
        else DATE_TYPE
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParentViewHolder {
        return if (viewType == TRANSACTION_TYPE) {
            val binding = ListItemTransactionBinding.inflate(LayoutInflater.from(parent.context))
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            binding.root.layoutParams = params
            TransactionViewHolder(binding)
        } else {
            val binding = ListItemDateBinding.inflate(LayoutInflater.from(parent.context))
            val params: LinearLayout.LayoutParams =
                LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            binding.root.layoutParams = params
            DateViewHolder(binding)
        }

    }


    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    abstract class ParentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(transactionsListModel: TransactionsListModel)
    }

    class DateViewHolder(val binding: ListItemDateBinding) : ParentViewHolder(binding.root) {
        override fun bind(transactionsListModel: TransactionsListModel) {
            val date = transactionsListModel as DateListItem
            with(binding) {
                this.date.text = date.date
            }
        }
    }

    class TransactionViewHolder(val binding: ListItemTransactionBinding) :
        ParentViewHolder(binding.root) {
        override fun bind(transactionsListModel: TransactionsListModel) {
            val financialTransaction = transactionsListModel as ColoredFinancialTransaction
            with(binding) {
                val transactionImage = if (financialTransaction.type == TransactionType.INCOME)
                    R.drawable.ic_baseline_arrow_upward_24 else
                    R.drawable.ic_baseline_arrow_downward_24
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = financialTransaction.creationTime
                transactionTime.setTime(
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    calendar.get(Calendar.SECOND)
                )
                transactionType.setImageResource(transactionImage)
                transactionCategory.text = financialTransaction.categoryName
                transactionAmount.text = "${financialTransaction.amount} $"
                transactionDescription.text = financialTransaction.description
                transactionContainer.setCardBackgroundColor(
                    android.graphics.Color.parseColor(
                        financialTransaction.categoryColor
                    )
                )
            }
        }
    }
}