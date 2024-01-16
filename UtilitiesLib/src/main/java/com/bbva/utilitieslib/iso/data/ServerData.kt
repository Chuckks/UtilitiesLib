package com.bbva.utilitieslib.iso.data

import com.pagatodoholdings.utilitieslib.interfaces.IEmpty

private const val DEFAULT_IP = "127.0.0.0"
private const val DEFAULT_PORT = 8080

data class ServerData(
    var ip: String = DEFAULT_IP,
        var port: Int = DEFAULT_PORT ): IEmpty {

    override fun isEmpty() = (ip == DEFAULT_IP && port == DEFAULT_PORT)

}