package com.bbva.utilitieslib.extensions

fun Int.toBoolean() = when (this) {
    1    -> true
    0    -> false
    else -> throw IllegalArgumentException("To Boolean Fail")
}

fun Int.isOdd(): Boolean{
    return (this and 0x01).toBoolean()
}