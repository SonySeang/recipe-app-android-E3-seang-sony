package com.aura.recipe.ui.screen.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun OnboardingScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🍳",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            Text(
                text = "Welcome to Recipe App",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Discover amazing recipes from around the world and save your favorites",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.4
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            Card(
                onClick = { 
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Get Started",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(20.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}