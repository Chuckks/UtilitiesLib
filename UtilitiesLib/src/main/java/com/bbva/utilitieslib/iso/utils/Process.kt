package com.bbva.utilitieslib.iso.utils

import android.util.Log

private val TAG = Process::class.java.simpleName
class Process: com.pagatodoholdings.utilitieslib.interfaces.IProcess {
    enum class EStep{
        STARTED,
        INITIALIZED,
        PREPARED,
        EXECUTED,
        CHECKED_RESPONSE,
        PROCESSED_ERROR,
        PROCESSED_DATA,
        SAVED,
        COMPLETED
    }

     var step = EStep.STARTED
        set(value) {
            Log.i(TAG, "STEP -> [${value.name}]")
            field = value
        }
    override fun initialize(): Boolean {
        step = EStep.INITIALIZED
        return true
    }

    override fun prepare(): Boolean {
        step = EStep.PREPARED
        return true
    }

    override fun execute(): Boolean {
        step = EStep.EXECUTED
        return true
    }

    override fun checkResponse(): Boolean {
        step = EStep.CHECKED_RESPONSE
        return true
    }

    override fun processData(): Boolean {
        step = EStep.PROCESSED_DATA
        return true
    }

    override fun saveData(): Boolean {
        step = EStep.SAVED
        return true
    }

    override fun complete(): Boolean {
        step = EStep.COMPLETED
        return true
    }

    override fun processError(): Boolean {
        step = EStep.PROCESSED_ERROR
        return true
    }

    override fun run(): Boolean {
        if( !initialize( ) )
            return false

        if( !prepare( ) )
            return false

        if( !execute( ) )
            return false

        if( !checkResponse( ) ){
            processError( )
            return false
        }

        if( !processData( ) )
            return false

        if( !saveData( ) )
            return false

        return complete( )
    }
}
