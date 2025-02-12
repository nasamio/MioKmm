package com.mio.pages.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.test
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebHomeUi() {
    var tableSelect by remember { mutableStateOf(0) }
    val tabs = listOf("Tab 1", "Tab 2", "Tab 3")

    GradientBackgroundAnimation(enable = true) {
        Row(
            modifier = Modifier.height(60.dp)
                .fillMaxWidth(),
        ) {
            Spacer(Modifier.width(10.dp))
            Image(
                painter = painterResource(Res.drawable.test),
                contentDescription = "test",
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Crop,
            )

            Spacer(Modifier.width(10.dp))


            SecondaryTabRow(
                selectedTabIndex = tableSelect,
                modifier = Modifier.height(60.dp).weight(1f),
                containerColor = Color.Transparent,
                indicator = @Composable {
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tableSelect, matchContentSize = true),
                        color = Color.White.copy(alpha = 0.8f),
                    )
                },
            ) {
                tabs.forEachIndexed { index, s ->
                    Tab(
                        selected = tableSelect == index,
                        onClick = {
                            tableSelect = index
                        },
                        text = {
                            Text(
                                text = s,
                                fontSize = 20.sp,
                            )
                        },
                        selectedContentColor = Color.White.copy(alpha = 0.8f),
                        unselectedContentColor = Color.White.copy(alpha = 0.45f),
                    )
                }
            }
        }

        Card(
            modifier = Modifier.width(200.dp)
                .height(100.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.DarkGray,
            elevation = 4.dp,
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = Color.Yellow.copy(alpha = .5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("测试card")
            }
        }

        ElevatedCard(
            modifier = Modifier.width(200.dp)
                .height(100.dp)
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = Color.Yellow.copy(alpha = .5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("测试ElevatedCard")
            }
        }

        OutlinedCard(
            modifier = Modifier.width(200.dp)
                .height(100.dp)
                .padding(10.dp),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(10.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(color = Color.Yellow.copy(alpha = .5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("测试OutlinedCard")
            }
        }

        Book()
    }
}

@Composable
fun Book() {
    val interactionSource = remember { MutableInteractionSource() }
    val hoveredAsState = interactionSource.collectIsHoveredAsState()

    Card(
        shape = RoundedCornerShape(30.dp),
        backgroundColor = Color(0xffff4141),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.height(60.dp)
                .widthIn(min = 60.dp)
                .wrapContentWidth()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                    },
                )
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Rounded.Create,
                contentDescription = "test",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            AnimatedVisibility(hoveredAsState.value) {
                Row {
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = "hello",
                        color = Color.White,
                        fontSize = 20.sp,
                        // 往下偏移10.dp
                        modifier = Modifier.offset(y = 5.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun GradientBackgroundAnimation(
    enable: Boolean = true,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (enable) {
            var colorIndex by remember { mutableStateOf(0) }
            val colors = listOf(
                Color(0xfff02fc2),
                Color(0xff6094ea),
                Color(0xff7117ea),
                Color(0xffce9ffc),
                Color(0xf9239bff),
            )

            val transition = updateTransition(targetState = colorIndex, label = "Color Transition")

            LaunchedEffect(Unit) {
                while (true) {
                    delay(3500) // 每2秒切换一次颜色
                    colorIndex = (colorIndex + 1) % colors.size
                }
            }

            val animatedColor1 by transition.animateColor(
                transitionSpec = {
                    tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                },
                label = "First Color"
            ) { index ->
                colors[index]
            }

            val animatedColor2 by transition.animateColor(
                transitionSpec = {
                    tween(durationMillis = 2000, easing = FastOutSlowInEasing)
                },
                label = "Second Color"
            ) { index ->
                colors[(index + 1) % colors.size]
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                val gradient = Brush.linearGradient(
                    colors = listOf(animatedColor1, animatedColor2),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height)
                )
                drawRect(brush = gradient)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xff43cbff),
                                Color(0xff9708cc),
                            ),
                        ),
                    ),
            ) {

            }
        }

        Column {
            content()
        }
    }
}

