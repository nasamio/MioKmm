package com.mio

import android.os.Build
import io.ktor.client.*
import io.ktor.client.engine.android.*

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Android) {
    config(this)
}


actual fun getRequestHost(): String = "http://192.168.3.10"

actual fun getRequestPort(): Int = 8080