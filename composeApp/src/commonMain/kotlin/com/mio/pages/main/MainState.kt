package com.mio.pages.main

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
object MainState {
    val studyMode = MutableStateFlow("map") // 未进入学习页面

    val showToast = MutableStateFlow("")

    fun toast(msg: String) {
        showToast.value = msg

//        GlobalScope.launch {
//            delay(1_000)
//            showToast.value = ""
//        }
    }
}