package utils

import bean.User
import com.mio.getRequestHost
import com.mio.getRequestPort
import com.mio.httpClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.kotlinx.serializer.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

/**
 * 网络请求 使用ktor进行网络请求
 */
object NetHelper {
    val httpClient: HttpClient by lazy {
        httpClient {
            // 这里可以配置你的HttpClient
            install(DefaultRequest) {
                url {
                    host = getRequestHost()
                    port = getRequestPort()
                }
            }
            install(ContentNegotiation) {
                json()
            }

        }
    }


    fun close() {
        httpClient.close()
    }

    inline fun <reified T> get(url: String, params: Map<String, String> = emptyMap()): Flow<T> {
        return flow {
            val response = httpClient.get(url) {
                params.forEach { parameter(it.key, it.value) }
            }
            val result = response.body<T>()
            emit(result)
        }.catch { throwable: Throwable ->
            logError("Error occurred during network request", throwable)
        }.onCompletion { cause ->
            close()
        }.flowOn(Dispatchers.Default)
    }


    inline fun <reified T> post(url: String, params: Map<String, String> = emptyMap()): Flow<T> {
        return flow {
            val response = httpClient.post(url) {
                params.forEach { parameter(it.key, it.value) }
            }
            val result = response.body<T>()
            emit(result)
        }.catch { throwable: Throwable ->
            logError("Error occurred during network request", throwable)
        }.onCompletion { cause ->
            close()
        }.flowOn(Dispatchers.Default)
    }


    fun logError(message: String, throwable: Throwable) {
        // 这里可以使用你喜欢的日志框架进行日志记录
        // 例如，使用println简单输出到控制台
        println("Net Error:$message: ${throwable.message}")
        throwable.printStackTrace()
    }

    /**
     * 下面的是具体的网络请求
     */

    suspend fun test(): Flow<String> {
        return get<String>("/")
    }

    suspend fun login(username: String, password: String): Flow<User> {
        return post<User>("/login", mapOf("name" to username, "pwd" to password))
    }
}
