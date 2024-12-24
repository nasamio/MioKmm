package com.mio

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import androidx.compose.ui.window.ComposeViewport
import io.ktor.client.fetch.*
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
//        CanvasBasedWindow("Coil3 CMP") {

        testFetch()

            App()
//        }
    }
}

fun testFetch() {
    // 执行js代码
    js("""
        var myHeaders = new Headers();
myHeaders.append("Authorization", "Cac8ge6lwmQ7CCLENFZ3KAFLYJ6s6AbI");
myHeaders.append("User-Agent", "Apifox/1.0.0 (https://apifox.com)");
myHeaders.append("Content-Type", "multipart/form-data");
myHeaders.append("Accept", "*/*");
myHeaders.append("Host", "sm.ms");
myHeaders.append("Connection", "keep-alive");

var requestOptions = {
   method: 'GET',
   headers: myHeaders,
   redirect: 'follow'
};

fetch("https://sm.ms/api/v2/upload_history?page=1", requestOptions)
   .then(response => response.text())
   .then(result => console.log(result))
   .catch(error => console.log('error', error));
    """)
}
