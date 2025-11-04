package com.aura.recipe.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_meals")
data class FavoriteMeal(
    @PrimaryKey val id: String,
    val name: String,
    val imageUrl: String,
    val category: String
)