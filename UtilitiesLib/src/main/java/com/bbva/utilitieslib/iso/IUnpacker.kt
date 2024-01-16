package com.bbva.utilitieslib.iso

interface IUnpacker<U, S> {
    fun getCounter(): Int
     fun specificSearch(index: Int, value: S ): Boolean
    fun specificUnpacker(index: Int, file: String ): U
}