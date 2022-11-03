package com.lightfeather.wide_awakefinancials.ui.analysis

import com.github.mikephil.charting.data.BarData

data class BarChartAnalysis(val title: String, val data: BarData, val days: List<String>)
