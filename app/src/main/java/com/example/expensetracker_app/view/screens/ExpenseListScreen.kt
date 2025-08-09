package com.example.expensetracker_app.view.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        var selectedButton by remember { mutableStateOf("Today") }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    vm.setDate(System.currentTimeMillis())
                    selectedButton = "Today"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "Today") Color.Gray else Color.DarkGray
                )
            ) {
                Text("Today", color = Color.White)
            }

            Button(
                onClick = {
                    val newDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis
                    vm.setDate(newDate)
                    selectedButton = "Yesterday"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "Yesterday") Color.Gray else Color.DarkGray
                )
            ) {
                Text("Yesterday", color = Color.White)
            }

            Button(
                onClick = {
                    groupByCategory = !groupByCategory
                    selectedButton = "Group"
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedButton == "Group") Color.Gray else Color.DarkGray
                )
            ) {
                Text(
                    if (groupByCategory) "Group: Category" else "Group: Time",
                    color = Color.White
                )
            }
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
fun ExpenseRow(
    title: String,
    amount: Double,
    category: String,
    date: Long,
    onDelete: () -> Unit,
//    onUpdate: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side: Title, category & time
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    "$category • ${
                        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(date))
                    }",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Middle: Price
            Text(
                "₹ ${"%.2f".format(amount)}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Right side: Update & Delete buttons
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row {

//                    Icon(
//                        modifier = Modifier
////                            .clickable{ onDelete() }
//                            .size(25.dp),
//                        imageVector = Icons.Default.Edit,
//                        contentDescription = "Delete",
//                        tint = Color.Yellow,
//                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Icon(modifier = Modifier
                        .clickable{ onDelete() }
                        .size(25.dp),
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                    )

                }
            }
        }
    }
}
