package com.bbva.utilitieslib.extensions

fun List<Char>.random(length: Int) : String = (1..length).map { this.random()}.joinToString("")