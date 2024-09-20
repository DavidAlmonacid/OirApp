package com.example.oirapp.utils

fun String.removeUppercaseAccents(): String {
    val accents = mapOf('Á' to 'A', 'É' to 'E', 'Í' to 'I', 'Ó' to 'O', 'Ú' to 'U')
    return this.map { accents[it] ?: it }.joinToString("")
}
