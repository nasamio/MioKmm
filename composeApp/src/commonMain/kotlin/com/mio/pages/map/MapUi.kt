package com.mio.pages.map

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.vinceglb.filekit.core.FileKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import miokmm.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Resource

@Composable
fun MapUi(
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        Text(text = "MapUi")
    }
}



