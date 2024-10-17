package com.example.webtoonvault.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.webtoonvault.data.models.Webtoon
import com.example.webtoonvault.presentation.common.WebtoonCard

@Composable
fun HomeScreen(
    webtoons: List<Webtoon>,
    onCardClick: (String) -> Unit = {},
    onFavouriteClick: () -> Unit = {},
    userName: String?
) {


    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {
        Spacer(modifier = Modifier.height(32.dp))
        // Top Section: Greeting and Profile
        TopSection (onFavouriteClick = onFavouriteClick, userName = userName)

        // Title Section
        Text(
            text = "Popular Manhwas",
            style = MaterialTheme.typography.titleLarge.copy(
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        // Grid of Webtoon Cards
        LazyColumn(
            //columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {

            items(webtoons.size) { index ->
                val webtoon = webtoons[index]
                WebtoonCard(webtoon = webtoon, onCardClick = {
                    onCardClick(webtoon.id)
                })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSection(onFavouriteClick: () -> Unit, userName: String?) {

    Row(
        modifier = Modifier

            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Greeting Text
        Text(
            text = "Hi $userName",
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )

        IconButton(
            onClick = {
                onFavouriteClick()
            },
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.Transparent)
        ){
            Icon(Icons.Default.Favorite, contentDescription = null,
                 tint = Color.White,
                 modifier = Modifier
                     .size(24.dp),

            )
        }
    }
}

