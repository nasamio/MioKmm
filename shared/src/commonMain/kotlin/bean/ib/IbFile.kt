package bean.ib

import kotlinx.serialization.Serializable

@Serializable
data class IbFile(
    val filename: String,
    val size: Long,
    val url: String,
)
