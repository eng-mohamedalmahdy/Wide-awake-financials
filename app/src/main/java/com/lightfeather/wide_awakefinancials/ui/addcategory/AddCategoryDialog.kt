package com.lightfeather.wide_awakefinancials.ui.addcategory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.larswerkman.holocolorpicker.ColorPicker
import com.lightfeather.wide_awakefinancials.R
import com.lightfeather.wide_awakefinancials.databinding.DialogAddCategoryBinding
import com.lightfeather.wide_awakefinancials.ui.startLoading
import com.lightfeather.wide_awakefinancials.ui.stopLoading
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddCategoryDialog : DialogFragment() {
    private lateinit var binding: DialogAddCategoryBinding
    private val viewModel by viewModel<AddCategoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogAddCategoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            colorPicker.onColorChangedListener = ColorPicker.OnColorChangedListener {
                chooseColor.setBackgroundColor(it)
            }

            addCategory.setOnClickListener {
                if (categoryName.text?.isEmpty() != false) {
                    categoryName.error = getString(R.string.cannot_be_empty)
                } else {
                    categoryName.error = null
                    requireActivity().startLoading()
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.addTransactionCategory(
                            categoryName.text.toString(),
                            colorPicker.color
                        )
                        withContext(Dispatchers.Main) {
                            requireActivity().stopLoading()
                            dismiss()
                        }
                    }
                }
            }

        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

}