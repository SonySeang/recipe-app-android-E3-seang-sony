package com.aura.recipe.data

import com.aura.recipe.data.model.Category
import com.aura.recipe.data.model.CategoryDetail
import com.aura.recipe.data.model.Meal
import com.aura.recipe.data.model.MealDetail
import com.aura.recipe.data.network.ApiService
import com.aura.recipe.data.network.RetrofitInstance


class RecipeRepository(private val apiService: ApiService = RetrofitInstance.api) {
    // Updated to match the new ApiService return types
    suspend fun getMeals(): List<Meal> = apiService.getMeals()
    suspend fun getCategories(): List<Category> = apiService.getCategories()
    suspend fun getMealDetails(mealId: String): MealDetail = apiService.getMealDetails(mealId)
    suspend fun getCategoryDetails(categoryId: String): CategoryDetail = apiService.getCategoryDetails(categoryId)
}