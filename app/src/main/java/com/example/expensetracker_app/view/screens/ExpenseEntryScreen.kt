package com.example.expensetracker_app.view.screens

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.expensetracker_app.data.model.Expense
import com.example.expensetracker_app.viewModel.ExpenseViewModel
import kotlinx.coroutines.delay
import java.util.*

@Composable
fun ExpenseEntryScreen(vm: ExpenseViewModel) {
    val context = LocalContext.current
    val total by vm.totalSpent.collectAsState()
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Staff") }
    var receiptUri by remember { mutableStateOf<String?>(null) }

    // Animation trigger
    var showSuccessAnimation by remember { mutableStateOf(false) }

    // Load Lottie composition
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = showSuccessAnimation,
        iterations = 1,
        speed = 1.5f,
        restartOnPlay = true
    )

    // ✅ Wrap everything in a Box to allow overlay
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            ScreenTopBar("Add Expense", "Total spent today: ₹ ${"%.2f".format(total)}")
            Spacer(modifier = Modifier.height(12.dp))

            LabeledTextField("Title", title, { title = it })
            Spacer(modifier = Modifier.height(8.dp))
            LabeledTextField("Amount (₹)", amount, { amount = it })
            Spacer(modifier = Modifier.height(8.dp))
            LabeledTextField("Notes (optional)", notes, { if (it.length <= 100) notes = it })

            Spacer(modifier = Modifier.height(16.dp))
//            CategoryPicker()
            CategoryPicker(
                selectedCategory = category,
                onCategorySelected = { category = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = {
                receiptUri = "mock://receipt/${UUID.randomUUID()}"
            }) {
                Text(if (receiptUri == null) "Attach Receipt (mock)" else "Receipt attached")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    val amt = amount.toDoubleOrNull()

                    fun showTopToast(message: String) {
                        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 100)
                        toast.show()
                    }

                    when {
                        title.isBlank() -> {
                            showTopToast("Please enter a title")
                            return@Button
                        }
                        amt == null || amt <= 0.0 -> {
                            showTopToast("Please enter a valid amount greater than ₹0")
                            return@Button
                        }
                        else -> {
                            val expense = Expense(
                                title = title.trim(),
                                amount = amt,
                                category = category,
                                notes = notes.takeIf { it.isNotBlank() },
                                date = System.currentTimeMillis(),
                                receiptImageUri = receiptUri
                            )
                            vm.addExpense(expense)

                            // Reset fields
                            title = ""
                            amount = ""
                            notes = ""
                            receiptUri = null

                            showTopToast("Expense added!")

                            // Trigger animation
                            showSuccessAnimation = true
                        }
                    }
                }



            ) {
                Text("Add Expense")
            }
        }

        // ✅ Overlay animation like a modal
        if (showSuccessAnimation) {
            LaunchedEffect(Unit) {
                delay(1500) // Adjust based on animation duration
                showSuccessAnimation = false
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)), // Dim background
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .size(200.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPicker(
    selectedCategory: String,  // Receive selected category from parent
    onCategorySelected: (String) -> Unit  // Receive selection callback
) {
    var expanded by remember { mutableStateOf(false) }
//    var category by remember { mutableStateOf("Staff") }
    val categories = listOf("Staff", "Travel", "Food", "Utility")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Select Category")
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedCategory,  // Use the passed selected category
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { cat ->
                    DropdownMenuItem(
                        text = { Text(cat) },
                        onClick = {
                            onCategorySelected(cat)  // Call the callback with selected category
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}