package com.aura.recipe.ui.screen.mealdetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aura.recipe.data.RecipeRepository
import com.aura.recipe.data.model.MealDetail
import com.aura.recipe.data.local.FavoriteMeal
import com.aura.recipe.data.local.RecipeDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MealDetailUiState(
    val mealDetail: MealDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isFavorite: Boolean = false
)

class MealDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: RecipeRepository = RecipeRepository()
    private val database = RecipeDatabase.getDatabase(application)
    private val favoriteMealDao = database.favoriteMealDao()

    private val _uiState = MutableStateFlow(MealDetailUiState())
    val uiState: StateFlow<MealDetailUiState> = _uiState.asStateFlow()

    fun fetchMealDetails(mealId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val detail = repository.getMealDetails(mealId)
                val isFavorite = favoriteMealDao.isFavorite(mealId)
                _uiState.update { it.copy(isLoading = false, mealDetail = detail, isFavorite = isFavorite) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load meal details: ${e.message}") }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch {
            val meal = _uiState.value.mealDetail ?: return@launch
            val favoriteMeal = FavoriteMeal(
                id = meal.id,
                name = meal.mealName,
                imageUrl = meal.mealThumb,
                category = meal.category ?: ""
            )
            
            if (_uiState.value.isFavorite) {
                favoriteMealDao.deleteFavorite(favoriteMeal)
                _uiState.update { it.copy(isFavorite = false) }
            } else {
                favoriteMealDao.insertFavorite(favoriteMeal)
                _uiState.update { it.copy(isFavorite = true) }
            }
        }
    }
}