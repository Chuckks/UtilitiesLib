package com.pagatodoholdings.utilitieslib.interfaces

interface IProcess {

    fun initialize(): Boolean
    fun prepare(): Boolean

    fun execute(): Boolean
    fun checkResponse(): Boolean

    fun processError(): Boolean
    fun processData(): Boolean

    fun saveData(): Boolean
    fun complete(): Boolean

    fun run(): Boolean {
        if (!initialize())
            return false

        if (!prepare())
            return false

        if (!execute())
            return false

        if (!checkResponse())
            return processError()

        if (!processData())
            return false

        if (!saveData())
            return false

        return complete()
    }
}
