package bean.ib

import kotlinx.serialization.Serializable

@Serializable
data class IbResponse<T>(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: T,
)
