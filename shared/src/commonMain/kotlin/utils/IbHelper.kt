package utils

import bean.ib.IbFile
import bean.ib.IbResponse
import com.mio.httpClient
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import utils.NetHelper.close
import utils.NetHelper.httpClient
import utils.NetHelper.logError

object IbHelper {
    const val BASE_URL = "http://192.168.3.10:8080"

    suspend fun listFiles(): Flow<IbResponse<List<IbFile>>> {
        return NetHelper.get<IbResponse<List<IbFile>>>(
            "${BASE_URL}/listFiles",
        )
    }

    // 这个必须每次新建一个client
    suspend fun upload(
        fileName: String,
        fileBytes: ByteArray,
    ): Flow<IbResponse<String>> {
        val client = httpClient {
            install(ContentNegotiation) {
                json()
            }
        }
        return flow {
            val response = client.submitFormWithBinaryData("$BASE_URL/upload", formData = formData {
                append("file", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, ContentType.Image.JPEG.toString()) // 根据文件类型设置
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            })
            emit(response.body<IbResponse<String>>())
        }.catch { throwable: Throwable ->
            logError("Error occurred during network request", throwable)
        }.onCompletion { cause ->
            close()
        }.flowOn(Dispatchers.Default)
    }
}