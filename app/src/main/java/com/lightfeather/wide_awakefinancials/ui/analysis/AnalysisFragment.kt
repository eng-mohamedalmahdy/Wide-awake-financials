package com.lightfeather.wide_awakefinancials.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.FragmentAnalysisBinding
import com.lightfeather.wide_awakefinancials.domain.persistence.AppPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "AnalysisFragment"

class AnalysisFragment : Fragment() {

    private lateinit var binding: FragmentAnalysisBinding
    private val viewModel: AnalysisViewModel by viewModel()
    private val prefs by lazy { AppPreferences(requireActivity()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalysisBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {


            viewLifecycleOwner.lifecycleScope.launchWhenStarted {

                launch {
                    viewModel.getLiabilitiesData().collect { data ->
                        val expensesChartDescription = Description()
                        val incomesChartDescription = Description()
                        val totalChartDescription = Description()

                        expensesChartDescription.text = getString(R.string.expenses)
                        expensesChartDescription.textSize = 12f
                        incomesChartDescription.text = getString(R.string.income)
                        incomesChartDescription.textSize = 12f
                        totalChartDescription.text = getString(R.string.total)
                        totalChartDescription.textSize = 12f

                        launch {
                            viewModel.getLiabilitiesNumbers().collect { numbers ->
                                liabilitiesAnalysisPager.adapter =
                                    LiabilitiesAnalysisPagerAdapter(
                                        requireActivity(), listOf(
                                            Liability(
                                                data[0],
                                                incomesChartDescription,
                                                numbers[0].toString()
                                            ),
                                            Liability(
                                                data[1],
                                                expensesChartDescription,
                                                numbers[1].toString()
                                            ),
                                            Liability(
                                                data[2],
                                                totalChartDescription,
                                                numbers[2].toString()
                                            ),
                                        )
                                    )
                            }

                        }
                    }
                }
                launch {
                    viewModel.getDailyAnalysisData().collect {
                        val data = it.map {
                            val entries = it.second.values.mapIndexed { idx, values ->
                                BarEntry(idx.toFloat(), values.sumOf { it.amount }.toFloat())
                            }
                            BarData(BarDataSet(entries, "").apply { this.colors = it.first })
                        }
                        dailyAnalysisPager.adapter = BarChartAnalysisPagerAdapter(
                            requireActivity(), listOf(
                                BarChartAnalysis(
                                    getString(R.string.expenses),
                                    data[0],
                                    it[0].second.keys.toList()
                                ),
                                BarChartAnalysis(
                                    getString(R.string.income),
                                    data[1],
                                    it[1].second.keys.toList()
                                )
                            )
                        )

                    }
                }

                launch {
                    viewModel.getAverageSpending().map { it.toString() }
                        .collect(tvAvgSpending::setText)
                }
                launch {
                    viewModel.getHighestTransaction().map { it.toString() }
                        .collect(tvHighestTransaction::setText)
                }
                launch {
                    viewModel.getLowestTransaction().map { it.toString() }
                        .collect(tvLowestTransaction::setText)
                }
                launch {
                    viewModel.getHighestCategory()
                        .collect {
                            tvHighestCategory.text =
                                "  ${it.name} ${it.amount}${prefs.getCurrency()}"
                        }
                }
                launch {
                    viewModel.getLowestCategory()
                        .collect {
                            tvLowestCategory.text =
                                "  ${it.name} ${it.amount}${prefs.getCurrency()}"
                        }
                }
            }
        }

    }

    inline fun <reified T> instantCombine(vararg flows: Flow<T>) = channelFlow {
        val array = Array(flows.size) {
            false to (null as T?) // first element stands for "present"
        }

        flows.forEachIndexed { index, flow ->
            launch {
                flow.collect { emittedElement ->
                    array[index] = true to emittedElement
                    send(array.filter { it.first }.map { it.second })
                }
            }
        }
    }
}