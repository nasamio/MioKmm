package com.mio.pages.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import miokmm.composeapp.generated.resources.Res
import miokmm.composeapp.generated.resources.test
import org.jetbrains.compose.resources.painterResource

private enum class ImageState {
    Small, Large
}

@Composable
fun animationUi() {
    var imageState by remember { mutableStateOf(ImageState.Small) }
    val transition = updateTransition(targetState = imageState, label = "ImageState Transition")

    val interactionSource = remember { MutableInteractionSource() }
    val hoveredAsState = interactionSource.collectIsHoveredAsState()

    LaunchedEffect(hoveredAsState.value) {
        imageState = if (hoveredAsState.value) {
            ImageState.Large
        } else {
            ImageState.Small
        }
    }

    val borderColor by transition.animateColor(label = "ImageState Color Transition") {
        when (it) {
            ImageState.Small -> Color.Green
            ImageState.Large -> Color.Magenta
        }
    }

    val size by transition.animateDp(label = "ImageState Size Transition") {
        when (it) {
            ImageState.Small -> 90.dp
            ImageState.Large -> 240.dp
        }
    }

    val rotation by transition.animateFloat(label = "ImageState Rotation Transition") {
        when (it) {
            ImageState.Small -> 0f
            ImageState.Large -> 360f
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = interactionSource,
            ) { },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(Res.drawable.test),
            contentDescription = null,
            modifier = Modifier
                .size(size)
                .rotate(rotation)
                .clip(shape = CircleShape)
                .border(color = borderColor, shape = CircleShape, width = 3.dp)
        )

        Button(
            onClick = {
                imageState = if (imageState == ImageState.Small) {
                    ImageState.Large
                } else {
                    ImageState.Small
                }
            }
        ) {
            Text(text = "切换")
        }
    }

}

@androidx.compose.desktop.ui.tooling.preview.Preview
@Composable
fun animationPreview() {
    animationUi()
}