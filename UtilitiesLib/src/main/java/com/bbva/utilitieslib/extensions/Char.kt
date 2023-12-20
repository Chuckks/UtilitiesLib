package com.bbva.utilitieslib.extensions

fun Char.isHexa(): Boolean {
    if (this in 'a'..'f')
        return true

    if (this in 'A'..'F')
        return true

    return this in '0'..'9'
}

fun Char.checkHexa(): Char {
    if (!isHexa())
        throw IllegalArgumentException("Ilegal Value [$this]")

    return this
}
fun Char.toHexa() = Character.digit(Character.toLowerCase(checkHexa()), 16)