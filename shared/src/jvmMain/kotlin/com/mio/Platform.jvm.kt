package com.mio

import io.ktor.client.*
import io.ktor.client.engine.apache.*
import java.io.File
import javax.swing.JFileChooser
import javax.swing.SwingUtilities

class JVMPlatform : Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()


actual fun httpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Apache) {
    engine {
        followRedirects = true
        socketTimeout = 10_000
        connectTimeout = 10_000
        connectionRequestTimeout = 20_000
        customizeClient {
            setMaxConnTotal(1000)
            setMaxConnPerRoute(100)
        }
        customizeRequest {
            // TODO: request transformations
        }
    }
    config(this)
}

actual fun getRequestHost(): String = "192.168.3.10"

actual fun getRequestPort(): Int = 8080


/**
 * jvm: 选择本地图片 返回图片绝对路径
 */
actual fun choosePic(onRes: (String) -> Unit) {
    // 在 Swing 中选择文件的操作需要在事件调度线程中进行
    SwingUtilities.invokeLater {
        val fileChooser = JFileChooser()
        // 设置文件选择器的过滤器，只显示图片文件
        fileChooser.fileFilter = javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif")

        // 打开文件选择对话框
        val returnValue = fileChooser.showOpenDialog(null)
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            val selectedFile: File = fileChooser.selectedFile
            // 返回选中的文件的绝对路径
            onRes(selectedFile.absolutePath)
        } else {
            // 如果没有选择文件，可以选择返回一个空字符串或其他处理
            onRes("")
        }
    }
}