package bean

import kotlinx.serialization.Serializable

/**
 * 前后端交互base基类
 */
@Serializable
data class KmmResponse<T>(
    val code: Int, // http状态码 200:正常 500:服务器异常
    val msg: String, // 返回消息
    val data: T? = null,
)
