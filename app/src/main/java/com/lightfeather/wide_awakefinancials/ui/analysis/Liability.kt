package com.lightfeather.wide_awakefinancials.ui.analysis

import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData

data class Liability(
    val data: PieData,
    val description: Description,
    val centerText: String
)
