package com.mio.pages.test

import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object TestState {
    // 是否处于测试模式 测试模式只能进入测试页面
    val testMode = true

    val testList = mutableListOf(
        TestData("图片选择") {
//            choosePic {
//                println("图片选择: $it")
//            }
            GlobalScope.launch {
                FileKit.pickFile(
                    type = PickerType.Image,
                    mode = PickerMode.Single,
                )?.let {
                    println("图片选择: ${it.path},name: ${it.name}")

                    GlobalScope.launch {
                        it.readBytes().let {
                            println("数据内容: ${it.joinToString { it.toString() }}")
                        }
                    }
                }
            }
        },
        TestData("图片上传", {}),
    )


}