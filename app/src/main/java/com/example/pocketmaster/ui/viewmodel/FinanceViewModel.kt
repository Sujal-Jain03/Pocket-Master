package com.example.pocketmaster.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmaster.data.database.AppDatabase
import com.example.pocketmaster.data.model.Category
import com.example.pocketmaster.data.model.Transaction
import com.example.pocketmaster.data.model.TransactionType
import com.example.pocketmaster.data.repository.FinanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FinanceViewModel(application: Application): AndroidViewModel(application) {
    private var repository:FinanceRepository
    lateinit var allTransaction:Flow<List<Transaction>>
    lateinit var allCategories: Flow<List<Category>>

    init{
        val database = AppDatabase.getDatabase(application)
        repository= FinanceRepository(database.transactionDao(),database.categoryDao())
        allTransaction=repository.allTransactions
        allCategories=repository.allCategory
    }

    fun getTransactionByType(type: TransactionType)=repository.getTransactionByType(type)

    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return repository.getCategoriesByType(type)
    }

    fun getCategoryTotal(type: TransactionType,startDate:Long=0)=repository.getCategoryTotal(type,startDate)

    suspend fun getTotalByType(type:TransactionType)=repository.getTotalsByType(type)

    fun addTransaction(transaction: Transaction)=viewModelScope.launch{
        repository.addTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transaction)=viewModelScope.launch{
        repository.deleteTransaction(transaction)
    }

    fun addCategory(category: Category)=viewModelScope.launch{
        repository.addCategory(category)

    }






}