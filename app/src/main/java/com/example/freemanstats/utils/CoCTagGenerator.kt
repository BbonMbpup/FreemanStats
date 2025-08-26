package com.example.freemanstats.utils

import kotlin.random.Random

object CoCTagGenerator {

    // Разрешённые символы в тэге (официальный алфавит CoC)
    private const val ALPHABET = "0289PYLQGRJCUV"

    /**
     * Генерирует один тэг.
     *
     * @param length длина без учёта '#'. Обычно 9, но у CoC встречаются 7–10.
     * @param withHash добавлять ли префикс '#'
     * @param random источник случайности (для тестов можно подставить фиксированный seed)
     */
    fun generate(
        length: Int = 8,
        withHash: Boolean = true,
        random: Random = Random.Default
    ): String {
        require(length in 7..10) { "Длина тега должна быть в диапазоне 7..10" }
        val core = buildString(length) {
            repeat(length) {
                append(ALPHABET[random.nextInt(ALPHABET.length)])
            }
        }
        return if (withHash) "#$core" else core
    }

    /**
     * Генерирует список тэгов.
     */
    fun generateBatch(
        count: Int,
        length: Int = 9,
        withHash: Boolean = true,
        random: Random = Random.Default
    ): List<String> {
        require(count > 0) { "count должен быть > 0" }
        return List(count) { generate(length, withHash, random) }
    }

    /**
     * Проверяет, что строка является валидным CoC-тэгом.
     * Принимает варианты как с '#', так и без.
     */
    fun isValid(tag: String): Boolean {
        val normalized = tag.trim().uppercase()
        val regex = Regex("^#?[${ALPHABET}]{7,10}$")
        return regex.matches(normalized)
    }
}