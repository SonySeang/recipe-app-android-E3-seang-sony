package com.aura.recipe.ui.screen.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.aura.recipe.data.local.FavoriteMeal
import com.aura.recipe.ui.screen.home.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    navController: NavController,
    favoritesViewModel: FavoritesViewModel = viewModel()
) {
    val uiState by favoritesViewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "favorites",
                onNavigate = { route -> navController.navigate(route) }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "❤️ My Favorites",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier.padding(8.dp)
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
                )
            } else if (uiState.favorites.isEmpty()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No favorites yet",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Start exploring recipes and save your favorites!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.favorites) { favorite ->
                        FavoriteRecipeCard(
                            meal = favorite,
                            onRemove = { favoritesViewModel.removeFavorite(favorite) },
                            onClick = { navController.navigate("mealDetail/${favorite.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteRecipeCard(
    meal: FavoriteMeal,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(modifier = Modifier.height(130.dp)) {
            Card(
                shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp),
                modifier = Modifier.weight(1f)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(meal.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = meal.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Column(
                modifier = Modifier
                    .weight(1.3f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = meal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f)
                        )
                    ) {
                        Text(
                            text = meal.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = onRemove) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove from favorites",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}