package com.lightfeather.wide_awakefinancials.ui.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lightfeather.wide_awakefinancials.databinding.FragmentInsightsBinding
import com.lightfeather.wide_awakefinancials.ui.util.showBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class InsightsFragment : Fragment() {

    private lateinit var binding: FragmentInsightsBinding
    private val viewModel by viewModel<InsightsViewModel>()


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
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().showBottomNavigation()
    }


}