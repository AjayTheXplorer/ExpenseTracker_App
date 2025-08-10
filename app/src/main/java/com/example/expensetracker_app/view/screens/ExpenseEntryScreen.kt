package com.example.expensetracker_app.view.screens

import android.view.Gravity
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.expensetracker_app.data.model.Expense
import com.example.expensetracker_app.viewModel.ExpenseViewModel
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

//@Composable
//fun ExpenseEntryScreen(vm: ExpenseViewModel) {
//    val context = LocalContext.current
//    val total by vm.totalSpent.collectAsState()
//    var title by remember { mutableStateOf("") }
//    var amount by remember { mutableStateOf("") }
//    var notes by remember { mutableStateOf("") }
//    var category by remember { mutableStateOf("Staff") }
//    var receiptUri by remember { mutableStateOf<String?>(null) }
//
//    // Animation trigger
//    var showSuccessAnimation by remember { mutableStateOf(false) }
//
//    // Load Lottie composition
//    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success.json"))
//    val progress by animateLottieCompositionAsState(
//        composition = composition,
//        isPlaying = showSuccessAnimation,
//        iterations = 1,
//        speed = 1.5f,
//        restartOnPlay = true
//    )
//
//    // ✅ Wrap everything in a Box to allow overlay
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(modifier = Modifier.padding(16.dp)) {
//            ScreenTopBar("Add Expense", "Total spent today: ₹ ${"%.2f".format(total)}")
//            Spacer(modifier = Modifier.height(12.dp))
//
//            LabeledTextField("Title", title, { title = it })
//            Spacer(modifier = Modifier.height(8.dp))
//            LabeledTextField("Amount (₹)", amount, { amount = it })
//            Spacer(modifier = Modifier.height(8.dp))
//            LabeledTextField("Notes (optional)", notes, { if (it.length <= 100) notes = it })
//
//            Spacer(modifier = Modifier.height(16.dp))
////            CategoryPicker()
//            CategoryPicker(
//                selectedCategory = category,
//                onCategorySelected = { category = it }
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Button(onClick = {
//                receiptUri = "mock://receipt/${UUID.randomUUID()}"
//            }) {
//                Text(if (receiptUri == null) "Attach Receipt (mock)" else "Receipt attached")
//            }
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                onClick = {
//                    val amt = amount.toDoubleOrNull()
//
//                    fun showTopToast(message: String) {
//                        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
//                        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 100)
//                        toast.show()
//                    }
//
//                    when {
//                        title.isBlank() -> {
//                            showTopToast("Please enter a title")
//                            return@Button
//                        }
//                        amt == null || amt <= 0.0 -> {
//                            showTopToast("Please enter a valid amount greater than ₹0")
//                            return@Button
//                        }
//                        else -> {
//                            val expense = Expense(
//                                title = title.trim(),
//                                amount = amt,
//                                category = category,
//                                notes = notes.takeIf { it.isNotBlank() },
//                                date = System.currentTimeMillis(),
//                                receiptImageUri = receiptUri
//                            )
//                            vm.addExpense(expense)
//
//                            // Reset fields
//                            title = ""
//                            amount = ""
//                            notes = ""
//                            receiptUri = null
//
//                            showTopToast("Expense added!")
//
//                            // Trigger animation
//                            showSuccessAnimation = true
//                        }
//                    }
//                }
//
//
//
//            ) {
//                Text("Add Expense")
//            }
//        }
//
//        // ✅ Overlay animation like a modal
//        if (showSuccessAnimation) {
//            LaunchedEffect(Unit) {
//                delay(1500) // Adjust based on animation duration
//                showSuccessAnimation = false
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.5f)), // Dim background
//                contentAlignment = Alignment.Center
//            ) {
//                LottieAnimation(
//                    composition = composition,
//                    progress = { progress },
//                    modifier = Modifier
//                        .size(200.dp)
//                )
//            }
//        }
//    }
//}

@Composable
fun ExpenseEntryScreen(vm: ExpenseViewModel) {
    val context = LocalContext.current
    val total by vm.totalSpent.collectAsState()
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Staff") }
    var receiptUri by remember { mutableStateOf<String?>(null) }
    var expenseDate by remember { mutableStateOf(System.currentTimeMillis()) }

    var showSuccessAnimation by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = showSuccessAnimation,
        iterations = 1,
        speed = 1.5f,
        restartOnPlay = true
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Add Expense", style = MaterialTheme.typography.titleLarge)
                    Text("Total spent today: ₹ ${"%.2f".format(total)}",
                        style = MaterialTheme.typography.bodyMedium)
                }

                DateSelectorButton(
                    date = expenseDate,
                    onClick = { showDatePicker = true }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            LabeledTextField("Title", title, { title = it })
            Spacer(modifier = Modifier.height(8.dp))
            LabeledTextField("Amount (₹)", amount, { amount = it })
            Spacer(modifier = Modifier.height(8.dp))
            LabeledTextField("Notes (optional)", notes, { if (it.length <= 100) notes = it })

            Spacer(modifier = Modifier.height(16.dp))
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
                                date = expenseDate,
                                receiptImageUri = receiptUri
                            )
                            vm.addExpense(expense)

                            title = ""
                            amount = ""
                            notes = ""
                            receiptUri = null

//                            showTopToast("Expense added!")

                            showSuccessAnimation = true
                        }
                    }
                }
            ) {
                Text("Add Expense")
            }
        }

        if (showSuccessAnimation) {
            LaunchedEffect(Unit) {
                delay(1500)
                showSuccessAnimation = false
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                )
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                date = expenseDate,
                onDateSelected = { newDate ->
                    expenseDate = newDate
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Composable
fun DateSelectorButton(
    date: Long,
    onClick: () -> Unit
) {
    val dateFormatter = remember { SimpleDateFormat("dd MMM", Locale.getDefault()) }

    OutlinedButton(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.CalendarMonth,
                contentDescription = "Select date",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = dateFormatter.format(Date(date)),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun DatePickerDialog(
    date: Long,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    var tempDate by remember { mutableStateOf(date) } // temporary selected date

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Select Date",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Filled.Close, "Close")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomDatePicker(
                    date = tempDate,
                    onDateChanged = { newDate -> tempDate = newDate }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        onDateSelected(tempDate) // ✅ Pass the updated date
//                        onDismiss()
                    }) {
                        Text("Select Date")
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDatePicker(
    date: Long,
    onDateChanged: (Long) -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = date }
    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var selectedDay by remember { mutableStateOf(calendar.get(Calendar.DAY_OF_MONTH)) }

    val monthNames = remember {
        listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    }

//    LaunchedEffect(selectedYear, selectedMonth, selectedDay) {
//        calendar.set(selectedYear, selectedMonth, selectedDay)
//        onDateChanged(calendar.timeInMillis)
//    }

    LaunchedEffect(selectedYear, selectedMonth, selectedDay) {
        calendar.set(selectedYear, selectedMonth, selectedDay)
        onDateChanged(calendar.timeInMillis)
    }


    // Year picker
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { selectedYear-- }) {
            Icon(Icons.Filled.ArrowBack, "Previous year")
        }
        Text(
            text = selectedYear.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = { selectedYear++ }) {
            Icon(Icons.Filled.ArrowForward, "Next year")
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Month picker
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier.height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(monthNames.size) { index ->
            val isSelected = index == selectedMonth
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clickable { selectedMonth = index },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = monthNames[index],
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Day picker
    val daysInMonth = remember(selectedYear, selectedMonth) {
        Calendar.getInstance().apply {
            set(selectedYear, selectedMonth, 1)
        }.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(7),
        modifier = Modifier.height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(daysInMonth) { day ->
            val dayNumber = day + 1
            val isSelected = dayNumber == selectedDay
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .background(
                        color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape
                    )
                    .clickable { selectedDay = dayNumber },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayNumber.toString(),
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPicker(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = listOf("Staff", "Travel", "Food", "Utility")

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Select Category")
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedCategory,
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
                            onCategorySelected(cat)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
