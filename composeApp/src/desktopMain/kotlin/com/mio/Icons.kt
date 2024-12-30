package com.mio

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Rounded.Message: ImageVector
    get() {
        if (_message != null) {
            return _message!!
        }
        _message = materialIcon(name = "Rounded.Add2") {
            materialPath {
                val roundValue = 5f

                moveTo(18.0f, 13.0f)
                horizontalLineToRelative(-11.0f)

                curveToRelative(roundValue - 1f, 0f, -1f, -roundValue, -1f, -1f)
                curveToRelative(0f, roundValue - 1f, roundValue, -1f, 1f, -1f)

                horizontalLineToRelative(11.0f)
                verticalLineToRelative(1.0f)
                close()
            }
        }.apply {
            println("size: $defaultWidth,$defaultHeight")
        }
        return _message!!
    }

private var _message: ImageVector? = null