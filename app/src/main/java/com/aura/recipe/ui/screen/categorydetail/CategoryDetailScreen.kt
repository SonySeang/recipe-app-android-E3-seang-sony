package com.aura.recipe.ui.screen.categorydetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDetailScreen(
    navController: NavController,
    categoryId: String,
    categoryDetailViewModel: CategoryDetailViewModel = viewModel()
) {
    val uiState by categoryDetailViewModel.uiState.collectAsState()

    LaunchedEffect(categoryId) {
        if (categoryId.isNotBlank()) {
            categoryDetailViewModel.fetchCategoryDetails(categoryId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = uiState.categoryDetail?.name ?: "Category",
                        maxLines = 1,
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
                uiState.categoryDetail?.let { category ->
                    LazyColumn(
                        contentPadding = PaddingValues(20.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        item {
                            Card(
                                shape = RoundedCornerShape(24.dp),
                                elevation = CardDefaults.cardElevation(12.dp)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(category.imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = category.name,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(280.dp)
                                )
                            }
                        }
                        
                        item {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        
                        item {
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                elevation = CardDefaults.cardElevation(4.dp)
                            ) {
                                Text(
                                    text = category.description,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4,
                                    modifier = Modifier.padding(20.dp)
                                )
                            }
                        }
                        
                        item { Spacer(modifier = Modifier.height(20.dp)) }
                    }
                }
            }
        }
    }
}
