package com.aura.recipe.ui.screen.mealdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aura.recipe.data.model.IngredientDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealDetailScreen(
    navController: NavController,
    mealId: String, // Accept mealId directly
    mealDetailViewModel: MealDetailViewModel = viewModel()
) {
    val uiState by mealDetailViewModel.uiState.collectAsState()

    // Fetch data when the screen is first displayed
    LaunchedEffect(mealId) {
        mealDetailViewModel.fetchMealDetails(mealId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.mealDetail?.mealName ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                uiState.mealDetail?.let { meal ->
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        item {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current).data(meal.mealThumb).crossfade(true).build(),
                                contentDescription = meal.mealName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth().height(250.dp).clip(RoundedCornerShape(16.dp))
                            )
                            Spacer(Modifier.height(16.dp))
                            Text(meal.mealName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(16.dp))
                        }

                        item { Text("Ingredients", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                        items(meal.ingredients) { ingredient ->
                            IngredientItem(ingredient)
                        }

                        item { Spacer(Modifier.height(16.dp)) }

                        item {
                            Text("Instructions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(meal.instructions, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IngredientItem(ingredient: IngredientDetail) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(ingredient.ingredient, Modifier.weight(1f))
        Text(ingredient.measure, fontWeight = FontWeight.Medium)
    }
}
