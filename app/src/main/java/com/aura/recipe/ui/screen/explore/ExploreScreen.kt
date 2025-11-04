package com.aura.recipe.ui.screen.explore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.aura.recipe.data.model.Category
import com.aura.recipe.data.model.Meal
import com.aura.recipe.ui.screen.home.BottomNavigationBar

@Composable
fun ExploreScreen(
    navController: NavController,
    exploreViewModel: ExploreViewModel = viewModel()
) {
    val uiState by exploreViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "🔍 Explore Recipes",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(20.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "explore",
                onNavigate = { route -> navController.navigate(route) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading && uiState.categories.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error!!,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "Categories",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
                        )
                        CategoryFilterChips(
                            categories = uiState.categories,
                            selectedCategory = uiState.selectedCategory,
                            onCategorySelected = { exploreViewModel.selectCategory(it) }
                        )
                    }
                    
                    item {
                        Text(
                            text = if (uiState.selectedCategory != null) 
                                "${uiState.selectedCategory!!.name} Meals" 
                            else 
                                "All Meals",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                    
                    if (uiState.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    } else {
                        items(uiState.filteredMeals) { meal ->
                            MealListItem(
                                meal = meal,
                                onClick = { navController.navigate("mealDetail/${meal.id}") },
                                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                            )
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterChips(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category?) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            FilterChip(
                onClick = { onCategorySelected(null) },
                label = { Text("All") },
                selected = selectedCategory == null,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
        
        items(categories) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category.name) },
                selected = category.id == selectedCategory?.id,
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    }
}

@Composable
fun MealListItem(
    meal: Meal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.height(100.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(meal.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = meal.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight()
            )
            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = meal.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Card(
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Text(
                        text = meal.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}