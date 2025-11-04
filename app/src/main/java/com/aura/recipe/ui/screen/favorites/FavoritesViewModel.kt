package com.aura.recipe.ui.screen.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.aura.recipe.data.local.FavoriteMeal
import com.aura.recipe.data.local.RecipeDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favorites: List<FavoriteMeal> = emptyList(),
    val isLoading: Boolean = false
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val database = RecipeDatabase.getDatabase(application)
    private val favoriteMealDao = database.favoriteMealDao()

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            favoriteMealDao.getAllFavorites().collect { favorites ->
                _uiState.value = _uiState.value.copy(
                    favorites = favorites,
                    isLoading = false
                )
            }
        }
    }

    fun removeFavorite(meal: FavoriteMeal) {
        viewModelScope.launch {
            favoriteMealDao.deleteFavorite(meal)
        }
    }
}