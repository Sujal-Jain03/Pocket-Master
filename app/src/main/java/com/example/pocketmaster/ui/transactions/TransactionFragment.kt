package com.example.pocketmaster.ui.transactions

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketmaster.data.model.Transaction
import com.example.pocketmaster.data.model.TransactionFilter
import com.example.pocketmaster.databinding.FragmentTransactionBinding
import com.google.android.material.tabs.TabLayout
import com.example.pocketmaster.ui.viewmodel.TransactionViewModel
import kotlinx.coroutines.launch


class TransactionFragment : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!


    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?){
        super.onViewCreated(view,savedInstanceState)

        setupRecyclerView()
        setupTabLayout()
        observeTransactions()

    }
    private fun setupRecyclerView() {
        transactionAdapter = TransactionAdapter(
            onItemClick = { transaction ->  // ← Fixed: removed asterisks
                showTransactionDetails(transaction)
            },
                    onDeleteClick = { transaction ->  // ← Fixed: removed asterisks
                deleteTransaction(transaction)
            }
        )
        binding.recyclerview.apply {  // ← Fixed: removed asterisks
            layoutManager = LinearLayoutManager(requireContext())  // ← Fixed: removed asterisks
            adapter = transactionAdapter  // ← Fixed: removed asterisks
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)  // ← Fixed: removed asterisks
            )
        }
    }
    private fun setupTabLayout(){
        binding.transactionTabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        0->viewModel.setTransactionFilter(TransactionFilter.ALL)
                        1->viewModel.setTransactionFilter(TransactionFilter.INCOME)
                        2->viewModel.setTransactionFilter(TransactionFilter.EXPENSE)
                    }

                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {}

                override fun onTabReselected(p0: TabLayout.Tab?) {}

            }
        )
    }
    private fun observeTransactions(){
        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.transactions.collect{transactions->
                transactionAdapter.submitList(transactions)
            }
        }
    }

    private fun showTransactionDetails(transaction: Transaction){

    }
    private fun deleteTransaction(transaction: Transaction){}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

}

