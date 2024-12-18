package com.mio

import io.ktor.client.*
import io.ktor.client.engine.js.*

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Js) {
    config(this)
}

actual fun getRequestHost(): String = "http://192.168.3.10"

actual fun getRequestPort(): Int = 8080
actual fun choosePic(onRes: (String) -> Unit) {
}