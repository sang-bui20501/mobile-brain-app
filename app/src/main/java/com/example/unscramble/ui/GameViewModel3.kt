/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NUM_WORDS
import com.example.unscramble.data.SCORE_INC
import com.example.unscramble.data.allWordsList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel containing the app data and methods to process the data
 */
class GameViewModel3 : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String
    var questionsAsked = 0
    init {
        resetGame()
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(
            currentScrambledWord = pickRandomWord(),
            currentWordCount = 1,
            score = 0,
            isGuessedWordWrong = false,
            isGameOver = false,
        )
    }

    /*
     * Update the user's guess
     */
    fun updateUserGuess(guessedWord: String) {
        questionsAsked++
        userGuess = guessedWord
    }

    /*
     * Checks if the user's guess is correct.
     * Increases the score accordingly.
     */
    fun checkUserGuess() {
        val input = userGuess
        if (_uiState.value.isGameOver) {
            return
        }
        if (isInputValid(input)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INC)
            updateGameState(updatedScore)
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        // Reset user guess
        updateUserGuess("")
    }


    /*
     * Picks a new currentWord and currentScrambledWord and updates UiState according to
     * current game state.
     */
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NUM_WORDS || questionsAsked >= 10) {
            // Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else {
            _uiState.update { currentState ->
                currentState.copy(
                    isGameOver = false,
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWord(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }
    /*
    * Skip to the next word
    */
    fun skipWord() {
        if (_uiState.value.isGameOver) {
            return
        }
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }



    private fun pickRandomWord(): String {
        // Filter the unused words to find words starting with the current letter
        val unusedWords = allWordsList.minus(usedWords)

        if (unusedWords.isEmpty() == true || _uiState.value.isGameOver == true) {
            _uiState.update { currentState ->
                currentState.copy(isGameOver = true)
            }
        }

        // Pick a random word from the filtered list
        currentWord = unusedWords.random()

        // Add the word to the used words set
        usedWords.add(currentWord)

        // Display only the first half of the word
        val spaceIndex = currentWord.indexOf(" ")
        val displayedWord = if (spaceIndex != -1) {
            currentWord.substring(0, spaceIndex)
        } else {
            currentWord.substring(0, currentWord.length / 2)
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentScrambledWord = displayedWord,
                currentWordCount = currentState.currentWordCount.inc()
            )
        }

        return displayedWord
    }


    private fun isInputValid(input: String): Boolean {
        return allWordsList.any { word -> input.equals(word, ignoreCase = true) }
    }
}
