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
    private var selectedCategory: TransactionCategory? = null
    private val transaction by lazy { AddTransactionDialogArgs.fromBundle(requireArguments()).transaction }
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
                                requireContext(),
                                R.layout.list_item_transaction_category,
                                category.map { "${it.id}- ${it.name}" }
                            )
                        )
                    }
                }
                withContext(Dispatchers.Main) {
                    fillData()
                }

                binding.transactionCategories.setOnItemClickListener { _, _, i, l ->
                    selectedCategory = category[i]
                    Log.d(TAG, "onViewCreated: $selectedCategory")

                }
            }
        }
        with(binding) {
            addTransaction.setOnClickListener {
                addNewOrUpdateTransaction(transaction != null)
            }
        }
    }

    private fun onInputValid(onInputValidAction: () -> Unit) {
        with(binding) {
            if (transactionDescription.text?.isEmpty() == true) {
                transactionDescription.error = getString(R.string.cannot_be_empty)
            } else if (transactionAmount.text?.isEmpty() == true) {
                transactionAmount.error = getString(R.string.cannot_be_empty)
            } else if (transactionCategories.text?.isEmpty() == true) {
                transactionCategories.error = getString(R.string.please_select)
            } else {
                onInputValidAction()
            }
        }
    }


    private fun addNewOrUpdateTransaction(isUpdating: Boolean) {
        with(binding) {
            onInputValid {
                requireActivity().startLoading()
                val selection =
                    if (toggleButton.checkedButtonId == R.id.expense) TransactionType.EXPENSE else TransactionType.INCOME
                clearErrors()
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d(TAG, "addNewOrUpdateTransaction: $transaction")

                    viewModel.addOrUpdateTransaction(
                        isUpdating = isUpdating,
                        transactionDescription.text.toString(),
                        selectedCategory?.id ?: -1,
                        transaction?.creationTime,
                        transactionAmount.text.toString().toDouble(),
                        selection,
                        transaction?.id
                    )
                    withContext(Dispatchers.Main) {
                        dismiss()
                        requireActivity().stopLoading()
                    }
                }
            }
        }
    }

    private fun clearErrors() {
        with(binding) {
            transactionDescription.error = null
            transactionAmount.error = null
            transactionCategories.error = null
        }
    }

    private fun fillData() {
        transaction?.let { transaction ->
            with(binding) {
                transactionDescription.setText(transaction.description)
                transactionAmount.setText(transaction.amount.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getTransactionCategories().collect { categories ->
                        val item = categories.first { transaction.categoryId == it.id }
                        transactionCategories.setText("${item.id}- ${item.name}", false)
                        selectedCategory = item
                    }
                }
                if (transaction.type == TransactionType.EXPENSE) {
                    toggleButton.check(R.id.expense)
                } else toggleButton.check(R.id.income)
                addTransaction.setText(R.string.update_transaction)
            }

        }
    }
}

