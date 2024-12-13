package utils

import com.mio.httpClient
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.errors.*

/**
 * 网络请求 使用ktor进行网络请求
 */
object NetHelper {
    private val client: HttpClient by lazy {
        httpClient {
            // 这里可以配置你的HttpClient
        }
    }

    suspend fun test(): String {
        return get("http://127.0.0.1:8080/")
    }

    suspend fun get(url: String): String {
        return try {
            client.get(url).bodyAsText()
        } catch (e: Exception) {
            // 记录异常日志
            logError("Error occurred while making GET request to $url", e)
            // 可以选择返回一个默认值或空字符串
            ""
        }
    }

    private fun logError(message: String, throwable: Throwable) {
        // 这里可以使用你喜欢的日志框架进行日志记录
        // 例如，使用println简单输出到控制台
        println("$message: ${throwable.message}")
        throwable.printStackTrace()
    }
}
