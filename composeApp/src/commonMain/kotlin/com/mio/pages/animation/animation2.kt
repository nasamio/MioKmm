import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun Animation2() {
    val timeDuration by mutableStateOf(16L)
    var canvasSize by remember { mutableStateOf(Size(0f, 0f)) }
    val f by remember { mutableStateOf(.95f) } //摩擦力
    val e by remember { mutableStateOf(.95f) } // 碰撞能量损耗

    // 初始化随机位置 随机速度
    var circles by remember {
        mutableStateOf(
            List(100) {
                Circle(
                    Random.nextInt(40, 1920-40).toFloat(),
                    Random.nextInt(40, 1080-40).toFloat(),
                    Random.nextInt(-200, 200).toFloat(),
                    Random.nextInt(-200, 200).toFloat(),
                )
            }
        )
    }


    LaunchedEffect(Unit) {
        while (true) {
            delay(timeDuration)

            circles = circles.map {
                it.copy().apply {
                    // 更新位置和速度
                    vx += it.ax * timeDuration / 1000f
                    vy += it.ay * timeDuration / 1000f
                    cx = it.cx + it.vx * timeDuration / 1000f
                    cy = it.cy + it.vy * timeDuration / 1000f
                    // 判断边界情况
                    if (cx - radius < 0f) {
                        cx = radius
                        vx *= -1f
                    } else if (cx + radius > canvasSize.width) {
                        cx = canvasSize.width - radius
                        vx *= -1f
                    }

                    if (cy - radius < 0f) {
                        cy = radius
                        vy *= -1f
                    } else if (cy + radius > canvasSize.height) {
                        cy = canvasSize.height - radius
                        vy *= -e
                        vx *= f
                    }


                }
            }

            for (i in 0 until circles.size - 1) {
                for (j in i + 1 until circles.size) {
                    val c1 = circles[i]
                    val c2 = circles[j]
                    val dist = sqrt((c1.cx - c2.cx) * (c1.cx - c2.cx) + (c1.cy - c2.cy) * (c1.cy - c2.cy))
                    if (dist < c1.radius + c2.radius) {
                        // v0new = ((m0-m1)*v0 + 2m1v1)/(m0+m1)
                        c1.vx = ((c1.mass - c2.mass) * c1.vx + 2 * c2.mass * c2.vx) / (c1.mass + c2.mass)
                        c2.vx = ((c2.mass - c1.mass) * c2.vx + 2 * c1.mass * c1.vx) / (c1.mass + c2.mass)
                        // 更新列表中的c1 c2
                        circles = circles.map {
                            if (it == c1) {
                                c1
                            } else if (it == c2) {
                                c2
                            } else {
                                it
                            }
                        }
                    }
                }
            }

            println("Animation2: (${circles[0].cx},${circles[0].cy}),vx:${circles[0].vx}")
        }
    }

    Canvas(
        modifier = Modifier.fillMaxWidth()
            .padding(10.dp)
            .border(
                width = 1.dp,
                color = Color.DarkGray,
                shape = RectangleShape,
            ).graphicsLayer {
                canvasSize = size
            }
    ) {
        circles.forEach {
            drawCircle(it)
        }
    }
}

data class Circle(
    var cx: Float,
    var cy: Float,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var ax: Float = 0f,
    var ay: Float = 9.8f * 0f,
    val radius: Float = 30f,
    val color: Color = randomColor(),
) {
    val mass: Float
        get() = radius * radius
}

// 返回随机颜色
fun randomColor(): Color {
    return Color(
        Random.nextInt(0, 255),
        Random.nextInt(0, 255),
        Random.nextInt(0, 255),
    )
}

fun DrawScope.drawCircle(
    circle: Circle,
) {
    drawCircle(
        center = Offset(circle.cx, circle.cy),
        radius = circle.radius,
        color = circle.color,
    )
}