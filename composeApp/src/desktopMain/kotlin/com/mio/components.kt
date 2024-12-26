package com.mio

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HoveredBox(
    modifier: Modifier,
    hoverColor: Color = Color(0x0e000000),
    onHoverChange: (Boolean) -> Unit = {},
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hoveredAsState = interactionSource.collectIsHoveredAsState()
    LaunchedEffect(hoveredAsState.value) {
        onHoverChange(hoveredAsState.value)
    }

    Box(
        modifier = modifier.background(
            color = if (hoveredAsState.value) hoverColor else Color.Transparent
        ).clickable(
            interactionSource = interactionSource,
            indication = null,
        ) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}
