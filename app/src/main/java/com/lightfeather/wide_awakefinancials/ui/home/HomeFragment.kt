package com.lightfeather.wide_awakefinancials.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.components.Description
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.FragmentHomeBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


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
            incomesChartDescription.text = getString(R.string.income)
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

            expensesChart.data = viewModel.getExpensesData()
            expensesChart.description = expensesChartDescription
            incomeChart.data = viewModel.getIncomeData()
            transactionsList.adapter = TransactionsListAdapter(viewModel.getAllTransactions())
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


}