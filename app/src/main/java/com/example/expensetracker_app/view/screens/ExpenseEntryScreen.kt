package com.example.expensetracker_app.view.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
        Spacer(modifier = Modifier.height(8.dp))

        // Simple category row
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Staff","Travel","Food","Utility").forEach { cat ->
                Button(onClick = { category = cat }) {
                    Text(cat + if (cat==category) " ✓" else "")
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Mock receipt picker
        Button(onClick = { /* TODO: open image picker, here we mock */ receiptUri = "mock://receipt/${UUID.randomUUID()}" }) {
            Text(if (receiptUri == null) "Attach Receipt (mock)" else "Receipt attached")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
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