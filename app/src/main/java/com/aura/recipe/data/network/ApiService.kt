package com.aura.recipe.data.network

import com.aura.recipe.data.model.Category
import com.aura.recipe.data.model.CategoryDetail
import com.aura.recipe.data.model.Meal
import com.aura.recipe.data.model.MealDetail
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    // Corrected to expect a direct List<Meal>
    @GET("meals")
    suspend fun getMeals(@Header("X-DB-NAME") dbName: String = ApiConstants.DB_NAME): List<Meal>

    // Corrected to expect a direct List<Category>
    @GET("categories")
    suspend fun getCategories(@Header("X-DB-NAME") dbName: String = ApiConstants.DB_NAME): List<Category>

    // Corrected to expect a single MealDetail object
    @GET("meals/{id}")
    suspend fun getMealDetails(
        @Path("id") mealId: String,
        @Header("X-DB-NAME") dbName: String = ApiConstants.DB_NAME
    ): MealDetail

    @GET("categories/{id}")
    suspend fun getCategoryDetails(
        @Path("id") categoryId: String,
        @Header("X-DB-NAME") dbName: String = ApiConstants.DB_NAME
    ): CategoryDetail
}