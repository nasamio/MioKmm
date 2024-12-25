package com.mio.pages.ib

import bean.ib.IbFile
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.flow.MutableStateFlow
import utils.IbHelper

object IbState {
    suspend fun choose2Upload() {
        FileKit.pickFile(
            type = PickerType.Image,
            mode = PickerMode.Single,
        )?.let {
            println("图片选择: ${it.path},name: ${it.name}")

            IbHelper.upload(it.name, it.readBytes()).collect {
                println("choose2Upload: $it")
            }

        }
    }

    val ibList = MutableStateFlow<List<IbFile>>(emptyList())
}