package com.example.expensetracker_app.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker_app.data.model.Expense
import com.example.expensetracker_app.data.repository.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class DailyTotal(val dayLabel: String, val total: Double)

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val repository: ExpenseRepository
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(System.currentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate

    val expenses: StateFlow<List<Expense>> = _selectedDate.flatMapLatest { date ->
        repository.getExpensesByDateRange(dayStart(date), dayEnd(date))
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val totalSpent: StateFlow<Double> = _selectedDate.flatMapLatest { date ->
        repository.getTotalAmountByDateRange(dayStart(date), dayEnd(date))
            .map { it ?: 0.0 }
    }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    // Weekly summary - last 7 days totals
    val weeklySummary: StateFlow<List<DailyTotal>> = flow {
        // compute start and end for 7 days and fetch combined
        val today = Calendar.getInstance()
        val totals = mutableListOf<DailyTotal>()
        for (i in 6 downTo 0) {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val start = dayStart(cal.timeInMillis)
            val end = dayEnd(cal.timeInMillis)
            val sum = repository.getTotalAmountByDateRange(start, end).first() ?: 0.0
            val label = "${cal.get(Calendar.DAY_OF_MONTH)}/${cal.get(Calendar.MONTH)+1}"
            totals.add(DailyTotal(label, sum))
        }
        emit(totals)
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setDate(date: Long) { _selectedDate.value = date }

    fun addExpense(expense: Expense) {
        viewModelScope.launch { repository.insertExpense(expense) }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { repository.deleteExpense(expense) }
    }

    private fun dayStart(time: Long) =
        Calendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

    private fun dayEnd(time: Long) =
        Calendar.getInstance().apply {
            timeInMillis = time
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
}