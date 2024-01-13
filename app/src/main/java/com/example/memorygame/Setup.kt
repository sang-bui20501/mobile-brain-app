package com.example.memorygame

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.memorygame.ui.memory.NewImageScreen
import com.example.memorygame.ui.memory.RememberColorScreen
import com.example.memorygame.ui.memory.RememberImageScreen
import com.example.unscramble.ui.GameScreen
import com.example.unscramble.ui.GameScreen2
import com.example.unscramble.ui.GameScreen3

@Composable
fun SetupMemory(
    navController: NavHostController
){
    NavHost(
        navController = navController,
        startDestination = Screen.MemoryScreen.route
    ){
        composable(
            route = Screen.MemoryScreen.route
        ){
            MemoryApp(navController = navController)
        }
        composable(
            route = Screen.RememberColor.route
        ){
            RememberColorScreen(navController = navController)
        }
        composable(
            route = Screen.NewImage.route
        ){
            NewImageScreen(navController = navController)
        }
        composable(
            route = Screen.RememberImage.route
        ){
            RememberImageScreen(navController = navController)
        }

        composable(
            route = Screen.GameScreen.route
        ){
            GameScreen()
        }
        composable(
            route = Screen.GameScreen2.route
        ){
            GameScreen2()
        }
        composable(
            route = Screen.GameScreen3.route
        ){
            GameScreen3()
        }
    }
}