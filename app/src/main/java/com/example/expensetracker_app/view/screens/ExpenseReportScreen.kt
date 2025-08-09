package com.example.expensetracker_app.view.screens

import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.expensetracker_app.viewModel.ExpenseViewModel
import com.example.expensetracker_app.viewModel.WeeklyExpense
import java.io.File
import java.io.FileWriter
import androidx.compose.ui.platform.LocalContext

@Composable
fun ExpenseReportScreen(vm: ExpenseViewModel) {
    val context = LocalContext.current
    val weekly by vm.weeklySummary.collectAsStateWithLifecycle(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreenTopBar("7-Day Report")
        Spacer(modifier = Modifier.height(12.dp))

        if (weekly.isEmpty()) {
            Text("No data for last 7 days")
            return@Column
        }

        Text("Daily totals:")
        Spacer(modifier = Modifier.height(32.dp))

        val maxTotal = weekly.maxOf { it.total }.coerceAtLeast(1.0)
        val chartHeight = 150.dp
        val tickCount = 5
        val tickStep = maxTotal / tickCount

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom
        ) {
            // Y-axis labels perfectly aligned with grid lines
            Box(
                modifier = Modifier
                    .height(chartHeight)
                    .padding(end = 4.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val stepHeight = size.height / tickCount
                    for (i in 0..tickCount) {
                        val y = i * stepHeight
                        drawContext.canvas.nativeCanvas.apply {
                            val label = "₹${formatToK((tickCount - i) * tickStep)}"
                            drawText(
                                label,
                                0f,
                                y + 4.sp.toPx() / 2,
                                android.graphics.Paint().apply {
                                    color = android.graphics.Color.WHITE
                                    textSize = 12.sp.toPx()
                                }
                            )
                        }
                    }
                }
            }

            // Chart with bars + grid lines
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(chartHeight)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val stepHeight = size.height / tickCount
                    for (i in 0..tickCount) {
                        val y = i * stepHeight
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.3f),
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1f
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    weekly.forEach { dt ->
                        val barHeight = ((dt.total / maxTotal) * chartHeight.value).dp
                        Box(
                            modifier = Modifier
                                .width(20.dp)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Canvas(
                                modifier = Modifier
                                    .height(barHeight)
                                    .width(20.dp)
                            ) {
                                drawRect(
                                    brush = Brush.linearGradient(
                                        colors = listOf(Color.Blue, Color.Green)
                                    ),
                                    size = size
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // X-axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weekly.forEach { dt ->
                Text(dt.dayLabel, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ✅ CSV Download Button
        Button(onClick = {
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val csvFile = File(downloadsDir, "expense_report.csv")

            try {
                FileWriter(csvFile).use { writer ->
                    writer.append("Day,Total\n")
                    weekly.forEach { dt ->
                        writer.append("${dt.dayLabel},${dt.total}\n")
                    }
                }
                Toast.makeText(context, "CSV saved to ${csvFile.absolutePath}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error saving CSV: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("Download CSV")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text("Category-wise breakdown and export features can be added later.")
    }
}


// Helper function for compact K formatting
fun formatToK(value: Double): String {
    return when {
        value >= 1_000_000 -> String.format("%.1fM", value / 1_000_000)
        value >= 1_000 -> String.format("%.0fK", value / 1_000)
        else -> value.toInt().toString()
    }
}

