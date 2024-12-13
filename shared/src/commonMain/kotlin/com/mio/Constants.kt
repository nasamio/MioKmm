package com.mio

const val SERVER_PORT = 8080

fun isWasm() = getPlatform().name.contains("Wasm")