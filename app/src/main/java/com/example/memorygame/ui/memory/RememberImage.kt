package com.example.memorygame.ui.memory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.memorygame.R
import com.example.memorygame.Screen
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.AlertDialog


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberImageScreen() {
    var score by remember { mutableStateOf(0) }
    var showTickEffect by remember { mutableStateOf(false) }
    var level by remember { mutableStateOf(1) }
    var currentRound by remember { mutableStateOf(1) }
    var isTimeUp by remember { mutableStateOf(false) }
    var selectedImage by remember { mutableStateOf(-1) }
    var selectedImage2 by remember { mutableStateOf(-1) }
    var images by remember { mutableStateOf<List<Int>>(emptyList()) }
    var mixedImages by remember { mutableStateOf<List<Int>>(emptyList()) }
    var selectedImages by remember { mutableStateOf<List<Int>>(emptyList()) }
    var scoreIncrease by remember { mutableStateOf(false) }
    var gameCompleted by remember { mutableStateOf(false) }
    var autoClickEnabled by remember { mutableStateOf(false) }

    fun onCheckSelectedImages(){
        if (currentRound in 6..10) {
            if (selectedImages.size == 2) {
                if (selectedImages.contains(selectedImage) && selectedImages.contains(
                        selectedImage2
                    )
                ) {
                    score += 100
                    scoreIncrease = true
                }
            }
        } else {
            if (selectedImages.size == 1) {
                if (selectedImages.count { it == selectedImage } == 1) {
                    score += 100
                    scoreIncrease = true

                }
            }
        }
        currentRound++
        level = currentRound
        CoroutineScope(Dispatchers.Main).launch {
            if (currentRound == 11) {
                gameCompleted = true
            }
            if (scoreIncrease) {
                showTickEffect = true
            }
            delay(2000) // Adjust the delay time as needed
            showTickEffect = false
            isTimeUp = false
            scoreIncrease = false
            selectedImage = -1
            selectedImage2 = -1
            selectedImages = emptyList()
        }
    }

    LaunchedEffect(isTimeUp) {
        val drawableImages = listOf(
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,
            R.drawable.image6,
            R.drawable.image7,
            R.drawable.image8,
            R.drawable.image9,
            R.drawable.image10
        )
        val imageCount = when (currentRound) {
            in 1..3 -> 4
            in 4..7 -> 5
            else -> 6
        }

        if (!isTimeUp) {
            images = drawableImages.shuffled().take(4)
            selectedImage = when {
                currentRound in 6..10 -> {
                    val shuffledImages = images.shuffled()
                    val firstImage = shuffledImages[0]
                    val secondImage = shuffledImages.firstOrNull { it != firstImage } ?: firstImage
                    selectedImage2 = secondImage
                    firstImage
                }
                else -> images.random()
            }
            delay(10000)
            isTimeUp = true
        } else {
            val remainingForSelection = drawableImages.filterNot { it in images }
            mixedImages = if (currentRound in 6..10) {
                (remainingForSelection.shuffled().take(imageCount - 2) + selectedImage + selectedImage2).shuffled()
            } else {
                (remainingForSelection.shuffled().take(imageCount - 1) + selectedImage).shuffled()
            }
        }
        if (isTimeUp && !autoClickEnabled) {
            autoClickEnabled = true
            delay(1000)
            if (isTimeUp && autoClickEnabled) {
                autoClickEnabled = false
                onCheckSelectedImages()
            }
        }
    }
    if (gameCompleted) {
        AlertDialog(
            onDismissRequest = {
                // Quay lại màn hình trước đó hoặc thoát ứng dụng
            },
            title = { Text(text = "Congratulations!") },
            text = { Text(text = "You've completed all levels!") },
            confirmButton = {
                Button(
                    onClick = {
                        // Quay lại màn hình trước đó hoặc thoát ứng dụng
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }
//
//    LaunchedEffect(score) {
//        showTickEffect = true
//        delay(2000) // Time to display the tick effect, e.g., 1 second
//        showTickEffect = false
//    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Remember Image") },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Menu",
                        modifier = Modifier
                            .padding(12.dp)
                            .clickable { /* Handle menu click */ }
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
                    containerColor = Color(204, 255, 255),
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    // Show score and level
                    Text("Score: $score")
                    Spacer(modifier = Modifier.width(28.dp))
                    Text("Level: $level")

                }
                // Show images for memorization
                Text("Remember these images:")
                ImageGrid(
                    images = images,
                    isTimeUp = isTimeUp,
                    selectedImage = selectedImage,
                    selectedImage2 = selectedImage2,
                    displayedImage = if (!isTimeUp) selectedImage else R.drawable.packet, // Change this to the replacement image
                    onImageSelected = { index ->
                        selectedImage = images[index]
                    }
                )
                Spacer(modifier = Modifier.height(20.dp))

                if (isTimeUp) {
                    // Show mixed images for selection
                    Text("Select the images you saw:")
                    if (selectedImage != -1) {
                        // Display mixed images after time's up
                        val shuffledIndices = mixedImages.indices.toList().shuffled()
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(4),
                            contentPadding = paddingValues
                        ) {
                            items(shuffledIndices.size) { index ->
                                val image = mixedImages[shuffledIndices[index]]
                                val isSelectedImage = selectedImages.contains(image)


                                Card(
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .size(100.dp)
                                        .clickable {
                                            if (isSelectedImage) {
                                                selectedImages = selectedImages - image
                                            } else {
                                                selectedImages = selectedImages + image
                                            }
                                        },
                                    border = BorderStroke(
                                        2.dp,
                                        if (isSelectedImage) Color.Blue else Color.Transparent
                                    )
                                ) {
                                    Image(
                                        painter = painterResource(id = image),
                                        contentDescription = null,
                                        modifier = Modifier.size(100.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(50.dp))
                        // Button to check selected images
                        Button(
                            onClick = {
                                onCheckSelectedImages()
                                autoClickEnabled = false

                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Check Selected Images")
                        }
                    }
                }

            }
            if (showTickEffect) {
                TickEffect()
            }
        }
    }
}

@Composable
fun ImageGrid(
    images: List<Int>,
    isTimeUp: Boolean,
    selectedImage: Int,
    selectedImage2: Int,
    displayedImage: Int,
    onImageSelected: (Int) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4)
    ) {
        items(images) { image ->
            Card(
                modifier = Modifier
                    .padding(2.dp)
                    .size(100.dp)
                    .clickable { onImageSelected(image) }
            ) {
                val painter = if (isTimeUp && (image == selectedImage || image == selectedImage2)) {
                    painterResource(id = displayedImage) // Use displayedImage if isTimeUp and image is selectedImage or selectedImage2
                } else {
                    painterResource(id = image)
                }

                // Your image content inside the Card
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
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
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
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

@Preview
@Composable
fun RememberImageScreenPreview(
) {
    RememberImageScreen()
}