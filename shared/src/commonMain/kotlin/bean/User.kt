
package bean

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val pwd: String,
){
    fun tojson() = """{"id":$id,"name":"$name","pwd":"$pwd"}"""
}
