package com.mio.map.bean

data class Properties(
    val acroutes: List<Int?>?,
    val adchar: String?,
    val adcode: String?,
    val center: List<Double?>?,
    val centroid: List<Double?>?,
    val childrenNum: Int?,
    val level: String?,
    val name: String?,
    val parent: Parent?,
    val subFeatureIndex: Int?
)