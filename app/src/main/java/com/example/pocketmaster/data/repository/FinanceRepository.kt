package com.example.pocketmaster.data.repository

import com.example.pocketmaster.data.dao.CategoryDao
import com.example.pocketmaster.data.dao.TransactionDao
import com.example.pocketmaster.data.model.Category
import com.example.pocketmaster.data.model.Transaction
import com.example.pocketmaster.data.model.TransactionType
import kotlinx.coroutines.flow.Flow

class FinanceRepository(private val transactionDao: TransactionDao,
                        private val categoryDao: CategoryDao)
{

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions()
    val allCategory=categoryDao.getAllCategories()


    fun getTransactionByType(type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(type)
    }
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>{
        return categoryDao.getCategoriesByType(type)
    }
    fun getCategoryTotal(type: TransactionType,startDate:Long=0)=categoryDao.getCategoryTotals(type,startDate)

    suspend fun getTotalsByType(type: TransactionType) = transactionDao.getTotalByType(type)?: 0.0

    suspend fun addTransaction(transaction: Transaction){
        transactionDao.insert(transaction)
    }

    suspend fun deleteTransaction(transaction: Transaction){
        transactionDao.delete(transaction)
    }
    suspend fun addCategory(category: Category){
        categoryDao.insert(category)
    }


}