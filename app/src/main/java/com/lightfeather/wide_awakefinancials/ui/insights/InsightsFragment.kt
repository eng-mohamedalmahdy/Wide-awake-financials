package com.lightfeather.wide_awakefinancials.ui.insights

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.FragmentInsightsBinding
import com.lightfeather.wide_awakefinancials.domain.persistence.AppPreferences
import com.lightfeather.wide_awakefinancials.ui.util.showBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding
    private val viewModel by viewModel<InsightsViewModel>()
    private val preferences by lazy { AppPreferences(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInsightsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {

            val expensesChartDescription = Description()
            val incomesChartDescription = Description()
            val totalChartDescription = Description()

            expensesChartDescription.text = getString(R.string.expenses)
            expensesChartDescription.textSize = 12f
            incomesChartDescription.text = getString(R.string.income)
            incomesChartDescription.textSize = 12f
            totalChartDescription.text = getString(R.string.total)
            totalChartDescription.textSize = 12f
            totalChart.legend.isWordWrapEnabled = true

            totalChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            totalChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            totalChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
            incomeChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
            expensesChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL

            incomeChart.legend.isWordWrapEnabled = true
            expensesChart.legend.isWordWrapEnabled = true




            incomeChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            incomeChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT


            expensesChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            expensesChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

            insights.text = viewModel.getInsights()
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getIncomeData().collect {
                    val entries = it.second.values.mapIndexed { idx, values ->
                        BarEntry(
                            idx.toFloat(),
                            values.sumOf { it.amount }.toFloat()
                        )
                    }
                    val data = BarData(BarDataSet(entries, "").apply { this.colors = it.first })
                    with(incomeSummery.xAxis) {
                        val monthsFormatter = object : ValueFormatter() {
                            override fun getFormattedValue(value: Float) =
                                it.second.keys.toList()[value.toInt()]
                        }
                        labelCount = it.second.keys.size
                        valueFormatter = monthsFormatter
                        position = XAxisPosition.BOTTOM
                        granularity = 1f
                    }
                    incomeSummery.data = data
                    incomeSummery.description.isEnabled = false
                    incomeSummery.invalidate()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getExpensesData().collect {
                    val entries = it.second.values.mapIndexed { idx, values ->
                        BarEntry(idx.toFloat(), values.sumOf { it.amount }.toFloat())
                    }
                    val data = BarData(BarDataSet(entries, "").apply { this.colors = it.first })

                    val monthsFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float) =
                            it.second.keys.toList()[value.toInt()]
                    }
                    with(expensesSummery.xAxis) {


                        labelCount = it.second.keys.size
                        valueFormatter = monthsFormatter
                        position = XAxisPosition.BOTTOM
                        granularity = 1f
                    }
                    expensesSummery.data = data
                    expensesSummery.description.isEnabled = false

                    expensesSummery.invalidate()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getExpensesDataColored().collect { data ->
                    expensesChart.data = data
                    expensesChart.description = expensesChartDescription
                    expensesChart.setUsePercentValues(true)
                    expensesChart.invalidate()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getTotalExpenses().collect {
                    expensesChart.setCenterTextSize(12f)
                    expensesChart.setDrawCenterText(true)
                    expensesChart.centerText = "$it ${preferences.getCurrency()}"
                    expensesChart.setCenterTextColor(Color.BLACK)
                    expensesChart.invalidate()
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getIncomeDataColored().collect { data ->
                    incomeChart.data = data
                    incomeChart.description = incomesChartDescription
                    incomeChart.setUsePercentValues(true)
                    incomeChart.invalidate()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getTotalIncome().collect {
                    incomeChart.setCenterTextSize(12f)
                    incomeChart.setDrawCenterText(true)
                    incomeChart.centerText = "$it ${preferences.getCurrency()}"
                    incomeChart.setCenterTextColor(Color.BLACK)
                    incomeChart.invalidate()
                }

            }

            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getAllData().collect {
                    totalChart.data = it
                    totalChart.description = totalChartDescription
                    totalChart.setUsePercentValues(true)
                    totalChart.invalidate()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getTotalExpensesAndIncome().collect {
                    totalChart.setCenterTextSize(12f)
                    totalChart.setDrawCenterText(true)
                    totalChart.centerText = "$it ${preferences.getCurrency()}"
                    totalChart.setCenterTextColor(Color.BLACK)
                    totalChart.invalidate()
                }

            }

        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().showBottomNavigation()
    }


}