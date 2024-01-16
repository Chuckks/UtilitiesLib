package com.bbva.utilitieslib.iso.data.config

import com.pagatodoholdings.utilitieslib.interfaces.IEmpty

const val DEFAULT_VERSION = "1.0.0"
data class IsoField(
    val versión: String = DEFAULT_VERSION,
    val fields: List<Field> = listOf()
): IEmpty{

    override fun isEmpty() = (versión == DEFAULT_VERSION && fields.isEmpty())

    }
