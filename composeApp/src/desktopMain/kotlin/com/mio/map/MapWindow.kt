package com.mio.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import com.google.gson.Gson
import com.mio.map.bean.Feature
import com.mio.map.bean.GeoJson
import com.mio.testMap
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.ic_menu
import org.jetbrains.compose.resources.painterResource
import java.io.File
import java.security.MessageDigest
import java.util.Random

@Composable
fun MapWindow(
    modifier: Modifier = Modifier
) {
    val windowState = rememberWindowState(
        width = 960.dp,
        height = 730.dp,
        position = WindowPosition(Alignment.Center),
    )

    Window(
        onCloseRequest = { testMap.value = false },
        state = windowState,
        icon = painterResource(Res.drawable.ic_menu),
        title = "Map",
    ) {
        Map(modifier)
    }
}

val filePath =
    "D:\\project\\study\\MioKmm\\MioKmm\\composeApp\\src\\desktopMain\\resources\\china-city.json"

fun loadGeoJson(filePath: String): GeoJson {
    val jsonString = File(filePath).readText()
    return Gson().fromJson(jsonString, GeoJson::class.java)
}

data class Point(var x: Double, var y: Double)

val random = Random()

@Composable
fun Map(
    modifier: Modifier = Modifier
) {
    var data by remember { mutableStateOf<GeoJson?>(null) }
    // 比例尺倒数
    var scale by remember { mutableStateOf(12.472f) }
    // 中心位置
    var center by remember { mutableStateOf(Point(100.83468981041254, 32.92010196029281)) }
    // 缩放系数
    val scaleFactor by remember { mutableStateOf(1.2f) }
    // 显示层级
    val showLevel by remember { mutableStateOf("city") }
    val textMeasurer = rememberTextMeasurer()

    LaunchedEffect(1) {
        loadGeoJson(filePath).let {
            println("一共有 ${it.features?.size} 个模块")
            data = it
        }
    }

    Text("Map")
    Box(
        modifier = modifier.fillMaxSize()
            .padding(10.dp)
            .border(1.dp, Color.Red)
    ) {
        Canvas(
            modifier = modifier.fillMaxSize()
                .pointerInput(Unit) {
                    // 拖拽检测
                    detectDragGestures { change, dragAmount ->
                        println("move:$dragAmount")
                        // 更新偏移量
                        center = center.copy(
                            x = center.x - dragAmount.x / scale,
                            y = center.y - dragAmount.y / scale
                        )
                    }
                }
                .pointerInput(Unit) {
                    // 缩放检测
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val scrollDelta = event.changes.firstOrNull()?.scrollDelta

                            when (scrollDelta?.y) {
                                -1f -> scale *= scaleFactor
                                1f -> scale /= scaleFactor
                            }
                        }
                    }
                }

        ) {
            println("触发重新绘制，当前中心点为(${center.x},${center.y}), 比例尺为$scale")


            data?.let { it ->
                it.features?.forEach { feature ->
                    when (feature?.geometry?.type) {
                        "MultiPolygon" -> { // 多面
                            drawFeature(
                                feature = feature,
                                scale = scale,
                                center = center,
                                showLevel = showLevel,
                                textMeasurer = textMeasurer,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
fun DrawScope.drawFeature(
    feature: Feature,
    scale: Float,
    center: Point,
    showLevel: String,
    textMeasurer: TextMeasurer,
) {
    println("${feature.properties}")

    if (showLevel != feature.properties?.level) return


    val bgColor = getBgColorByAdcode(feature.properties.adcode)

    (feature.geometry?.coordinates!![0] as List<List<List<Double>>>).let { it ->
        it.forEach {
            drawArea(
                points = it,
                scale = scale,
                center = center,
                bgColor = bgColor,
            )
        }
    }

    // 绘制区域名称
    val drawName = false
    if (drawName) {
        val text = feature.properties.name ?: ""
        val textCenter = feature.properties.center as List<Double>

        if (text.isNotEmpty()) {
            val textSize = textMeasurer.measure(text).size
            val textWidth = textSize.width
            val textHeight = textSize.height
            var textX = textCenter[0] - textWidth / 2
            var textY = textCenter[1] - textHeight / 2
            if (textX + textWidth > size.width) {
                textX = (size.width - textWidth).toDouble()
            }
            if (textY + textHeight > size.height) {
                textY = (size.height - textHeight).toDouble()
            }

            drawText(
                textMeasurer = textMeasurer,
                text = text,
                topLeft = Offset(
                    (size.width / 2f + (textX - center.x) * scale).toFloat(),
                    (size.height / 2f + -1f * (textY - center.y) * scale).toFloat()
                )
            )
        }
    }
}


fun DrawScope.drawArea(
    points: List<List<Double>>,
    scale: Float,
    center: Point,
    bgColor: Color = Color.White,
    name: String? = "",
    namePos: List<Double>? = null,
) {
    val canvasCx = size.width / 2
    val canvasCy = size.height / 2

    Path().apply {
        for (point in points) {
            val x = (point[0] - center.x) * scale
            val y = (-(point[1] - center.y)) * scale
//            println("当前点为：($x,$y)")
            if (isEmpty) {
                moveTo(canvasCx + x.toFloat(), canvasCy + y.toFloat())
            } else {
                lineTo(canvasCx + x.toFloat(), canvasCy + y.toFloat())
            }
        }
        drawPath(
            path = this,
            color = bgColor,
        )
        drawPath(
            path = this,
            color = Color.Black.copy(alpha = .8f),
            style = Stroke(width = 1f)
        )
    }
}

fun getBgColorByAdcode(adcode: String?): Color {
    if (adcode == null) {
        return Color.Gray // 返回默认颜色
    }

    // 获取字符串的哈希值
    val hash = MessageDigest.getInstance("MD5").digest(adcode.toByteArray())
    // 使用哈希值的前三个字节生成颜色
    val r = (hash[0].toInt() and 0xFF)
    val g = (hash[1].toInt() and 0xFF)
    val b = (hash[2].toInt() and 0xFF)

    // 返回生成的颜色
    return Color(r, g, b)
}