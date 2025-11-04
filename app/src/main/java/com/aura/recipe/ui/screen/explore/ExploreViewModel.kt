package com.aura.recipe.ui.screen.explore

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

data class ExploreUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val allMeals: List<Meal> = emptyList(),
    val filteredMeals: List<Meal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ExploreViewModel(
    private val repository: RecipeRepository = RecipeRepository()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExploreUiState(isLoading = true))
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val categories = repository.getCategories()
                val meals = repository.getMeals()
                
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        categories = categories,
                        allMeals = meals,
                        filteredMeals = meals
                    )
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = "Failed to load data: ${e.message}"
                    ) 
                }
            }
        }
    }

    fun selectCategory(category: Category?) {
        _uiState.update { currentState ->
            val filteredMeals = if (category == null) {
                currentState.allMeals
            } else {
                currentState.allMeals.filter { meal -> 
                    meal.category == category.name 
                }
            }
            
            currentState.copy(
                selectedCategory = category,
                filteredMeals = filteredMeals
            )
        }
    }
}