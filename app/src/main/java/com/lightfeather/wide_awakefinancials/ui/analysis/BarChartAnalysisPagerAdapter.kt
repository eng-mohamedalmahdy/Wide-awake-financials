package com.lightfeather.wide_awakefinancials.ui.analysis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.ValueFormatter
import com.lightfeather.wide_awakefinancials.databinding.ListItemDailyAnalysisBinding

class BarChartAnalysisPagerAdapter(
    fa: FragmentActivity,
    private val dataList: List<BarChartAnalysis>,
    private val pagesCount: Int = 2
) :
    FragmentStateAdapter(fa) {
    private val ITEM_COUNT = pagesCount


    override fun getItemCount(): Int = ITEM_COUNT

    override fun createFragment(position: Int): Fragment = DailyAnalysisFragment(dataList[position])

    internal class DailyAnalysisFragment(private val analysis: BarChartAnalysis) : Fragment() {

        private lateinit var binding: ListItemDailyAnalysisBinding
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = ListItemDailyAnalysisBinding.inflate(inflater)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            with(binding) {
                title.text = analysis.title
                with(incomeSummery.xAxis) {
                    val monthsFormatter = object : ValueFormatter() {
                        override fun getFormattedValue(value: Float) =
                            analysis.days[value.toInt()]
                    }
                    labelCount = analysis.days.size
                    valueFormatter = monthsFormatter
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f
                }
                incomeSummery.data = analysis.data
                incomeSummery.description.isEnabled = false
                incomeSummery.invalidate()

            }
        }

    }
}