package com.example.pocketmaster.data.model

import com.example.pocketmaster.data.model.ExpenseCategoryData
data class DashboardState(
    val balance: Double =0.0,
    val totalIncome: Double =0.0,
    val totalExpense: Double = 0.0,
    val expenseCategories: List<ExpenseCategoryData> = emptyList()

)