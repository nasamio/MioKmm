package com.mio

import io.ktor.client.*

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


// 初始化ktor client
expect fun httpClient(config: HttpClientConfig<*>.() -> Unit = {}): HttpClient

// 请求域名
expect fun getRequestHost(): String

// 请求端口
expect fun getRequestPort(): Int
