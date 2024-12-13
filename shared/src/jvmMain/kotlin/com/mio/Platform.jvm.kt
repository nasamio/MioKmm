package com.mio

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()


actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp){
    config(this)
}