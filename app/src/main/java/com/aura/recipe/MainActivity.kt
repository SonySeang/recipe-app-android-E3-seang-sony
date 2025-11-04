package com.aura.recipe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.aura.recipe.ui.screen.categorydetail.CategoryDetailScreen
import com.aura.recipe.ui.screen.mealdetail.MealDetailScreen
import com.aura.recipe.ui.screen.home.HomeScreen
import com.aura.recipe.ui.theme.RecipeTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecipeTheme {
                RecipeAppNavHost()
            }
        }
    }
}

@Composable
fun RecipeAppNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController)
        }
        composable(
            route = "mealDetail/{mealId}",
            arguments = listOf(navArgument("mealId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extract the mealId and pass it to the screen
            val mealId = backStackEntry.arguments?.getString("mealId")
            requireNotNull(mealId) { "mealId parameter was not found." }
            MealDetailScreen(navController = navController, mealId = mealId)
        }
        composable(
            route = "categoryDetail/{categoryId}",
            arguments = listOf(navArgument("categoryId") { type = NavType.StringType })
        ) { backStackEntry ->
            // Extract the categoryId and pass it to the screen
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            requireNotNull(categoryId) { "categoryId parameter was not found." }
            CategoryDetailScreen(navController = navController, categoryId = categoryId)
        }
    }
}
