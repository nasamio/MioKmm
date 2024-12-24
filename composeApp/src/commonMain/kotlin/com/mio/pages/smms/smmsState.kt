package com.mio.pages.smms

import bean.smms.SmmsImage
import kotlinx.coroutines.flow.MutableStateFlow

object smmsState {
    val smmsList = MutableStateFlow<List<SmmsImage>>(emptyList())
}