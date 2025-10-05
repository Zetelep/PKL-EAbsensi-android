    package com.zulfa.eabsensi

    import android.os.Build
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.activity.enableEdgeToEdge
    import androidx.annotation.RequiresApi
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import com.zulfa.eabsensi.core.data.pref.UserModel
    import com.zulfa.eabsensi.core.data.pref.UserPreference
    import com.zulfa.eabsensi.presentation.auth.login.LoginScreen
    import com.zulfa.eabsensi.presentation.auth.onboarding.OnboardingScreen
    import com.zulfa.eabsensi.presentation.main.MainScreen
    import com.zulfa.eabsensi.presentation.main.navigation.Routes
    import com.zulfa.eabsensi.presentation.theme.EAbsensiTheme
    import org.koin.android.ext.android.inject

    class MainActivity : ComponentActivity() {

        private val userPreference: UserPreference by inject()

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContent {
                EAbsensiTheme {
                    val userSession by userPreference.getSession()
                        .collectAsState(initial = UserModel("", "", "", "", false))

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = if (userSession.isLogin) Routes.Main else Routes.Onboarding
                    ) {
                        // Onboarding → after finish go to Login
                        composable(Routes.Onboarding) {
                            OnboardingScreen(onFinish = {
                                navController.navigate(Routes.Login) {
                                    popUpTo(Routes.Onboarding) { inclusive = true }
                                }
                            })
                        }

                        // Login → after success go to Main (with BottomNav)
                        composable(Routes.Login) {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate(Routes.Main) {
                                    popUpTo(Routes.Login) { inclusive = true }
                                }
                            })
                        }

                        // Main screen hosts bottom navigation
                        composable(Routes.Main) {
                            MainScreen(startDestination = "home",
                                rootNavController = navController
                            )

                        }
                    }
                }
            }
        }
    }

