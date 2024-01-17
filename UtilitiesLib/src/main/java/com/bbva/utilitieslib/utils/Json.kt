package com.bbva.utilitieslib.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object Json {

    inline fun <reified E> toList(readText: String): List<E> =
        Gson().fromJson(readText, object: TypeToken<List<E>>() {}.type) ?: listOf()

    inline fun <reified E> toJson(list: List<E>) =
        Gson().toJson(list, object: TypeToken<List<E>>() {}.type) ?: ""

    inline fun <reified E> fromJson(readText: String) =
        Gson().fromJson(readText, E::class.java) ?: ""

    fun <T> fromJson(readText: String, value: Class<T>) =
        Gson().fromJson(readText, value)

    inline fun <reified E> toString(data: E) =
        Gson().toJson(data, E::class.java) ?: ""
}
