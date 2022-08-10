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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.snackbar.Snackbar
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.FragmentHomeBinding
import com.lightfeather.wide_awakefinancials.domain.model.ColoredFinancialTransaction
import com.lightfeather.wide_awakefinancials.domain.model.DateListItem
import com.lightfeather.wide_awakefinancials.domain.model.TransactionsListModel
import com.lightfeather.wide_awakefinancials.domain.persistence.AppPreferences
import com.lightfeather.wide_awakefinancials.ui.util.SwipeGesture
import com.lightfeather.wide_awakefinancials.ui.util.showBottomNavigation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


private const val TAG = "HomeFragment"

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val preferences by lazy { AppPreferences(requireContext()) }
    private val viewModel: HomeViewModel by viewModel()
    private var fabExpanded = false

    private val fromBottomAnimation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.from_bottom)
    }
    private val toBottomAnimation by lazy {
        AnimationUtils.loadAnimation(requireActivity(), R.anim.to_bottom)
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
            val totalChartDescription = Description()

            expensesChartDescription.text = getString(R.string.expenses)
            expensesChartDescription.textSize = 12f
            incomesChartDescription.text = getString(R.string.income)
            incomesChartDescription.textSize = 12f
            totalChartDescription.text = getString(R.string.total)
            totalChartDescription.textSize = 12f


            totalChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
            incomeChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
            expensesChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL

            totalChart.legend.isWordWrapEnabled = true
            incomeChart.legend.isWordWrapEnabled = true
            expensesChart.legend.isWordWrapEnabled = true


            totalChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            totalChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT


            incomeChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            incomeChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT


            expensesChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            expensesChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT



            expandFabs.setOnClickListener {
                setupVisibility(fabExpanded)
                startAnimation(fabExpanded)
                fabExpanded = !fabExpanded

            }
            addTransaction.setOnClickListener {
                setupVisibility(fabExpanded)
                startAnimation(fabExpanded)
                fabExpanded = !fabExpanded
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAddTransactionDialog(null)
                )

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
                    expensesChart.setCenterTextSize(12f)
                    expensesChart.setDrawCenterText(true)
                    expensesChart.centerText = "$it ${preferences.getCurrency()}"
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



            CoroutineScope(Dispatchers.Main).launch {
                viewModel.getAllTransactionsWithColors()
                    .map {
                        val res = mutableListOf<TransactionsListModel>()
                        it.toMutableList()
                            .reversed()
                            .groupBy {
                                val date = Date(it.creationTime)
                                val format =
                                    SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                                format.format(date)
                            }.forEach { (k, v) ->
                                res += DateListItem(k)
                                v.forEach {
                                    res += it
                                }
                            }
                        res.distinct().toMutableList()
                    }
                    .map(::TransactionsListAdapter)
                    .collect { adapter ->
                        itemsPlaceHolder.visibility =
                            if (adapter.items.isEmpty()) View.VISIBLE else View.GONE
                        transactionsList.adapter = adapter
                        val swipeGesture = object : SwipeGesture(requireContext()) {
                            override fun onSwiped(
                                viewHolder: RecyclerView.ViewHolder,
                                direction: Int
                            ) {
                                try {
                                    val item =
                                        adapter.items[viewHolder.bindingAdapterPosition] as ColoredFinancialTransaction
                                    when (direction) {
                                        ItemTouchHelper.LEFT -> {
                                            CoroutineScope(Dispatchers.Main).launch {

                                                viewModel.deleteItem(item)
                                                adapter.notifyItemChanged(viewHolder.adapterPosition);
                                                Snackbar.make(
                                                    binding.root,
                                                    getString(R.string.deleted_successfully),
                                                    Snackbar.LENGTH_LONG
                                                ).apply {
                                                    setAction(getString(R.string.undo)) {
                                                        CoroutineScope(Dispatchers.IO).launch {
                                                            viewModel.insertTransaction(item)
                                                        }
                                                    }
                                                    show()
                                                }
                                            }
                                        }
                                        ItemTouchHelper.RIGHT -> {
                                            findNavController().navigate(
                                                HomeFragmentDirections.actionHomeFragmentToAddTransactionDialog(
                                                    item
                                                )
                                            )
                                            adapter.notifyDataSetChanged()
                                            transactionsList.recycledViewPool.clear()
                                        }
                                    }
                                    adapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)

                                } catch (ex: IndexOutOfBoundsException) {
                                    adapter.notifyItemChanged(viewHolder.absoluteAdapterPosition)
                                    transactionsList.recycledViewPool.clear()
                                }

                            }
                        }
                        val touchHelper = ItemTouchHelper(swipeGesture)
                        touchHelper.attachToRecyclerView(transactionsList)
                    }


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
                addCategory.startAnimation(toBottomAnimation)
                addTransaction.startAnimation(toBottomAnimation)
            } else {
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