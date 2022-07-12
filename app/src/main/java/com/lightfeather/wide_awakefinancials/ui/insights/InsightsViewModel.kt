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
    fun getIncomeData(): Flow<BarData> {
        return repository.getIncomeData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries = transactions.groupBy {
                SimpleDateFormat("MMM").format(Date(it.creationTime))
            }.values.mapIndexed { idx, values ->
                BarEntry(
                    idx.toFloat(),
                    values.sumOf { it.amount }.toFloat()
                )
            }
            BarData(BarDataSet(entries, "").apply {
                this.colors = colors
            })
        }
    }

    fun getExpensesData(): Flow<BarData> {
        return repository.getExpensesData().map { transactions ->

            val colors = transactions.map { Color.parseColor(it.categoryColor) }
            val entries = transactions.groupBy {
                SimpleDateFormat("MMM").format(Date(it.creationTime))
            }.values.mapIndexed { idx, values ->
                BarEntry(
                    idx.toFloat(),
                    values.sumOf { it.amount }.toFloat()
                )
            }
            BarData(BarDataSet(entries, "").apply {
                this.colors = colors
            })
        }
    }

}