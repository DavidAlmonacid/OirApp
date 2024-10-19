package com.example.oirapp.utils

fun String.removeAccents(): String {
    val accents = mapOf(
        'Á' to 'A', 'É' to 'E', 'Í' to 'I', 'Ó' to 'O', 'Ú' to 'U',
        'á' to 'a', 'é' to 'e', 'í' to 'i', 'ó' to 'o', 'ú' to 'u',
    )

    return this.map { accents[it] ?: it }.joinToString("")
}
