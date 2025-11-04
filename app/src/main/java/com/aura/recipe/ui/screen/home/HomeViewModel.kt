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
    val selectedCategory: Category? = null,
    val featuredRecipes: List<Meal> = emptyList(),
    val popularRecipes: List<Meal> = emptyList(),
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
                val selectedCategory = categories.firstOrNull()

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        featuredRecipes = meals.take(5), // Example: first 5 are "featured"
                        selectedCategory = selectedCategory,
                        popularRecipes = meals.filter { meal -> meal.category == selectedCategory?.name }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load data: ${e.message}") }
            }
        }
    }

    fun selectCategory(category: Category) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val meals = repository.getMeals()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        selectedCategory = category,
                        popularRecipes = meals.filter { meal -> meal.category == category.name }
                    )
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to filter meals: ${e.message}") }
            }
        }
    }
}