package com.example.pocketmaster.ui.transactions

import android.icu.number.NumberFormatter
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pocketmaster.data.model.Transaction
import com.example.pocketmaster.data.model.TransactionType
import com.example.pocketmaster.databinding.ItemTransactionBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(
    private val onItemClick: (Transaction) -> Unit,
    private val onDeleteClick: (Transaction) -> Unit
): ListAdapter<Transaction, TransactionAdapter.TransactionViewHolder>(TransactionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding : ItemTransactionBinding =ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class TransactionViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

        // Add these if they're missing:
        private val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        private val currencyFormatter = NumberFormat.getCurrencyInstance()

        fun bind(transaction: Transaction) {
            binding.apply {
                tvDescription.text = transaction.description
                tvCategory.text = transaction.category
                tvDate.text = dateFormatter.format(Date(transaction.date))  // Note: Date() wrapper for Long

                val amount: String = currencyFormatter.format(transaction.amount)
                tvAmount.text = when(transaction.type) {
                    TransactionType.INCOME -> "+$amount"
                    TransactionType.EXPENSE -> "-$amount"
                }

                tvAmount.setTextColor(
                    tvAmount.context.getColor(
                        when(transaction.type) {
                            TransactionType.INCOME -> android.R.color.holo_green_dark
                            TransactionType.EXPENSE -> android.R.color.holo_red_dark
                        }
                    )
                )
            }
        }
    }

    private class TransactionDiffCallback: DiffUtil.ItemCallback<Transaction>(){
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }

    }



}