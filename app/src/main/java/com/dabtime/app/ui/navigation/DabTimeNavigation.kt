package com.dabtime.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dabtime.app.ui.screens.auth.AuthScreen
import com.dabtime.app.ui.screens.main.MainScreen
import com.dabtime.app.ui.screens.timer.TimerScreen
import com.dabtime.app.ui.screens.leaderboard.LeaderboardScreen
import com.dabtime.app.ui.screens.achievements.AchievementsScreen
import com.dabtime.app.ui.screens.social.SocialScreen
import com.dabtime.app.ui.screens.settings.SettingsScreen

@Composable
fun DabTimeNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Main.route) {
            MainScreen(
                onNavigateToTimer = { navController.navigate(Screen.Timer.route) },
                onNavigateToLeaderboard = { navController.navigate(Screen.Leaderboard.route) },
                onNavigateToAchievements = { navController.navigate(Screen.Achievements.route) },
                onNavigateToSocial = { navController.navigate(Screen.Social.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        
        composable(Screen.Timer.route) {
            TimerScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Leaderboard.route) {
            LeaderboardScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Achievements.route) {
            AchievementsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Social.route) {
            SocialScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onSignOut = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object Auth : Screen("auth")
    object Main : Screen("main")
    object Timer : Screen("timer")
    object Leaderboard : Screen("leaderboard")
    object Achievements : Screen("achievements")
    object Social : Screen("social")
    object Settings : Screen("settings")
}