package com.aura.recipe.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(
    entities = [FavoriteMeal::class],
    version = 1,
    exportSchema = false
)
abstract class RecipeDatabase : RoomDatabase() {
    abstract fun favoriteMealDao(): FavoriteMealDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}