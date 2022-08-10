package com.lightfeather.wide_awakefinancials.ui.insights

import android.graphics.Color
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.*
import com.lightfeather.wide_awakefinancials.domain.repositories.ExpensesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.*

class InsightsViewModel(private val repository: ExpensesRepository) : ViewModel() {

    fun getInsights(): String = repository.getInsights()
    fun getIncomeData() = repository.getIncomeData().map { transactions ->
        val colors = transactions.map { Color.parseColor(it.categoryColor) }
        val entries =
            transactions.groupBy { SimpleDateFormat("dd/MMM/YY").format(Date(it.creationTime)) }
        Pair(colors, entries)
    }


    fun getExpensesData() =
        repository.getExpensesData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries = transactions.groupBy {
                SimpleDateFormat("dd/MMM/YY").format(Date(it.creationTime))
            }
            Pair(colors, entries)
        }


}