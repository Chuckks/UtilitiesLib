package com.bbva.utilitieslib.iso.data

import com.bbva.utilitieslib.iso.data.config.EFormat
import com.pagatodoholdings.utilitieslib.interfaces.IEmpty

private const val DEFAULT_SIZE = -1
private const val DEFAULT_NUMBER = -1
private const val DEFAULT_NAME = ""


class PackagerData(var name: String = DEFAULT_NAME, var number: Int = DEFAULT_NUMBER,
                    var format: EFormat = EFormat.UNKNOW, var size: Int = DEFAULT_SIZE): IEmpty {
    override fun isEmpty() = (name == DEFAULT_NAME && number == DEFAULT_NUMBER
            && size == DEFAULT_SIZE && format == EFormat.UNKNOW)


    companion object{
        const val MAXSIZE = 10000
        const val MANUMBER = 128
    }
}