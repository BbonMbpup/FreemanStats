package com.example.freemanstats.presentation

import java.util.UUID
import com.evrencoskun.tableview.sort.ISortableModel

class SortableCellModel(
    private val content: Any,
    private val id: String = UUID.randomUUID().toString()
) : ISortableModel {

    // Для сортировки
    override fun getContent(): Any = content
    override fun getId(): String = id

    // Вспомогательные методы
    fun getAsString(): String = content.toString()

    fun getAsInt(): Int = when (content) {
        is Int -> content
        is String -> content.toIntOrNull() ?: 0 // безопасное преобразование
        else -> 0
    }

    fun getAsDouble(): Double = when (content) {
        is Double -> content
        is String -> content.toDoubleOrNull() ?: 0.0 // безопасное преобразование
        else -> 0.0
    }

    fun getAsBoolean(): Boolean = when (content) {
        is Boolean -> content
        is String -> content.toBoolean()
        else -> false
    }
}