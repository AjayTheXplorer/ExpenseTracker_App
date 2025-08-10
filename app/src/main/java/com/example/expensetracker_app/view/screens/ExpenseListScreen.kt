package com.example.expensetracker_app.view.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensetracker_app.data.model.Expense
import com.example.expensetracker_app.viewModel.ExpenseViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(vm: ExpenseViewModel) {
    val expenses by vm.expenses.collectAsState()
    val total by vm.totalSpent.collectAsState()
    var groupByCategory by remember { mutableStateOf(false) }
    var selectedButton by remember { mutableStateOf("Today") }

    // 🔹 Auto-refresh when entering screen
    LaunchedEffect(Unit) {
        vm.refreshData() // Make sure your ViewModel has this method
    }

    // State for date picker
    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDate = remember { mutableStateOf(System.currentTimeMillis()) }
    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTopBar("Expenses", "${expenses.size} • ₹ ${"%.2f".format(total)}")
        Spacer(modifier = Modifier.height(8.dp))

        // Date selector row
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date display with picker icon
            Text(
                text = dateFormatter.format(Date(selectedDate.value)),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(16.dp),

            )

            IconButton(
                onClick = { showDatePicker = true },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
//                Text(text = "Select Date")
//                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = "Pick date",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quick date filters
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterButton(
                label = "Today",
                isSelected = selectedButton == "Today",
                onClick = {
                    selectedDate.value = System.currentTimeMillis()
                    vm.setDate(selectedDate.value)
                    selectedButton = "Today"
                }
            )

            FilterButton(
                label = "Yesterday",
                isSelected = selectedButton == "Yesterday",
                onClick = {
                    val newDate = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis
                    selectedDate.value = newDate
                    vm.setDate(selectedDate.value)
                    selectedButton = "Yesterday"
                }
            )

            FilterButton(
                label = if (groupByCategory) "Group: Category" else "Group: Time",
                isSelected = selectedButton == "Group",
                onClick = {
                    groupByCategory = !groupByCategory
                    selectedButton = "Group"
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (expenses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No expenses for ${dateFormatter.format(Date(selectedDate.value))}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            return@Column
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (!groupByCategory) {
                items(expenses) { e ->
                    ExpenseRow(
                        expense = e,
                        onDelete = { vm.deleteExpense(e) }
                    )
                }
            } else {
                // group by category
                val grouped = expenses.groupBy { it.category }
                grouped.forEach { (cat, list) ->
                    item {
                        CategoryHeader(cat, list.sumOf { it.amount })
                    }
                    items(list) { e ->
                        ExpenseRow(
                            expense = e,
                            onDelete = { vm.deleteExpense(e) }
                        )
                    }
                }
            }
        }
    }

    // Date picker dialog
    if (showDatePicker) {
        val calendar = Calendar.getInstance().apply { timeInMillis = selectedDate.value }
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val newDate = Calendar.getInstance().apply {
                            set(year, month, day)
                        }.timeInMillis

                        selectedDate.value = newDate
                        vm.setDate(newDate)
                        selectedButton = "Custom"
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        ) {
            DatePicker(
                date = selectedDate.value,
                onDateSelected = { newDate ->
                    selectedDate.value = newDate
                }
            )
        }
    }
}

@Composable
fun FilterButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.surfaceVariant,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) Color.Transparent
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (isSelected) 4.dp else 0.dp
        )
    ) {
        Text(label, fontSize = 12.sp)
    }
}

@Composable
fun DatePicker(
    date: Long,
    onDateSelected: (Long) -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = date }
    val year = remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    val month = remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    val day = remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    // Update calendar when values change
    LaunchedEffect(year.value, month.value, day.value) {
        calendar.set(year.value, month.value, day.value)
        onDateSelected(calendar.timeInMillis)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Month selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                month.value = (month.value - 1).mod(12)
                if (month.value == 11) year.value--
            }) {
                Icon(Icons.Filled.ArrowBack, "Previous month")
            }

            Text(
                text = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                    .format(calendar.time),
                style = MaterialTheme.typography.titleMedium
            )

            IconButton(onClick = {
                month.value = (month.value + 1).mod(12)
                if (month.value == 0) year.value++
            }) {
                Icon(Icons.Filled.ArrowForward, "Next month")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Day names header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { day ->
                Text(
                    day,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.width(32.dp).wrapContentWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Days grid
        val firstDayOfMonth = Calendar.getInstance().apply {
            set(year.value, month.value, 1)
        }
        val daysInMonth = firstDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
        val startOffset = firstDayOfMonth.get(Calendar.DAY_OF_WEEK) - 1

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.height(200.dp)
        ) {
            items(42) { index ->
                if (index >= startOffset && index < startOffset + daysInMonth) {
                    val dayNum = index - startOffset + 1
                    val isSelected = dayNum == day.value

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = if (isSelected) MaterialTheme.colorScheme.primary
                                else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable {
                                day.value = dayNum
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$dayNum",
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
fun CategoryHeader(category: String, total: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            category,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            "₹ ${"%.2f".format(total)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ExpenseRow(
    expense: Expense,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                Text(
                    expense.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    "${expense.category} • ${
                        SimpleDateFormat("hh:mm a", Locale.getDefault())
                            .format(Date(expense.date))
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            // Middle: Price
            Text(
                "₹ ${"%.2f".format(expense.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            // Right side: Delete button
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
