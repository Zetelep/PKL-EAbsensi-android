package com.zulfa.eabsensi.presentation.main.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Corrected import
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.zulfa.eabsensi.R
import com.zulfa.eabsensi.presentation.components.BottomNavItem

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavItem("home", R.drawable.active_home, R.drawable.inactive_home),
        BottomNavItem("history", R.drawable.active_history, R.drawable.inactve_history)
    )

    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = Color.White,
        shadowElevation = 8.dp 
    ) {
        NavigationBar(
            containerColor = Color.Transparent, 
            modifier = Modifier.height(100.dp)
        ) {
            val currentBackStack by navController.currentBackStackEntryAsState()
            val currentRoute = currentBackStack?.destination?.route

            items.forEach { item ->
                val isSelected = currentRoute == item.route
                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        Image(
                            painter = painterResource(
                                id = if (isSelected) item.activeIcon else item.inactiveIcon
                            ),
                            contentDescription = item.route,
                            modifier = Modifier.size(34.dp)
                        )
                    },
                    label = null,
                    alwaysShowLabel = false
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomNavPreview() {
    val navController = rememberNavController()
    BottomNavigationBar(navController = navController)
}
