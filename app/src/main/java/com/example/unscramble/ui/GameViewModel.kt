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
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


/**
 * ViewModel containing the app data and methods to process the data
 */
/*
class GameViewModel : ViewModel() {


    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String
    private var score: Int = 0

    init {
        resetGame()
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWord())
    }

    /*
     * Update the user's guess
     */
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    /*
     * Checks if the user's guess is correct.
     * Increases the score accordingly.
     */
    fun checkUserGuess() {
        val input = userGuess
        if (input.equals(currentWord, ignoreCase = true)) {
            score += currentWord.length
            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for the next round
            val updatedScore = _uiState.value.score + score
            updateGameState(updatedScore)
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        // Reset user guess
        updateUserGuess("")
    }

    /*
     * Skip to next word
     */
    fun skipWord() {
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }

    /*
     * Picks a new currentWord and currentScrambledWord and updates UiState according to
     * current game state.
     */
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS){
            //Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else{
            // Normal round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWord(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    private fun pickRandomWord(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        val scrambledWord = allWords.random()
        currentWord = getUnscrambledWord(scrambledWord)
        return if (usedWords.contains(currentWord)) {
            pickRandomWord()
        } else {
            usedWords.add(currentWord)
            currentWord
        }
    }

    private fun isInputValid(input: String): Boolean {
        return input.equals(currentWord, ignoreCase = true)
    }

}*/

/*class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String
    private var score: Int = 0

    init {
        resetGame()
    }



    /*
     * Re-initializes the game data to restart the game.
     */
    fun resetGame() {
        usedWords.clear()
        updateGameState()
    }

    /*
     * Update the user's guess
     */
    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    /*
     * Checks if the user's guess is correct.
     * Increases the score accordingly.
     */
    fun checkUserGuess() {
        val input = userGuess
        if (isInputValid(input)) {
            score += currentWord.length
            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for the next round
            updateGameState()
        } else {
            // User's guess is wrong, show an error
            _uiState.value = _uiState.value.copy(isGuessedWordWrong = true)
        }
        // Reset user guess
        updateUserGuess("")
    }

    /*
     * Skip to next word
     */
    fun skipWord() {
        updateGameState()
        // Reset user guess
        updateUserGuess("")
    }

    /*
     * Picks a new currentWord and currentScrambledWord and updates UiState according to
     * current game state.
     */
    private fun updateGameState() {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            // Last round in the game, update isGameOver to true, don't pick a new word
            _uiState.value = _uiState.value.copy(
                isGuessedWordWrong = false,
                score = score,
                isGameOver = true
            )
        } else {
            if (isInputValid(userGuess)) {
                // User's guess is correct, increase the score
                score += currentWord.length
            } else {
                // User's guess is wrong, show an error
                _uiState.value = _uiState.value.copy(isGuessedWordWrong = true)
            }
            // Reset user guess
            updateUserGuess("")

            // Normal round in the game
            currentWord = pickRandomWord()
            _uiState.value = _uiState.value.copy(
                isGuessedWordWrong = false,
                currentScrambledWord = scrambleWord(currentWord),
                currentWordCount = _uiState.value.currentWordCount.inc(),
                score = score
            )
        }
    }

    private fun pickRandomWord(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        val unusedWords = allWords - usedWords
        currentWord = unusedWords.random()
        usedWords.add(currentWord)
        return currentWord
    }

    private fun isInputValid(input: String): Boolean {
        return allWords.any { word -> input.equals(word, ignoreCase = true) }
    }

    private fun scrambleWord(word: String): String {
        val charArray = word.toCharArray()
        charArray.shuffle()
        return String(charArray)
    }
}*/

/*Game1*/
class GameViewModel : ViewModel() {

    // Game UI state
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    var userGuess by mutableStateOf("")
        private set

    // Set of words used in the game
    private var usedWords: MutableSet<String> = mutableSetOf()
    private lateinit var currentWord: String

    init {
        resetGame()
    }

    /*
     * Re-initializes the game data to restart the game.
     */
    fun resetGame() {
        usedWords.clear()
        val randomLetter = pickRandomLetter()
        currentWord = pickRandomWordStartingWith(randomLetter)
        _uiState.value = GameUiState(
            currentScrambledWord = scrambleWord(currentWord),
            currentWordCount = 1,
            score = 0,
            isGuessedWordWrong = false,
            isGameOver = false,
            currentLetter = randomLetter
        )
    }

    /*
     * Update the user's guess
     */
    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    /*
     * Checks if the user's guess is correct.
     * Increases the score accordingly.
     */
    fun checkUserGuess() {
        val input = userGuess
        if (isInputValid(input)) {
            val updatedScore = _uiState.value.score.plus( currentWord.length*100)
            // User's guess is correct, increase the score
            // and call updateGameState() to prepare the game for the next round
            updateGameState(updatedScore)
        } else {
            // User's guess is wrong, show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }        }
        // Reset user guess
        updateUserGuess("")
    }


    /*
     * Picks a new currentWord and currentScrambledWord and updates UiState according to
     * current game state.
     */
    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
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
                        isGuessedWordWrong = false,
                        currentScrambledWord = scrambleWord(currentWord),
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
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }

    private fun pickRandomLetter(): Char {
        val alphabet = ('A'..'Z').toList()
        return alphabet.random()
    }

    private fun pickRandomWordStartingWith(letter: Char): String {
        // Filter the unused words to find words starting with the current letter
        val unusedWordsStartingWithLetter = allWords
            .filter { word -> word.startsWith(letter, ignoreCase = true) }
            .minus(usedWords)

        // Pick a random word from the filtered list
        currentWord = unusedWordsStartingWithLetter.random()

        // Add the word to the used words set
        usedWords.add(currentWord)

        return currentWord
    }

    private fun isInputValid(input: String): Boolean {
        return allWords.any { word -> input.equals(word, ignoreCase = true) }
    }

    private fun scrambleWord(word: String): String {
        val charArray = word.toCharArray()
        charArray.shuffle()
        return String(charArray)
    }
}





