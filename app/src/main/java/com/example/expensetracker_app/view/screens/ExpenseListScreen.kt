package com.example.expensetracker_app.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.expensetracker_app.viewModel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExpenseListScreen(vm: ExpenseViewModel) {
    val expenses by vm.expenses.collectAsState()
    val total by vm.totalSpent.collectAsState()
    var groupByCategory by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTopBar("Expenses", "${expenses.size} • ₹ ${"%.2f".format(total)}")
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vm.setDate(System.currentTimeMillis()) }) { Text("Today") }
            Button(onClick = {
                // simple previous day switch (for demo) - could be replaced with date picker
                val newDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis
                vm.setDate(newDate)
            }) { Text("Yesterday") }
            Button(onClick = { groupByCategory = !groupByCategory }) { Text(if (groupByCategory) "Group: Category" else "Group: Time") }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (expenses.isEmpty()) {
            Text("No expenses yet", modifier = Modifier.padding(24.dp))
            return@Column
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!groupByCategory) {
                items(expenses) { e ->
                    ExpenseRow(e.title, e.amount, e.category, e.date, onDelete = { vm.deleteExpense(e) })
                }
            } else {
                // group by category
                val grouped = expenses.groupBy { it.category }
                grouped.forEach { (cat, list) ->
                    item {
                        Text(cat, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(vertical = 8.dp))
                    }
                    items(list) { e ->
                        ExpenseRow(e.title, e.amount, e.category, e.date, onDelete = { vm.deleteExpense(e) })
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseRow(title: String, amount: Double, category: String, date: Long, onDelete: ()->Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text("$category • ${SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(date))}", style = MaterialTheme.typography.bodySmall)
            }
            Column(horizontalAlignment = androidx.compose.ui.Alignment.End) {
                Text("₹ ${"%.2f".format(amount)}")
                Spacer(modifier = Modifier.height(6.dp))
                Text("Delete", modifier = Modifier.clickable { onDelete() }, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}