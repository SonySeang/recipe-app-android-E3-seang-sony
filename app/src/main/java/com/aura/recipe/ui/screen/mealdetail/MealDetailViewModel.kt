package com.aura.recipe.ui.screen.mealdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.recipe.data.RecipeRepository
import com.aura.recipe.data.model.MealDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MealDetailUiState(
    val mealDetail: MealDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

// Removed SavedStateHandle from constructor for simpler instantiation
class MealDetailViewModel : ViewModel() {
    private val repository: RecipeRepository = RecipeRepository()

    private val _uiState = MutableStateFlow(MealDetailUiState())
    val uiState: StateFlow<MealDetailUiState> = _uiState.asStateFlow()

    // Public function to fetch details, called from the screen
    fun fetchMealDetails(mealId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val detail = repository.getMealDetails(mealId)
                _uiState.update { it.copy(isLoading = false, mealDetail = detail) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load meal details: ${e.message}") }
            }
        }
    }
}