package com.mio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun HoveredBox(
    modifier: Modifier,
    hoverColor: Color = Color(0x0e000000),
    defaultColor : Color = Color.Transparent,
    onHoverChange: (Boolean) -> Unit = {},
    isCheck: Boolean = false, // 如果是check，该组件仅为带有背景颜色的box
    shape: Shape = RectangleShape,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hoveredAsState = interactionSource.collectIsHoveredAsState()
    LaunchedEffect(hoveredAsState.value) {
        if (!isCheck) onHoverChange(hoveredAsState.value)
    }

    Box(
        modifier = modifier.background(
            color = if (hoveredAsState.value || isCheck) hoverColor else defaultColor,
            shape = shape,
        ).clickable(
            interactionSource = interactionSource,
            indication = null,
        ) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
fun HoverIcon(
    modifier: Modifier,
    resource: DrawableResource,
    hoverTintColor: Color = Color(0xff26b9f3),
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hoveredAsState = interactionSource.collectIsHoveredAsState()

    Icon(
        modifier = modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
        ) { onClick() },
        painter = painterResource(resource),
        contentDescription = null,
        tint = if (hoveredAsState.value) hoverTintColor else Color.Unspecified,
    )
}
