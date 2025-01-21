import java.awt.Desktop
import java.net.URI

actual fun openBrowser(url: String) {
    // jvm平台 打开浏览器并访问地址
    Desktop.getDesktop().browse(URI(url))
}