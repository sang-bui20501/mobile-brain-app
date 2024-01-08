package com.example.memorygame

sealed class Screen(val route: String) {
    object MemoryScreen : Screen("memory_game")
    object RememberColor : Screen("memory_game_color")
    object RememberImage : Screen("memory_game_image")
    object NewImage : Screen("memory_game_new")
}