package com.pagatodoholdings.utilitieslib.interfaces

interface IEmpty {

    fun isEmpty(): Boolean
    fun isNotEmpty() = !isEmpty()
}
