package bean.smms

import kotlinx.serialization.Serializable

@Serializable
data class SmmsResponse(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: List<SmmsImage>,
    val RequestId: String,
    val CurrentPage: Int,
    val TotalPages: Int,
    val PerPage:Int,
    val Count: Int,
)
