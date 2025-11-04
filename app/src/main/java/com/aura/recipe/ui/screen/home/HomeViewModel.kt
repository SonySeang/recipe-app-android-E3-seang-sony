package com.aura.recipe.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.recipe.data.RecipeRepository
import com.aura.recipe.data.model.Category
import com.aura.recipe.data.model.Meal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val userName: String = "Alena Sabyan",
    val categories: List<Category> = emptyList(),
    val randomMeal: Meal? = null,
    val popularMeals: List<Meal> = emptyList(),
    val areas: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class HomeViewModel(private val repository: RecipeRepository = RecipeRepository()) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val categories = repository.getCategories()
                val meals = repository.getMeals()
                val areas = listOf("Italian", "Chinese", "Mexican", "Indian", "French", "Japanese")

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        popularMeals = meals.take(10),
                        areas = areas
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load data: ${e.message}") }
            }
        }
    }

    fun loadRandomMeal() {
        viewModelScope.launch {
            try {
                val meals = repository.getMeals()
                val randomMeal = meals.randomOrNull()
                _uiState.update { it.copy(randomMeal = randomMeal) }
            } catch (e: Exception) {
                // Handle error silently for random meal
            }
        }
    }


}