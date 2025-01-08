package com.mio.map.bean

data class Feature(
    val geometry: Geometry?,
    val properties: Properties?,
    val type: String?
)