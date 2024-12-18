package com.mio

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.os.Build
import io.ktor.client.*
import io.ktor.client.engine.android.*
import java.lang.ref.WeakReference

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

lateinit var contextRef: WeakReference<Context>
val context get() = contextRef.get() ?: error("Context is not initialized")

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Android) {
    config(this)
}


actual fun getRequestHost(): String = "http://192.168.3.10"

actual fun getRequestPort(): Int = 8080

/**
 * android: 选择本地图片 返回图片绝对路径
 */
actual fun choosePic(onRes: (String) -> Unit) {

}