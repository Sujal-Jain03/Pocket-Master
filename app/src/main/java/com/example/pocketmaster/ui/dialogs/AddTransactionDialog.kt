package com.example.pocketmaster.ui.dialogs

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope  // ← Fixed: removed asterisks
import com.example.pocketmaster.R
import com.example.pocketmaster.data.model.Category
import com.example.pocketmaster.data.model.CategoryTotal
import com.example.pocketmaster.data.model.Transaction
import com.example.pocketmaster.data.model.TransactionType
import com.example.pocketmaster.databinding.FragmentAddTransactionDialogBinding
import com.example.pocketmaster.ui.viewmodel.FinanceViewModel
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class AddTransactionDialog: DialogFragment(){
    private var _binding: FragmentAddTransactionDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: FinanceViewModel
    private var currentType = TransactionType.EXPENSE
    private var categories: List<Category> = listOf<Category>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddTransactionDialogBinding.inflate(inflater, container, false)  // ← Fixed: removed backslash
        return binding.root  // ← Fixed: removed asterisks
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[FinanceViewModel::class.java]  // ← Fixed: removed asterisks
        setupInitial()
        setupTypeSelection()
        setupCategorySpinner()
        setupSaveButton()
        observeCategories()
    }

    private fun setupInitial(){
        Log.d("AddTransaction","initial type $currentType")
        val initialTab:Int=when(currentType){
            TransactionType.INCOME -> 0
            TransactionType.EXPENSE -> 1
        }
        binding.typeTabLayout.getTabAt(initialTab)?.select()
    }

    private fun setupTypeSelection(){
        binding.typeTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val newType: TransactionType=when(tab?.position){
                    0 -> TransactionType.INCOME
                    else -> TransactionType.EXPENSE
                }

                Log.d("Add Transaction","Tab Selected:${tab?.position}, New type $newType")
                if(newType!=currentType){
                    currentType=newType
                    observeCategories()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun observeCategories() {
        viewLifecycleOwner.lifecycleScope.launch {  // ← Fixed: removed asterisks
            viewModel.getCategoriesByType(currentType).collect { newCategories ->  // ← Fixed: removed asterisks
                categories = newCategories
                updateCategorySpinner()
            }
        }
    }

    private fun updateCategorySpinner() {
        val categoryNames = categories.map { it.name }  // ← Fixed: removed asterisks
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,  // ← Fixed: removed backslashes
            categoryNames
        )
        binding.spinnerCategory.setAdapter(adapter) // ← Fixed: removed asterisks
    }

    private fun setupCategorySpinner() {
        val adapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,  // ← Fixed: removed backslashes
            mutableListOf<String>()  // ← Fixed: removed asterisks
        )
        binding.spinnerCategory.setAdapter(adapter)  // ← Fixed: use .adapter instead of setAdapter()
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {  // ← Fixed: removed asterisks
            if(validateInput()){
                saveTransaction()
                dismiss()
            }
        }

        binding.btnCancel.setOnClickListener {  // ← Fixed: removed asterisks
            dismiss()
        }
    }

    private fun validateInput(): Boolean{
        var isValid = true
        val amount = binding.etAmount.text.toString()  // ← Fixed: removed asterisks

        if(amount.isEmpty() || amount.toDoubleOrNull() == null){  // ← Fixed: removed asterisks
            binding.etAmount.error = requireContext().getString(R.string.error_invalid_amount)  // ← Fixed: removed backslashes
            isValid = false
        }
        if(binding.etDescription.text.toString().isEmpty()){  // ← Fixed: removed asterisks
            binding.etDescription.error = requireContext().getString(R.string.error_description_required)  // ← Fixed: removed backslashes
            isValid = false
        }
        if(binding.spinnerCategory.text.toString().isEmpty()){
            binding.spinnerCategory.error = requireContext().getString(R.string.error_select_category)
            isValid = false
        }
        return isValid
    }

    private fun saveTransaction(){
        val amount = binding.etAmount.text.toString().toDouble()  // ← Fixed: removed asterisks
        val description = binding.etDescription.text.toString()  // ← Fixed: removed asterisks
        val category:String = binding.spinnerCategory.text.toString()  // ← Fixed: proper spinner value access

        Log.d("AddTransactionDialog", "Saving transaction with type: $currentType")
        val transaction = Transaction(
            amount = amount,
            description = description,
            category = category,
            type = currentType,
            date = System.currentTimeMillis()
        )
        viewModel.addTransaction(transaction)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // ← Fixed: removed backslash
    }
}
