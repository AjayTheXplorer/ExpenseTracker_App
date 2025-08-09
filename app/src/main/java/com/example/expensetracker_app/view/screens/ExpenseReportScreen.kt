package com.example.expensetracker_app.view.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
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
import com.example.expensetracker_app.viewModel.ExpenseViewModel

@Composable
fun ExpenseReportScreen(vm: ExpenseViewModel) {
    val weekly by vm.weeklySummary.collectAsState()

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
                        // Draw text aligned exactly with the grid line
                        drawContext.canvas.nativeCanvas.apply {
                            val label = "₹${formatToK((tickCount - i) * tickStep)}"
                            drawText(
                                label,
                                0f,
                                y + 4.sp.toPx() / 2, // small offset for visual centering
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
                // Grid lines
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

                // Bars
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

