package com.example.pocketmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.pocketmaster.data.database.TransactionConverter

@Entity(tableName = "transactions")

data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val amount: Double,
    val description: String,
    val category: String,
    @TypeConverters(TransactionConverter::class)
    val type: TransactionType,
    val date: Long=System.currentTimeMillis()
)
