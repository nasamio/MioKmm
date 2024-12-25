package com.mio

import bean.ib.IbFile
import bean.ib.IbResponse
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import java.io.File

const val FILE_PATH = "D:\\images\\imagebed"
const val PATH_PREFIX = "http://192.168.3.10:8080/files/"

fun Route.ImageRouting() {
    static("/files") {
        files(FILE_PATH)
    }

    // 获取 FILE_PATH 下所有非文件夹的文件 并返回
    get("/listFiles") {
        val files = File(FILE_PATH)
        files.listFiles()
            ?.filter { it.isFile }
            ?.map { IbFile(it.name, it.length(), "$PATH_PREFIX${it.name}") }
            ?.let {
                call.callback(
                    IbResponse<List<IbFile>>(
                        success = true,
                        code = "200",
                        message = "success",
                        data = it
                    )
                )
            } ?: run {
            call.callback(
                IbResponse<List<IbFile>>(
                    success = false,
                    code = "404",
                    message = "not found",
                    data = emptyList()
                )
            )
        }
    }

    post("/upload") {
        val multipart = call.receiveMultipart()
        var imageUrl: String? = null

        multipart.forEachPart { part ->
            if (part is PartData.FileItem) {
                val fileName = part.originalFileName ?: "unknown"
                val fileBytes = part.streamProvider().readBytes()
                val file = File("$FILE_PATH/$fileName")

                // 确保上传目录存在
                file.writeBytes(fileBytes)
                imageUrl = "$PATH_PREFIX$fileName"
            }
            part.dispose()
        }

        if (imageUrl != null) {
            call.callback(IbResponse(true, "200", "success", imageUrl!!))
        } else {
            call.callback(IbResponse(false, "400", "failed", ""))
        }
    }
}