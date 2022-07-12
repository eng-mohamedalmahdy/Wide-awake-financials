package com.lightfeather.wide_awakefinancials.ui.addtransaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.DialogAddTransactionBinding
import com.lightfeather.wide_awakefinancials.domain.model.TransactionCategory
import com.lightfeather.wide_awakefinancials.domain.model.TransactionType
import com.lightfeather.wide_awakefinancials.ui.util.startLoading
import com.lightfeather.wide_awakefinancials.ui.util.stopLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "AddTransactionDialog"

class AddTransactionDialog : DialogFragment() {
    private val viewModel: AddTransactionViewModel by viewModel()
    private lateinit var binding: DialogAddTransactionBinding
    private var firstTimeSpinner = true
    private var selectedCategory: TransactionCategory? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddTransactionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch {
            viewModel.getTransactionCategories().collect { category ->

                withContext(Dispatchers.Main) {
                    if (context != null) {
                        binding.transactionCategories.setAdapter(
                            ArrayAdapter(
                                context!!,
                                R.layout.list_item_transaction_category,
                                category.map { "${it.id}- ${it.name}" }
                            )
                        )
                    }
                }

                binding.transactionCategories.setOnItemClickListener { adapterView, view, i, l ->
                    selectedCategory = category[i]
                    Log.d(TAG, "onViewCreated: $selectedCategory")

                }
            }
        }
        with(binding) {
            addCategory.setOnClickListener {
                if (transactionDescription.text?.isEmpty() == true) {
                    transactionDescription.error = getString(R.string.cannot_be_empty)
                } else if (transactionAmount.text?.isEmpty() == true) {
                    transactionAmount.error = getString(R.string.cannot_be_empty)
                } else if (transactionCategories.text?.isEmpty() == true) {
                    transactionCategories.error = getString(R.string.please_select)
                } else {
                    requireActivity().startLoading()
                    val selection =
                        if (toggleButton.checkedButtonId == R.id.expense) TransactionType.EXPENSE else TransactionType.INCOME
                    transactionDescription.error = null
                    transactionAmount.error = null
                    transactionCategories.error = null
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.addTransaction(
                            transactionDescription.text.toString(),
                            selectedCategory?.id ?: -1,
                            transactionAmount.text.toString().toDouble(),
                            selection
                        )
                        withContext(Dispatchers.Main) {
                            dismiss()
                            requireActivity().stopLoading()

                        }
                    }
                }
            }
        }
    }
}

