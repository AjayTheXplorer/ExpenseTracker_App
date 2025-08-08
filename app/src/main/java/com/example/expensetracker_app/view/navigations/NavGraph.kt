package com.example.expensetracker_app.view.navigations

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.expensetracker_app.view.screens.ExpenseEntryScreen
import com.example.expensetracker_app.view.screens.ExpenseListScreen
import com.example.expensetracker_app.view.screens.ExpenseReportScreen
import com.example.expensetracker_app.viewModel.ExpenseViewModel

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val vm: ExpenseViewModel = hiltViewModel()

    Scaffold(
        bottomBar = { BottomNav(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Entry.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Entry.route) {
                ExpenseEntryScreen(vm)
            }
            composable(Screen.List.route) {
                ExpenseListScreen(vm)
            }
            composable(Screen.Report.route) {
                ExpenseReportScreen(vm)
            }
        }
    }
}

@Composable
fun BottomNav(navController: NavHostController) {
    val currentRoute = remember { mutableStateOf(Screen.Entry.route) }
    NavigationBar {
        bottomScreens.forEach { screen ->
            NavigationBarItem(
                icon = { /* simple icon placeholder */ },
                label = { Text(screen.label) },
                selected = currentRoute.value == screen.route,
                onClick = {
                    currentRoute.value = screen.route
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
