package bean.smms

import kotlinx.serialization.Serializable

@Serializable
data class SmmsImage(
    val width: Int,
    val height: Int,
    val filename: String,
    val storename: String,
    val size: Int,
    val path: String,
    val hash: String,
    val created_at: String,
    val url: String,
    val delete: String,
    val page: String,
)
