package com.example.memorygame


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlin.math.absoluteValue


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MemoryApp(
    navController: NavController
){
    val state = rememberPagerState(0)

    val slider = listOf(
        SlideData(R.drawable.screen1, "Ghi nhớ màu", "Cố gắng nhớ những ô màu", Screen.RememberColor.route),
        SlideData(R.drawable.screen2, "Tìm hình mới", "Tìm những hình chưa xuất hiện", Screen.NewImage.route),
        SlideData(R.drawable.screen3, "Đó là hình nào", "Cố gắng ghi nhớ những hình đ ...", Screen.RememberImage.route),
        SlideData(R.drawable.screen4, "Thêm chữ cái", "Từ một chữ cho trước, hãy bổ sung...", Screen.GameScreen.route),
        SlideData(R.drawable.screen5, "Sắp xếp chữ", "Sắp xếp từ lộn xộn...", Screen.GameScreen2.route),
        SlideData(R.drawable.screen5, "Nối từ", "Từ một từ cho trước, hãy bổ sung...", Screen.GameScreen3.route),
        )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAE7F3)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "CHỌN TRÒ CHƠI",
            fontSize = 28.sp
        )
        Spacer(modifier = Modifier.height(62.dp))
        HorizontalPager(
            pageCount = slider.size,
            state = state,
            contentPadding = PaddingValues(horizontal = 100.dp),
            modifier = Modifier
                .height(350.dp)
        ){page ->
            val (imageRes, title, description, route) = slider[page]
            val onClick = {
                navController.navigate(route)
            }
            Card(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(
                    5.dp, Color.White
                ),
                modifier = Modifier
                    .graphicsLayer{
                        val pageOffset = (state.currentPage - page).toFloat().absoluteValue
                        lerp(
                            start = 0.50f,
                            stop = 1f,
                            fraction = 1f- pageOffset.coerceIn(0f, 1f)
                        )
                            .also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }
                        alpha = lerp(
                            start = 0.5f,
                            stop =1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .clickable(onClick = onClick)
            ){
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(imageRes),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(0.8f)
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier
                            .weight(0.2f)
                            .padding(10.dp)
                    ){
                        Image(
                            painter = painterResource(imageRes),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .weight(0.3f)
                                .fillMaxWidth(0.2f)
                        )

                        Column(
                            modifier = Modifier
                                .weight(0.7f)
                                .padding(start = 8.dp)
                        ) {
                            Text(
                                title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                description,
                                fontSize = 8.sp
                            )
                        }
                    }
                }
            }
        }

    }
}

data class SlideData(
    val imageRes: Int,
    val title: String,
    val description: String,
    val route: String
)

@Preview(showBackground = true)
@Composable
fun MemoryAppPreview(){
    MemoryApp(
        navController = rememberNavController()
    )
}

