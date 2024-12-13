package com.mio

import io.ktor.client.*

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


// 初始化ktor client
expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient