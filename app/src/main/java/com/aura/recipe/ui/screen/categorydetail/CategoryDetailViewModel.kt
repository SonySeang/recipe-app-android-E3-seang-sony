package com.aura.recipe.ui.screen.categorydetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aura.recipe.data.RecipeRepository
import com.aura.recipe.data.model.CategoryDetail

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryDetailUiState(
    val categoryDetail: CategoryDetail? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

// Removed SavedStateHandle from constructor for simpler instantiation
class CategoryDetailViewModel : ViewModel() {
    private val repository: RecipeRepository = RecipeRepository()

    private val _uiState = MutableStateFlow(CategoryDetailUiState())
    val uiState: StateFlow<CategoryDetailUiState> = _uiState.asStateFlow()

    // Public function to fetch details, called from the screen
    fun fetchCategoryDetails(categoryId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val detail = repository.getCategoryDetails(categoryId)
                _uiState.update { it.copy(isLoading = false, categoryDetail = detail) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = "Failed to load category details: ${e.message}") }
            }
        }
    }
}