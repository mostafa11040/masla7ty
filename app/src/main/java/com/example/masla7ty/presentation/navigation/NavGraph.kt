package com.example.maslahty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.maslahty.presentation.screens.auth.LoginScreen
import com.example.maslahty.presentation.screens.auth.OTPVerificationScreen
import com.example.maslahty.presentation.screens.auth.RegistrationScreen
import com.example.maslahty.presentation.screens.home.HomeScreen
import com.example.maslahty.presentation.screens.transfer.ApprovalScreen
import com.example.maslahty.presentation.screens.transfer.ImageUploadScreen
import com.example.maslahty.presentation.screens.transfer.PricingScreen
import com.example.maslahty.presentation.screens.transfer.RequestDetailsScreen
import com.example.maslahty.presentation.screens.transfer.RequestsManagementScreen
import com.example.maslahty.presentation.screens.transfer.TransferRequestScreen
import com.example.maslahty.presentation.screens.transfer.VehicleDetailsScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }

        composable(
            route = Screen.OTPVerificationScreen.route,
            arguments = listOf(navArgument("phoneNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val phoneNumber = backStackEntry.arguments?.getString("phoneNumber").orEmpty()
            OTPVerificationScreen(navController = navController, phoneNumber = phoneNumber)
        }

        composable(Screen.RegistrationScreen.route) {
            RegistrationScreen(navController = navController)
        }

        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }

        composable(Screen.VehicleDetailsScreen.route) {
            VehicleDetailsScreen(navController = navController)
        }

        composable(
            route = Screen.ImageUploadScreen.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId").orEmpty()
            ImageUploadScreen(navController = navController, vehicleId = vehicleId)
        }

        composable(
            route = Screen.PricingScreen.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId").orEmpty()
            PricingScreen(navController = navController, vehicleId = vehicleId)
        }

        composable(
            route = Screen.TransferRequestScreen.route,
            arguments = listOf(navArgument("vehicleId") { type = NavType.StringType })
        ) { backStackEntry ->
            val vehicleId = backStackEntry.arguments?.getString("vehicleId").orEmpty()
            TransferRequestScreen(navController = navController, vehicleId = vehicleId)
        }

        composable(Screen.RequestsManagementScreen.route) {
            RequestsManagementScreen(navController = navController)
        }

        composable(
            route = Screen.ApprovalScreen.route,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId").orEmpty()
            ApprovalScreen(navController = navController, requestId = requestId)
        }

        composable(
            route = Screen.RequestDetailsScreen.route,
            arguments = listOf(navArgument("requestId") { type = NavType.StringType })
        ) { backStackEntry ->
            val requestId = backStackEntry.arguments?.getString("requestId").orEmpty()
            RequestDetailsScreen(navController = navController, requestId = requestId)
        }

        // More screens will be added here
    }
}

