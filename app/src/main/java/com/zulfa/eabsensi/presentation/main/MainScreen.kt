package com.zulfa.eabsensi.presentation.main

import HomeScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zulfa.eabsensi.presentation.main.history.HistoryScreen
import com.zulfa.eabsensi.presentation.main.navigation.BottomNavigationBar
import com.zulfa.eabsensi.presentation.main.navigation.MainRoutes
import com.zulfa.eabsensi.presentation.main.navigation.Routes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(
    startDestination: String = MainRoutes.Home, // default home
    rootNavController: NavHostController
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = Color(0xFFDDDDDD)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(MainRoutes.Home) { HomeScreen() }
            composable(MainRoutes.History) { HistoryScreen(
                onLogout = {
                    // ✅ When logout succeeds → navigate to login and clear back stack
                    rootNavController.navigate(Routes.Login) {
                        popUpTo(Routes.Main) { inclusive = true }
                    }
                }
                )
            }
        }
    }
}