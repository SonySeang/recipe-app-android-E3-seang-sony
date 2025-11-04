package com.aura.recipe.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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


@Composable
fun HomeScreen(navController: NavController, homeViewModel: HomeViewModel = viewModel()) {
    val uiState by homeViewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        if (uiState.isLoading && uiState.categories.isEmpty()) { // Show full screen loader only on initial load
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.Center).padding(16.dp)
            )
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                item { GreetingSection(name = uiState.userName) }
                item {
                    SectionTitle(title = "Featured")
                    FeaturedRecipes(recipes = uiState.featuredRecipes, navController = navController)
                }
                item {
                    val categoryId = uiState.selectedCategory?.id ?: ""
                    SectionTitle(
                        title = "Category",
                        action = "See All",
                        onActionClick = { if(categoryId.isNotEmpty()) navController.navigate("categoryDetail/$categoryId") }
                    )
                    CategorySelection(
                        categories = uiState.categories,
                        selectedCategory = uiState.selectedCategory,
                        onCategorySelected = { homeViewModel.selectCategory(it) }
                    )
                }
                item { SectionTitle(title = "Popular Recipes", action = "See All") }
                items(uiState.popularRecipes) { recipe ->
                    PopularRecipeCard(
                        recipe = recipe,
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clickable { navController.navigate("mealDetail/${recipe.id}") }
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingSection(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "☀️ Good Morning", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            Text(text = name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        }
        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Cart", modifier = Modifier.size(28.dp))
    }
}

@Composable
fun SectionTitle(title: String, action: String? = null, onActionClick: () -> Unit = {}) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        action?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}

@Composable
fun FeaturedRecipes(recipes: List<Meal>, navController: NavController) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(recipes) { recipe ->
            FeaturedRecipeCard(recipe, onClick = { navController.navigate("mealDetail/${recipe.id}") })
        }
    }
}

@Composable
fun FeaturedRecipeCard(recipe: Meal, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.width(280.dp).clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.height(180.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(recipe.imageUrl).crossfade(true).build(),
                contentDescription = recipe.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))
            Text(
                text = recipe.name,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
            )
        }
    }
}

@Composable
fun CategorySelection(
    categories: List<Category>,
    selectedCategory: Category?,
    onCategorySelected: (Category) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category.id == selectedCategory?.id
            Button(
                onClick = { onCategorySelected(category) },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text(text = category.name)
            }
        }
    }
}

@Composable
fun PopularRecipeCard(recipe: Meal, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.height(120.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(recipe.imageUrl).crossfade(true).build(),
                contentDescription = recipe.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.weight(0.8f).clip(RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp))
            )
            Column(
                modifier = Modifier.weight(1f).padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = recipe.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 2)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = recipe.category, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}