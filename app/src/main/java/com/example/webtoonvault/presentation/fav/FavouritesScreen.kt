package com.example.webtoonvault.presentation.fav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.webtoonvault.data.models.Webtoon
import com.example.webtoonvault.presentation.common.WebtoonCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    onCardClick: (String) -> Unit = {},
    webtoons: List<Webtoon>,
    navigateUp: () -> Unit
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)) {

        TopAppBar(
            title = { },
            modifier = Modifier.fillMaxWidth(),
            colors = TopAppBarDefaults.mediumTopAppBarColors(
                actionIconContentColor = Color.Gray,
                containerColor = Color.Black,
                navigationIconContentColor = Color.White,
                scrolledContainerColor = Color.Transparent,
                titleContentColor = Color.Gray
            ),
            navigationIcon = {
                IconButton(onClick = {  navigateUp()}) {
                     Icon(Icons.Default.ArrowBack , contentDescription = null)
                }
            },

        )

        // Title Section
        Text(
            text = "Your Favourites",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(16.dp)
        )

        if (webtoons.isEmpty()){
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center

            ){
                Text(
                    text = "No Favourites",
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 24.sp
                    )
                )
            }
        }
        else{
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
        // Grid of Webtoon Cards
    }
}



