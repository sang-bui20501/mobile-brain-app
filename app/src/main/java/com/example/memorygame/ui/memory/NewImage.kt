package com.example.memorygame.ui.memory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.memorygame.R
import com.example.memorygame.Screen
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewImageScreen(
    navController: NavController
) {
    var selectedImage by remember { mutableStateOf<ImageItem?>(null) }
    var previousImage by remember { mutableStateOf<List<ImageItem>>(emptyList()) }
    var indexTest by remember { mutableStateOf(0) }
    var images by remember { mutableStateOf<List<ImageItem>>(emptyList()) }
    var selectedOne by remember { mutableStateOf(false) }
    var live by remember { mutableStateOf(1) }
    var nextLive by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var isFailed by remember { mutableStateOf(false) }
    var level by remember { mutableStateOf(1) }
    val maxLevel = 7
    var gameCompleted by remember { mutableStateOf(false) }


    val drawableImages = listOf<ImageItem>(
        ImageItem(1, R.drawable.image1),
        ImageItem(2, R.drawable.image2),
        ImageItem(3, R.drawable.image3),
        ImageItem(4, R.drawable.image4),
        ImageItem(5, R.drawable.image5),
        ImageItem(6, R.drawable.image6),
        ImageItem(7, R.drawable.image7),
        ImageItem(8, R.drawable.image8),
        ImageItem(9, R.drawable.image9),
        ImageItem(10, R.drawable.image10)
    )

    LaunchedEffect(selectedOne) {
        if (selectedOne == true) {
            score += 100
        }
        delay(3000)
        selectedOne = false

    }

    LaunchedEffect(selectedImage) {
        if (images.isNotEmpty()) {
            delay(3000)
            images = drawableImages.filter { it != selectedImage && it !in previousImage }.shuffled().take(3).toMutableList()
            images += previousImage
            images = images.shuffled()
            indexTest++
            selectedOne = false
        }
        if (images.isEmpty()) {
            images = drawableImages.filter { it != selectedImage && it !in previousImage }.shuffled().take(3).toMutableList()

            images += previousImage
            images = images.shuffled()
            indexTest++
            selectedOne = false
        }
    }


    if (gameCompleted) {
        AlertDialog(
            onDismissRequest = {
            },
            title = { Text(text = "Chúc mừng!") },
            text = { Text(text = "Bạn đã hoàn thành tất cả các lượt chơi") },
            confirmButton = {
                Button(
                    onClick = {
                        navController.navigate(Screen.MemoryScreen.route)

                    }
                ) {
                    Text("Quay lại màn hình chính")
                }
            }
        )
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tìm hình mới") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Menu",
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable {
                                navController.navigate(Screen.MemoryScreen.route)
                            }
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = "Guidelines",
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable { /* Handle guidelines click */ }
                    )

                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color(0xFFDCF2F1),
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
                    .background(Color(0xFFFAE7F3)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "Lượt chơi: $live/2",
                    modifier = Modifier.padding(vertical = 8.dp),
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    contentPadding = paddingValues
                ) {
                    items(images.size) { index ->
                        var image = images[index]!!
                        var isSelected by remember { mutableStateOf(false) }

                        Card(
                            modifier = Modifier
                                .padding(5.dp)
                                .size(125.dp)
                                .clickable {
                                    isSelected = !isSelected
                                    if (isSelected) {
                                        selectedImage = image
                                        selectedOne = true
                                        level++
                                    }
                                    previousImage += image
                                },
                                border = BorderStroke(
                                    2.dp,
                                    if (isSelected) Color.Blue else Color.Transparent
                                )
                        ) {
                            Image(
                                painter = painterResource(id = image.imageRes),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        if (isSelected) {
                            LaunchedEffect(Unit) {
                                delay(3000)
                                isSelected = false
                            }
                        }
                    }
                }
            }
            if (selectedOne == true && selectedImage in previousImage.dropLast(1) && selectedImage != null) {
                previousImage = emptyList()
                selectedOne = false
                if (live >= 2) {
                    gameCompleted = true
                }else nextLive = true

                if (nextLive) {
                    live++
                    level = 1

                    previousImage = emptyList()
                    images = emptyList()
                    selectedImage = null
                    selectedOne = false

                    isFailed = false
                    nextLive = false
                }
            }
            if (selectedOne == true && selectedImage != null) {
                TickEffect()
                if (level > maxLevel) {
                    gameCompleted = true
                }
                selectedOne == false
            }

        }
    }
}

@Composable
private fun TickEffect() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray.copy(alpha = 0.5f))
            .pointerInput(Unit) { detectTapGestures {} },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Tick Symbol",
            modifier = Modifier
                .background(Color.Green, MaterialTheme.shapes.extraLarge)
                .size(48.dp)

        )
    }
}

data class ImageItem(val id: Int, val imageRes: Int)
@Preview
@Composable
fun ColorMemoryPreview() {
    NewImageScreen(
        navController = rememberNavController()
    )
}