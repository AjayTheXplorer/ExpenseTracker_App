package com.example.expensetracker_app.view.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensetracker_app.viewModel.ExpenseViewModel

@Composable
fun ExpenseReportScreen(vm: ExpenseViewModel) {
    val weekly by vm.weeklySummary.collectAsState()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        ScreenTopBar("7-Day Report")
        Spacer(modifier = Modifier.height(12.dp))

        if (weekly.isEmpty()) {
            Text("No data for last 7 days")
            return@Column
        }

        Text("Daily totals:")
        Spacer(modifier = Modifier.height(8.dp))
        // Simple bar-like visualization using Canvas
        val max = weekly.maxOf { it.total }.coerceAtLeast(1.0)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            weekly.forEach { dt ->
                Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Bottom) {
                    Canvas(modifier = Modifier
                        .height((dt.total / max * 150).dp)
                        .fillMaxWidth()) {
                        // simple rect bar
                        drawRect(
                            brush = Brush.linearGradient(
                                colors = listOf(Color.Blue, Color.Green)
                            ),
                            size = size
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(dt.dayLabel, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Category-wise breakdown and export features can be added later.")
    }
}
