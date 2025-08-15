package com.example.pocketmaster.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.core.graphics.toColorInt
import com.example.pocketmaster.data.dao.CategoryDao
import com.example.pocketmaster.data.dao.TransactionDao
import com.example.pocketmaster.data.model.Transaction
import com.example.pocketmaster.data.model.Category
import com.example.pocketmaster.data.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Transaction::class, Category::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(TransactionConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {  // ← Fixed: removed asterisks
                val instance = Room.databaseBuilder(
                    context.applicationContext,  // ← Fixed: removed asterisks
                    AppDatabase::class.java,     // ← Fixed: removed asterisks
                    "finance_database"           // ← Fixed: removed backslash
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {  // ← Fixed: removed asterisks
                                INSTANCE?.categoryDao()?.apply {     // ← Fixed: removed asterisks
                                    insert(Category("Food & Dining", type = TransactionType.EXPENSE, color = "#FF5252".toColorInt()))  // ← Fixed: removed asterisks
                                    insert(Category("Transportation", type = TransactionType.EXPENSE, color = "#448AFF".toColorInt()))
                                    insert(Category("Shopping", type = TransactionType.EXPENSE, color = "#4CAF50".toColorInt()))
                                    insert(Category("Bills & Utilities", type = TransactionType.EXPENSE, color = "#FFC107".toColorInt()))
                                    insert(Category("Healthcare", type = TransactionType.EXPENSE, color = "#E040FB".toColorInt()))

                                    insert(Category("Salary", type = TransactionType.INCOME, color = "#00BCD4".toColorInt()))
                                    insert(Category("Freelance", type = TransactionType.INCOME, color = "#9C27B0".toColorInt()))
                                    insert(Category("Investment", type = TransactionType.INCOME, color = "#4CAF50".toColorInt()))
                                }
                            }
                        }
                    }).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
