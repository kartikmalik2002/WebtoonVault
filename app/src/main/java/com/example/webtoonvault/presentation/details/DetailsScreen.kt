package com.example.webtoonvault.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.webtoonvault.data.models.Webtoon
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebtoonDetailScreen(
    webtoon: Webtoon?,
    navigateUp: () -> Unit,
    onSubmitRating: (Int) -> Unit,
    onFavoriteClick: (String, Boolean) -> Unit,
    isFavorite: Boolean
) {

    val viewModel: DetailsViewModel = viewModel(modelClass = DetailsViewModel::class)

    //remember the state of the BottomSheetScaffold
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = false
        )
    )
    val coroutineScope = rememberCoroutineScope()

    // State to store selected rating
    var selectedRating by remember { mutableStateOf(0) }

    var favoriteState by remember { mutableStateOf(isFavorite) }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            // Content for Rating Bottom Sheet
            RatingBottomSheet(
                selectedRating = selectedRating,
                onRatingSelected = { rating -> selectedRating = rating },
                onSubmit = {
                    coroutineScope.launch {
                        bottomSheetScaffoldState.bottomSheetState.hide()
                        onSubmitRating(selectedRating)

                    }
                }
            )
        },
        sheetPeekHeight = 0.dp
    ){
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = {
                            navigateUp()
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                onFavoriteClick(webtoon!!.id, !isFavorite)
                                //favoriteState = !isFavorite

                            }
                        ) {
                            if (isFavorite)
                                Icon(Icons.Default.Favorite, contentDescription = "Favourite")
                            else
                                Icon(Icons.Default.FavoriteBorder, contentDescription = "UnFavourite")
                        }
                    },
                    colors = TopAppBarColors(
                        actionIconContentColor = Color.Gray,
                        containerColor = Color.Black,
                        navigationIconContentColor = Color.White,
                        scrolledContainerColor = Color.Transparent,
                        titleContentColor = Color.Gray
                    )


                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(Color.Black)
            ) {
                // Webtoon Image
                val context = LocalContext.current
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(webtoon?.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(450.dp)
                            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    )
                    Box(

                        modifier = Modifier
                            .fillMaxWidth()
                            .size(450.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black
                                    ),
                                    startY = 300f
                                )
                            )
                    )
                    // Title
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = webtoon!!.title,
                            style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = Color.Yellow
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${webtoon.averageRating}/5 (${webtoon.ratingCount} ratings)",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                // Overview and "Give Rating" Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Let's Dive In",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color.White
                    )
                    TextButton(onClick = {
                        coroutineScope.launch {
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                    )
                        {
                        Text(text = "Give Rating", color = Color.Yellow)
                    }
                    }

                            Spacer (modifier = Modifier.height(8.dp))

                            // Webtoon Overview Content
                            Column (modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text(
                            text = webtoon!!.content,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Light,
                                lineHeight = 28.sp
                            ),
                            color = Color.White
                        )
                    }

                            Spacer (modifier = Modifier.height(16.dp))

                }
            }
        }
}

@Composable
fun RatingBottomSheet(
    selectedRating: Int,
    onRatingSelected: (Int) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rate this Mahnwa",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Yellow
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Rating Stars
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= selectedRating) Icons.Default.Star else Icons.Default.StarBorder,
                    contentDescription = "Star $i",
                    tint = Color.Yellow,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable {
                            onRatingSelected(i)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit Button
        Button(onClick = onSubmit) {
            Text(text = "Submit")
        }
    }
}
