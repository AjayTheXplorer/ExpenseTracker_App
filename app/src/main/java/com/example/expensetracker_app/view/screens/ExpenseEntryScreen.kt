package com.example.expensetracker_app.view.screens

import android.R
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensetracker_app.data.model.Expense
import com.example.expensetracker_app.viewModel.ExpenseViewModel
import java.util.*

@Composable
fun ExpenseEntryScreen(vm: ExpenseViewModel) {
    val total by vm.totalSpent.collectAsState()
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Food") }
    var receiptUri by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        ScreenTopBar("Add Expense", "Total spent today: ₹ ${"%.2f".format(total)}")
        Spacer(modifier = Modifier.height(12.dp))

        LabeledTextField("Title", title, { title = it })
        Spacer(modifier = Modifier.height(8.dp))
        LabeledTextField("Amount (₹)", amount, { amount = it })
        Spacer(modifier = Modifier.height(8.dp))
        LabeledTextField("Notes (optional)", notes, { if (it.length <= 100) notes = it })


        Spacer(modifier = Modifier.height(16.dp))

        // Simple category row
        CategoryPicker()


        Spacer(modifier = Modifier.height(32.dp))

        // Mock receipt picker
        Button(onClick = { /* TODO: open image picker, here we mock */ receiptUri = "mock://receipt/${UUID.randomUUID()}" }) {
            Text(if (receiptUri == null) "Attach Receipt (mock)" else "Receipt attached")
        }

        Spacer(modifier = Modifier.height(100.dp))

        Button(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),

            onClick = {
            val amt = amount.toDoubleOrNull() ?: 0.0
            if (title.isBlank() || amt <= 0.0) {
                // show a simple validation - replace with snackbar/toast
                return@Button
            }
            val expense = Expense(
                title = title.trim(),
                amount = amt,
                category = category,
                notes = notes.takeIf { it.isNotBlank() },
                date = System.currentTimeMillis(),
                receiptImageUri = receiptUri
            )
            vm.addExpense(expense)
            // reset fields
            title = ""
            amount = ""
            notes = ""
            receiptUri = null
        }) {
            Text("Add Expense")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPicker() {
    var expanded by remember { mutableStateOf(false) }
    var category by remember { mutableStateOf("Staff") }
    val categories = listOf("Staff", "Travel", "Food", "Utility")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Select Category")
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            category = cat
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}