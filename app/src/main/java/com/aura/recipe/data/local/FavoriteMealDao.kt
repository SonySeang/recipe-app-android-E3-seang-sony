package com.aura.recipe.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteMealDao {
    @Query("SELECT * FROM favorite_meals")
    fun getAllFavorites(): Flow<List<FavoriteMeal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(meal: FavoriteMeal)

    @Delete
    suspend fun deleteFavorite(meal: FavoriteMeal)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_meals WHERE id = :mealId)")
    suspend fun isFavorite(mealId: String): Boolean
}