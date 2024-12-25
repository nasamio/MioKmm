package utils

import bean.smms.SmmsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn

/**
 * 测试学习sm.ms图床操作
 */
object SmmsHelper {
    const val BASE_URL = "https://sm.ms/api/v2"
    const val AUTH = "Cac8ge6lwmQ7CCLENFZ3KAFLYJ6s6AbI"

    suspend fun token(): Flow<String> {
        return NetHelper.post<String>(
            "${BASE_URL}token",
            mapOf(
                "Authorization" to AUTH,
                "username" to "nasamio",
                "password" to "qq123456",
            )
        )
    }

    suspend fun uploadHistory(page: Int = 1): Flow<SmmsResponse> {
        return NetHelper.get<SmmsResponse>(
            "${BASE_URL}/upload_history",
            headers = mapOf(
                "Authorization" to AUTH,
            ),
            params = mapOf(
                "page" to page.toString()
            )
        )
    }
}