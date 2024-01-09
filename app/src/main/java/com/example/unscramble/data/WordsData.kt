package com.example.unscramble.data

const val MAX_NO_OF_WORDS = 10

// List with all the words for the Game
    val allWords: Set<String> = setOf(
        "apple",
        "banana",
        "carrot",
        "dog",
        "elephant",
        "fish",
        "grape",
        "horse",
        "ice cream",
        "jellyfish",
        "kiwi",
        "lemon",
        "melon",
        "nut",
        "orange",
        "pear",
        "quail",
        "rabbit",
        "strawberry",
        "tomato",
        "umbrella",
        "vanilla",
        "watermelon",
        "xylophone",
        "yogurt",
        "zebra",
    )


/**
 * Maps words to their lengths. Each word in allWords has a unique length. This is required since
 * the words are randomly picked inside GameViewModel and the selection is unpredictable.
 */
private val wordLengthMap: Map<Int, String> = allWords.associateBy({ it.length }, { it })

internal fun getUnscrambledWord(scrambledWord: String) = wordLengthMap[scrambledWord.length] ?: ""