package com.example.pocketmaster.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.pocketmaster.data.database.AppDatabase
import com.example.pocketmaster.data.model.CategoryTotal
import com.example.pocketmaster.data.model.DashboardState
import com.example.pocketmaster.data.model.ExpenseCategoryData
import com.example.pocketmaster.data.model.TransactionType
import com.example.pocketmaster.data.repository.FinanceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

import kotlin.collections.map

class DashboardViewModel(application: Application): AndroidViewModel(application){
    private val repository: FinanceRepository= FinanceRepository(
        AppDatabase.getDatabase(application).transactionDao(),
        AppDatabase.getDatabase(application).categoryDao()
    )

    private val incomeTotalFlow = repository.allTransactions
        .map{ transactions ->
            transactions
                .filter{it.type==TransactionType.INCOME}
                .sumOf{it.amount}

        }
    private val expenseTotalFlow = repository.allTransactions
        .map{transactions ->
            transactions
                .filter{it.type==TransactionType.EXPENSE}
                .sumOf({it.amount})
        }

    val dashboardState: StateFlow<DashboardState> = combine(
            incomeTotalFlow,
        expenseTotalFlow,
        repository.getCategoryTotal(TransactionType.EXPENSE)
    ){income: Double,expense:Double,categoryTotals: List<CategoryTotal> ->
        DashboardState(
            totalIncome = income,
            totalExpense = expense,
            balance = income - expense,
            expenseCategories = categoryTotals.map{ categoryTotal ->
                ExpenseCategoryData(
                    category = categoryTotal.category,
                    amount = categoryTotal.total
                )
            }
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardState()

    )

}




