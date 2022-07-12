package com.lightfeather.wide_awakefinancials.ui.home

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.utils.ColorTemplate
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.FragmentHomeBinding
import com.lightfeather.wide_awakefinancials.ui.util.showBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModel()
    private var fabExpanded = false
    private val openAnimation by lazy {
        AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.rotate_open
        )
    }
    private val closeAnimation by lazy {
        AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.rotate_close
        )
    }
    private val fromBottomAnimation by lazy {
        AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.from_bottom
        )
    }
    private val toBottomAnimation by lazy {
        AnimationUtils.loadAnimation(
            requireActivity(),
            R.anim.to_bottom
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val expensesChartDescription = Description()
            val incomesChartDescription = Description()

            expensesChartDescription.text = getString(R.string.expenses)
            expensesChartDescription.textSize = 14f
            incomesChartDescription.text = getString(R.string.income)
            incomesChartDescription.textSize = 14f

            expandFabs.setOnClickListener {
                setupVisibility(fabExpanded)
                startAnimation(fabExpanded)
                fabExpanded = !fabExpanded

            }
            addTransaction.setOnClickListener {
                setupVisibility(fabExpanded)
                startAnimation(fabExpanded)
                fabExpanded = !fabExpanded
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddTransactionDialog())

            }
            addCategory.setOnClickListener {
                setupVisibility(fabExpanded)
                startAnimation(fabExpanded)
                fabExpanded = !fabExpanded
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddCategoryDialog())
            }

            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getExpensesData().collect {
                    expensesChart.data = it
                    expensesChart.description = expensesChartDescription
                    expensesChart.setUsePercentValues(true)
                    expensesChart.invalidate()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getTotalExpenses().collect {
                    expensesChart.setCenterTextSize(20f)
                    expensesChart.setDrawCenterText(true)
                    expensesChart.centerText = "$it $"
                    expensesChart.setCenterTextColor(Color.BLACK)
                    expensesChart.invalidate()
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getIncomeData().collect {
                    incomeChart.data = it
                    incomeChart.description = incomesChartDescription
                    incomeChart.setUsePercentValues(true)
                    incomeChart.invalidate()
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getTotalIncome().collect {
                    incomeChart.setCenterTextSize(20f)
                    incomeChart.setDrawCenterText(true)
                    incomeChart.centerText = "$it $"
                    incomeChart.setCenterTextColor(Color.BLACK)
                    incomeChart.invalidate()
                }

            }



            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getAllTransactionsWithColors().map { TransactionsListAdapter(it) }
                    .collect(transactionsList::setAdapter)
            }
        }
    }

    private fun setupVisibility(fabExpanded: Boolean) {
        with(binding) {

            if (!fabExpanded) {
                addTransaction.visibility = View.VISIBLE
                addCategory.visibility = View.VISIBLE
            } else {
                addTransaction.visibility = View.INVISIBLE
                addCategory.visibility = View.INVISIBLE
            }
        }
    }

    private fun startAnimation(fabExpanded: Boolean) {
        with(binding) {
            if (fabExpanded) {
                expandFabs.startAnimation(closeAnimation)
                addCategory.startAnimation(toBottomAnimation)
                addTransaction.startAnimation(toBottomAnimation)
            } else {
                expandFabs.startAnimation(openAnimation)
                addCategory.startAnimation(fromBottomAnimation)
                addTransaction.startAnimation(fromBottomAnimation)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        requireActivity().showBottomNavigation()
    }
}