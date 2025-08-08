package com.example.expensetracker_app.data.repository

import com.example.expensetracker_app.data.model.Expense
import com.example.expensetracker_app.data.model.ExpenseDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ExpenseRepository {
    suspend fun insertExpense(expense: Expense)
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    fun getExpensesByDateRange(start: Long, end: Long): Flow<List<Expense>>
    fun getTotalAmountByDateRange(start: Long, end: Long): Flow<Double?>
}

class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {
    override suspend fun insertExpense(expense: Expense) = dao.insertExpense(expense)
    override suspend fun updateExpense(expense: Expense) = dao.updateExpense(expense)
    override suspend fun deleteExpense(expense: Expense) = dao.deleteExpense(expense)
    override fun getExpensesByDateRange(start: Long, end: Long) = dao.getExpensesByDateRange(start, end)
    override fun getTotalAmountByDateRange(start: Long, end: Long) = dao.getTotalAmountByDateRange(start, end)
}