package com.example.pocketmaster.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmaster.data.database.AppDatabase
import com.example.pocketmaster.data.model.Transaction
import com.example.pocketmaster.data.model.TransactionFilter
import com.example.pocketmaster.data.model.TransactionType
import com.example.pocketmaster.data.repository.FinanceRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application): AndroidViewModel(application)
{

    private val repository: FinanceRepository = FinanceRepository(
        AppDatabase.Companion.getDatabase(application).transactionDao(),
        AppDatabase.Companion.getDatabase(application).categoryDao()
    )

    private val _transactionFilter = MutableStateFlow(TransactionFilter.ALL)  // ← Fixed: underscore, removed asterisks

    @OptIn(ExperimentalCoroutinesApi::class)
    val transactions = _transactionFilter
        .flatMapLatest { filter ->
            Log.d("TransactionViewModel", "Filter changed to: $filter")
            when (filter) {
                TransactionFilter.ALL ->{
                    Log.d("TransactionViewModel", "Getting all transactions")
                    repository.allTransactions  
                }
                TransactionFilter.INCOME -> {
                    Log.d("TransactionViewModel", "Getting income transactions")
                    repository.getTransactionByType(TransactionType.INCOME) }
                TransactionFilter.EXPENSE -> {
                    Log.d("TransactionViewModel", "Getting expense transactions")
                    repository.getTransactionByType(TransactionType.EXPENSE)  }
            }
        }
        .stateIn(  
            viewModelScope,  
            SharingStarted.Companion.WhileSubscribed(5000),
            emptyList()  
        )

    fun setTransactionFilter(filter: TransactionFilter) {
        _transactionFilter.value = filter  // ← Fixed: underscore
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {  
            repository.deleteTransaction(transaction)
        }
    }

    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            Log.d("TransactionViewModel", "Adding transaction: ${transaction.description}")
            repository.addTransaction(transaction)
        }
    }
}