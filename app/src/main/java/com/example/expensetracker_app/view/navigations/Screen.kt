package com.example.expensetracker_app.view.navigations

sealed class Screen(val route: String, val label: String) {
    object Entry : Screen("entry", "Add")
    object List : Screen("list", "List")
    object Report : Screen("report", "Report")
}

val bottomScreens = listOf(Screen.Entry, Screen.List, Screen.Report)