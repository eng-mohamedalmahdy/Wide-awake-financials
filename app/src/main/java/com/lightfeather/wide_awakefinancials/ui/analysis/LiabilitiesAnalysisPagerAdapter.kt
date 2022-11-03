package com.lightfeather.wide_awakefinancials.ui.analysis

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.ListItemLiabilityBinding
import com.lightfeather.wide_awakefinancials.domain.persistence.AppPreferences
import com.lightfeather.wide_awakefinancials.ui.analysis.LiabilitiesAnalysisPagerAdapter.Constants.ITEMS_COUNT

class LiabilitiesAnalysisPagerAdapter(fa: FragmentActivity, private val dataList: List<Liability>) :
    FragmentStateAdapter(fa) {

    object Constants {
        const val ITEMS_COUNT = 3
    }


    override fun getItemCount(): Int = ITEMS_COUNT

    override fun createFragment(position: Int): Fragment =
        LiabilitiesAnalysisItemFragment(dataList[position])

    internal class LiabilitiesAnalysisItemFragment(private val liability: Liability) : Fragment() {

        private lateinit var binding: ListItemLiabilityBinding
        private val preferences by lazy { AppPreferences(requireContext()) }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = ListItemLiabilityBinding.inflate(inflater)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val chart: PieChart = binding.chart

            chart.legend.isWordWrapEnabled = true
            chart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            chart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
            chart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
            chart.data = liability.data
            chart.description = liability.description
            chart.invalidate()
            chart.setCenterTextSize(12f)
            chart.setDrawEntryLabels(false)
            chart.setDrawCenterText(true)
            chart.centerText = "${liability.centerText} ${preferences.getCurrency()}"
            chart.setCenterTextColor(Color.BLACK)
            chart.setTouchEnabled(false)
            chart.disableScroll()
            chart.isEnabled = false
        }
    }
}