package com.example.memorygame.ui.memory


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import kotlinx.coroutines.delay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.memorygame.R
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.memorygame.Screen
import kotlinx.coroutines.launch
import java.lang.Integer.max

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RememberColorScreen(
    navController: NavController
){
    var gameCompleted by remember { mutableStateOf(false) }
    var score by remember { mutableStateOf(0) }
    var level by remember { mutableStateOf(1) }
    var round by remember { mutableStateOf(1) }
    var size by remember { mutableStateOf(1) }
    var gridSize by remember { mutableStateOf(1) }
    var blueSquares by remember { mutableStateOf<List<Boolean>>(emptyList()) }
    var saveBlueSquares by remember { mutableStateOf<List<Boolean>>(emptyList()) }
    var enableClick by remember { mutableStateOf(false) }
    var falseClick by remember { mutableStateOf<List<Int>>(emptyList()) }
    var correctCount by remember { mutableStateOf(0) }
    var falseCount by remember { mutableStateOf(0) }
    var tiles by remember { mutableStateOf(0) }
    var gameOver by remember { mutableStateOf(false) }
    var gameStart by remember { mutableStateOf(false) }
    var showTickEffect by remember { mutableStateOf(false) }

    val levelList = listOf(
        LevelInfo(1, 1, Pair(2, 2)),
        LevelInfo(2, 2, Pair(3, 3)),
        LevelInfo(3, 3, Pair(3, 4)),
        LevelInfo(4, 4, Pair(4, 4)),
        LevelInfo(5, 5, Pair(4, 5)),
        LevelInfo(6, 6, Pair(4, 5)),
        LevelInfo(7, 7, Pair(5, 5)),
        LevelInfo(8, 8, Pair(5, 6)),
        LevelInfo(9, 9, Pair(5, 6)),
        LevelInfo(10, 10, Pair(6, 6))
    )

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
    LaunchedEffect(round) {
        val currentLevelInfo = levelList.find { it.level == level }

        currentLevelInfo?.let { info ->
            val (x, y) = info.grid
            size = x * y
            gridSize = x

            tiles = info.tiles
            val indices = (0 until size).toList().shuffled().take(info.tiles)
            blueSquares = List(size) { index -> indices.contains(index) }

            delay(5000)
            saveBlueSquares = blueSquares
            blueSquares = List(size) {false}
            enableClick = true


        }
        if (round > 10) {
            gameCompleted = true
        }
    }

    LaunchedEffect(gameStart){
        if (gameStart){
            delay(10000)
            gameOver = true
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Ghi nhớ màu") },
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
                Row(modifier = Modifier
                    .padding(10.dp)
                    .size(200.dp, 50.dp)
                    .background(color = Color.White, shape = MaterialTheme.shapes.extraLarge)
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.score),
                        contentDescription = "Score",
                        modifier = Modifier
                            .size(50.dp)
                            .padding(5.dp)
                    )
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Điểm: $score",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = contentColorFor(Color.White),
                            fontSize = 25.sp
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {


                    LazyVerticalGrid(
                        columns = GridCells.Fixed(gridSize),
                        contentPadding = paddingValues,
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(size) {index ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .aspectRatio(1f)
                                    .padding(5.dp)
                                    .shadow(
                                        elevation = 15.dp,
                                        shape = MaterialTheme.shapes.extraSmall
                                    )
                                    .background(
                                        when {
                                            blueSquares.getOrNull(index) == true -> Color.Blue
                                            falseClick.contains(index) -> Color.Red
                                            else -> Color.White
                                        }
                                    )
                                    .clickable {
                                        if(enableClick){
                                            gameStart = true
                                            if(saveBlueSquares.getOrNull(index) == true && blueSquares.getOrNull(index) == false){
                                                blueSquares = blueSquares.toMutableList().apply {
                                                    set(index, !this[index])
                                                }
                                                correctCount++

                                                if (correctCount == tiles) {
                                                    enableClick = false
                                                    score += 100
                                                    CoroutineScope(Dispatchers.Main).launch {
                                                        showTickEffect = true
                                                        delay(2000)
                                                        showTickEffect = false
                                                        blueSquares = emptyList()
                                                        falseClick = emptyList()
                                                        saveBlueSquares = emptyList()
                                                        correctCount = 0
                                                        falseCount = 0
                                                        level++
                                                        round++
                                                    }
                                                }
                                            }else {
                                                falseClick = falseClick.toMutableList().apply {
                                                    add(index)
                                                }
                                                falseCount++
                                                if (falseCount == 1 && tiles == 1) {
                                                    gameOver = true
                                                } else if (falseCount >= 2 && tiles >= 2) {
                                                    gameOver = true
                                                }
                                                if(gameOver){
                                                    correctCount = 0
                                                    falseCount = 0
                                                    enableClick = false
                                                    CoroutineScope(Dispatchers.Main).launch {
                                                        blueSquares = saveBlueSquares
                                                        delay(3000)
                                                        blueSquares = emptyList()
                                                        saveBlueSquares = emptyList()
                                                        falseClick = emptyList()
                                                        gameOver = false
                                                        level = max(1, level - 1)
                                                        round++
                                                    }
                                                }
                                            }



                                        }

                                    }
                            ) {

                            }

                        }

                    }


                }
            }
        }
        if (showTickEffect) {
            TickEffect()
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

data class LevelInfo(val level: Int, val tiles: Int, val grid: Pair<Int, Int>)

@Preview
@Composable
fun RememberColorScreenPreview(){
    RememberColorScreen(
        navController = rememberNavController()
    )
}