package com.lightfeather.wide_awakefinancials.ui.insights

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
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
    private val months =
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    private val monthsFormatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float) = months[value.toInt()]
    }

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
                    with(incomeSummery.xAxis) {
                        labelCount = 12
                        valueFormatter = monthsFormatter
                        position = XAxisPosition.BOTTOM
                        granularity = 1f
                    }
                    incomeSummery.data = it
                    incomeSummery.description.isEnabled = false
                    incomeSummery.invalidate()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getExpensesData().collect {
                    with(expensesSummery.xAxis) {
                        labelCount = 12
                        valueFormatter = monthsFormatter
                        position = XAxisPosition.BOTTOM
                        granularity = 1f
                    }
                    expensesSummery.data = it
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