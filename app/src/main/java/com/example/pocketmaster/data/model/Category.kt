package com.example.pocketmaster.data.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categories")
data class Category(
    @PrimaryKey val name:String,
    val type: TransactionType,
    val color: Int
)
