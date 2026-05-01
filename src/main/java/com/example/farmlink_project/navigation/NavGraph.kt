package com.example.farmlink_project.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.farmlink_project.*

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "farmlink_screen"
    ) {
        composable("farmlink_screen") {
            FarmLinkScreen(navController)
        }

        composable("home_screen") {
            HomepageScreen(navController)
        }

        composable("sign_in_screen") {
            SignInLayout(navController)
        }

        composable("chat_screen") {
            ChatScreen(navController = navController)
        }

        composable("marketplace_screen") {
            MarketplaceScreen(
                navigateToDetails = { selectedItem ->
                    navController.navigate("details/$selectedItem")
                },
                navigateToCart = {
                    navController.navigate("cart_screen")
                }
            )
        }

        composable(
            "details/{itemName}",
            arguments = listOf(navArgument("itemName") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemName = backStackEntry.arguments?.getString("itemName") ?: ""
            DetailsScreen(
                itemName = itemName,
                navigateBack = { navController.popBackStack() },
                navigateToCart = { navController.navigate("cart_screen") },
                navController = navController
            )
        }

        composable("cart_screen") {
            ShoppingCartScreen(navController = navController)
        }

        composable("order_list") {
            OrderListScreen(navController = navController)
        }

        // Add ProfileScreen to the navigation graph
        composable("profile_screen") {
            ProfileScreen(navController = navController)
        }

        composable("farmlink_screen") { FarmLinkScreen(navController) }
        composable("sign_in_screen") { SignInLayout(navController) }
        composable("register_screen") { RegisterLayout(navController) }

    }
}
