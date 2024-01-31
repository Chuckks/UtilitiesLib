package com.bbva.utilitieslib.interfaces

interface IEmpty {

    fun isEmpty(): Boolean
    fun isNotEmpty() = !isEmpty()
}
