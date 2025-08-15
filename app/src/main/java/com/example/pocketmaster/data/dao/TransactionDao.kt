package com.example.pocketmaster.data.dao

import androidx.room.Delete
import androidx.room.Insert
import com.example.pocketmaster.data.model.CategoryTotal
import com.example.pocketmaster.data.model.Transaction
import com.example.pocketmaster.data.model.TransactionType
import kotlinx.coroutines.flow.Flow
import androidx.room.Query
import androidx.room.Dao
import com.example.pocketmaster.data.model.Category

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>

    @Insert
    suspend fun insert(transaction: Transaction)

    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT SUM(amount) FROM transactions WHERE type = :type")
    suspend fun getTotalByType(type: TransactionType): Double?

    @Query("""
        SELECT category, COALESCE(SUM(amount), 0) as total
        FROM transactions
        WHERE type = :type AND date >= :startDate
        GROUP BY category
    """)
    fun getCategoriesTotals(
        type: TransactionType,
        startDate: Long = 0
    ): Flow<List<CategoryTotal>>


}
